import { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, useMap, useMapEvents } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import type { Poi } from '../models/Poi';
import { usePois } from '../api/usePois';
import { getCategoryMeta } from '../constants/categories';

// Default map center — Wrocław Old Town
const WROCLAW_CENTER: [number, number] = [51.107885, 17.038538];
const DEFAULT_ZOOM = 13;

interface MapViewProps {
  selectedPoi: Poi | null;
  setSelectedPoi: (poi: Poi | null) => void;
  isAddingMode: boolean;
  draftPosition: { lat: number; lng: number } | null;
  setDraftPosition: (pos: { lat: number; lng: number }) => void;
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
  setDraftPosition
}) => {
  const { data: pois } = usePois();

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
      <MapClickHandler isAddingMode={isAddingMode} setDraftPosition={setDraftPosition} />

      {/* Render draft marker if dropping a pin */}
      {draftPosition && (
        <Marker position={[draftPosition.lat, draftPosition.lng]} icon={draftIcon} />
      )}

      {/* Render existing POI markers */}
      {pois?.map((poi) => (
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
    </MapContainer>
  );
};

export default MapView;
