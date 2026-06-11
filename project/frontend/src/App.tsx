import MapView from './features/poi-catalog/components/MapView';
import PoiDrawer from './features/poi-catalog/components/PoiDrawer';
import FloatingSearch from './shared/components/FloatingSearch';
import FloatingAvatar from './features/iam/components/FloatingAvatar';
import FabAdd from './features/verification/components/FabAdd';

/**
 * Root application shell.
 * Composes the full-screen map with floating UI overlays.
 */
function App() {
  return (
    <div className="relative h-full w-full">
      {/* Base layer — full-screen Leaflet map */}
      <MapView />

      {/* Panel layer — POI list drawer */}
      <PoiDrawer />

      {/* Overlay layer — floating UI elements */}
      <FloatingSearch />
      <FloatingAvatar />
      <FabAdd />
    </div>
  );
}

export default App;
