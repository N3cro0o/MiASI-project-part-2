import { useState } from 'react';
import MapView from './features/poi-catalog/components/MapView';
import PoiDrawer from './features/poi-catalog/components/PoiDrawer';
import FloatingSearch from './shared/components/FloatingSearch';
import FloatingAvatar from './features/iam/components/FloatingAvatar';
import FabAdd from './features/verification/components/FabAdd';
import type { Poi } from './features/poi-catalog/models/Poi';

/**
 * Root application shell.
 * Composes the full-screen map with floating UI overlays.
 */
function App() {
  const [activeCategory, setActiveCategory] = useState<string>('ALL');
  const [selectedPoi, setSelectedPoi] = useState<Poi | null>(null);

  return (
    <div className="relative h-full w-full">
      {/* Base layer — full-screen Leaflet map */}
      <MapView selectedPoi={selectedPoi} setSelectedPoi={setSelectedPoi} />

      {/* Panel layer — POI list drawer */}
      <PoiDrawer
        activeCategory={activeCategory}
        setActiveCategory={setActiveCategory}
        selectedPoi={selectedPoi}
        setSelectedPoi={setSelectedPoi}
      />

      {/* Overlay layer — floating UI elements */}
      <FloatingSearch />
      <FloatingAvatar />
      <FabAdd />
    </div>
  );
}

export default App;
