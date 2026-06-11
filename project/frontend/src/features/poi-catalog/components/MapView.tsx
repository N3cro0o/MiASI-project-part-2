import { MapContainer, TileLayer } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

// Default map center — Wrocław Old Town
const WROCLAW_CENTER: [number, number] = [51.107885, 17.038538];
const DEFAULT_ZOOM = 13;

/**
 * Full-screen Leaflet map centered on Wrocław.
 * Zoom controls are hidden — custom UI will be layered on top via App.tsx.
 */
const MapView: React.FC = () => {
  return (
    <MapContainer
      center={WROCLAW_CENTER}
      zoom={DEFAULT_ZOOM}
      zoomControl={false}
      className="absolute inset-0 h-full w-full"
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
    </MapContainer>
  );
};

export default MapView;
