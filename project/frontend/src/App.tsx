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
  
  // Adding mode states
  const [isAddingMode, setIsAddingMode] = useState(false);
  const [draftPosition, setDraftPosition] = useState<{lat: number, lng: number} | null>(null);

  return (
    <div className="relative h-full w-full">
      {/* Base layer — full-screen Leaflet map */}
      <MapView 
        selectedPoi={selectedPoi} 
        setSelectedPoi={setSelectedPoi}
        isAddingMode={isAddingMode}
        setDraftPosition={setDraftPosition}
        draftPosition={draftPosition}
      />

      {/* Panel layer — POI list drawer */}
      <PoiDrawer
        activeCategory={activeCategory}
        setActiveCategory={setActiveCategory}
        selectedPoi={selectedPoi}
        setSelectedPoi={setSelectedPoi}
        draftPosition={draftPosition}
        onCancelDraft={() => {
          setDraftPosition(null);
          setIsAddingMode(false);
        }}
      />

      {/* Overlay layer — floating UI elements */}
      <FloatingSearch />
      <FloatingAvatar />
      <FabAdd 
        isAddingMode={isAddingMode} 
        toggleAddingMode={() => {
          setIsAddingMode(!isAddingMode);
          if (isAddingMode) setDraftPosition(null); // Clear draft if cancelling
        }} 
      />
    </div>
  );
}

export default App;
