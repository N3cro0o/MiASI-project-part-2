import { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap, useMapEvents } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import type { Poi } from '../models/Poi';
import { usePois } from '../api/usePois';
import { getCategoryMeta } from '../constants/categories';
import { useAuth } from '../../iam/context/AuthContext';
import { useMySubmissions } from '../../verification/api/useMySubmissions';
import { usePendingSubmissions } from '../../verification/api/useModeration';
import type { DrawerView } from './PoiDrawer';

// Default map center — Wrocław Old Town
const WROCLAW_CENTER: [number, number] = [51.107885, 17.038538];
const DEFAULT_ZOOM = 13;

interface MapViewProps {
  selectedPoi: Poi | null;
  setSelectedPoi: (poi: Poi | null) => void;
  isAddingMode: boolean;
  draftPosition: { lat: number; lng: number } | null;
  setDraftPosition: (pos: { lat: number; lng: number }) => void;
  currentView?: DrawerView;
  mapFocusPoint?: { lat: number; lng: number } | null;
  activeCategory?: string;
  searchTerm?: string;
  filteredPois?: Poi[];
}

/**
 * Component to handle map panning when a POI is selected.
 */
const MapEffectController: React.FC<{ selectedPoi: Poi | null }> = ({ selectedPoi }) => {
  const map = useMap();

  useEffect(() => {
    if (selectedPoi) {
      map.flyTo([selectedPoi.position.latitude, selectedPoi.position.longitude], 16, {
        duration: 1.5,
      });
    }
  }, [selectedPoi, map]);

  return null;
};

/**
 * Component to handle map panning when mapFocusPoint changes.
 */
const FlyToController: React.FC<{ focusPoint?: { lat: number; lng: number } | null }> = ({ focusPoint }) => {
  const map = useMap();
  useEffect(() => {
    if (focusPoint) {
      map.flyTo([focusPoint.lat, focusPoint.lng], 16, { animate: true, duration: 1.5 });
    }
  }, [focusPoint, map]);
  return null;
};

/**
 * Captures map clicks to drop a pin in adding mode.
 */
const MapClickHandler: React.FC<{
  isAddingMode: boolean;
  setDraftPosition: (pos: { lat: number; lng: number }) => void;
}> = ({ isAddingMode, setDraftPosition }) => {
  useMapEvents({
    click(e) {
      if (isAddingMode) {
        setDraftPosition({ lat: e.latlng.lat, lng: e.latlng.lng });
      }
    },
  });
  return null;
};

/**
 * Full-screen Leaflet map centered on Wrocław.
 */
const MapView: React.FC<MapViewProps> = ({ 
  selectedPoi, 
  setSelectedPoi,
  isAddingMode,
  draftPosition,
  setDraftPosition,
  currentView = 'CATALOG',
  mapFocusPoint,
  activeCategory = 'ALL',
  searchTerm = '',
  filteredPois = []
}) => {
  const { isAuthenticated, user } = useAuth();
  const isModerator = user?.role === 'EDITOR' || user?.role === 'ADMIN';

  const { data: mySubmissions } = useMySubmissions(isAuthenticated && currentView === 'MY_SUBMISSIONS');
  const { data: pendingSubmissions } = usePendingSubmissions(isModerator && currentView === 'MODERATOR_QUEUE');

  // Helper to create a custom Tailwind-styled div icon for Leaflet
  const createCustomIcon = (poi: Poi) => {
    const meta = getCategoryMeta(poi.category);
    return L.divIcon({
      className: 'bg-transparent border-0',
      html: `
        <div class="flex h-8 w-8 items-center justify-center rounded-full bg-white text-lg shadow-lg ring-2 ring-wroclaw-sand transition-transform hover:scale-110">
          ${meta.emoji}
        </div>
      `,
      iconSize: [32, 32],
      iconAnchor: [16, 16],
    });
  };

  // Helper to create submission icon
  const createSubmissionIcon = (colorClass: string) => {
    return L.divIcon({
      className: 'bg-transparent border-0',
      html: `
        <div class="flex h-8 w-8 items-center justify-center rounded-full ${colorClass} text-white text-lg shadow-lg ring-2 ring-white transition-transform hover:scale-110">
          📍
        </div>
      `,
      iconSize: [32, 32],
      iconAnchor: [16, 16],
    });
  };

  const blueIcon = createSubmissionIcon('bg-blue-500');
  const orangeIcon = createSubmissionIcon('bg-orange-500');

  const draftIcon = L.divIcon({
    className: 'bg-transparent border-0',
    html: `
      <div class="flex h-8 w-8 items-center justify-center rounded-full bg-red-500 text-white text-lg shadow-xl ring-4 ring-red-200 animate-bounce">
        📍
      </div>
    `,
    iconSize: [32, 32],
    iconAnchor: [16, 32],
  });

  return (
    <MapContainer
      center={WROCLAW_CENTER}
      zoom={DEFAULT_ZOOM}
      zoomControl={false}
      className={`absolute inset-0 h-full w-full ${isAddingMode ? 'cursor-crosshair' : ''}`}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      <MapEffectController selectedPoi={selectedPoi} />
      <FlyToController focusPoint={mapFocusPoint} />
      <MapClickHandler isAddingMode={isAddingMode} setDraftPosition={setDraftPosition} />

      {/* Render draft marker if dropping a pin */}
      {draftPosition && (
        <Marker position={[draftPosition.lat, draftPosition.lng]} icon={draftIcon} />
      )}

      {/* Render existing POI markers */}
      {currentView === 'CATALOG' && filteredPois?.map((poi) => (
        <Marker
          key={poi.id}
          position={[poi.position.latitude, poi.position.longitude]}
          icon={createCustomIcon(poi)}
          eventHandlers={{
            click: () => {
              if (!isAddingMode) setSelectedPoi(poi);
            },
          }}
        />
      ))}

      {currentView === 'MY_SUBMISSIONS' &&
        mySubmissions?.map((sub) => {
          if (!sub.pos) return null;
          return (
            <Marker
              key={`sub-${sub.id}`}
              position={[sub.pos.latitude, sub.pos.longitude]}
              icon={createCustomIcon({
                id: sub.id,
                name: sub.name,
                description: 'Pending submission',
                position: sub.pos,
                category: 'submission',
                status: sub.status,
              })}
            >
              <Popup className="custom-popup">
                <div className="p-2">
                  <h3 className="font-bold text-wroclaw-dark">{sub.name}</h3>
                  <p className="text-xs text-wroclaw-dark/60 mt-1">Status: {sub.status}</p>
                </div>
              </Popup>
            </Marker>
          );
        })}

      {currentView === 'MODERATOR_QUEUE' && pendingSubmissions?.filter(s => s.krasnalPos).map((sub) => (
        <Marker
          key={sub.id}
          position={[sub.krasnalPos!.latitude, sub.krasnalPos!.longitude]}
          icon={orangeIcon}
        />
      ))}
    </MapContainer>
  );
};

export default MapView;
