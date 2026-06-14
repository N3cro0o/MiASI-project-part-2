import { useState } from 'react';
import MapView from './features/poi-catalog/components/MapView';
import PoiDrawer from './features/poi-catalog/components/PoiDrawer';
import type { DrawerView } from './features/poi-catalog/components/PoiDrawer';
import FloatingAvatar from './features/iam/components/FloatingAvatar';
import AuthModal from './features/iam/components/AuthModal';
import FabAdd from './features/verification/components/FabAdd';
import type { Poi } from './features/poi-catalog/models/Poi';
import { useAuth } from './features/iam/context/AuthContext';

/**
 * Root application shell.
 * Composes the full-screen map with floating UI overlays.
 */
function App() {
  const [activeCategory, setActiveCategory] = useState<string>('ALL');
  const [selectedPoi, setSelectedPoi] = useState<Poi | null>(null);

  // Adding mode states
  const [isAddingMode, setIsAddingMode] = useState(false);
  const [draftPosition, setDraftPosition] = useState<{ lat: number, lng: number } | null>(null);

  // Drawer visibility
  const [isDrawerOpen, setIsDrawerOpen] = useState(true);

  // Drawer view navigation
  const [currentView, setCurrentView] = useState<DrawerView>('CATALOG');

  // Auth modal visibility
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);

  // Auth state
  const { isAuthenticated } = useAuth();

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
        isOpen={isDrawerOpen}
        onToggle={() => setIsDrawerOpen(!isDrawerOpen)}
        currentView={currentView}
        setCurrentView={setCurrentView}
        isAuthenticated={isAuthenticated}
      />

      {/* Overlay layer — floating UI elements */}
      <FloatingAvatar onClick={() => {
        setIsAuthModalOpen(true); console.log(isAuthModalOpen);
      }} />
      {isAuthenticated && (
        <FabAdd
          isAddingMode={isAddingMode}
          toggleAddingMode={() => {
            setIsAddingMode(!isAddingMode);
            if (isAddingMode) setDraftPosition(null); // Clear draft if cancelling
          }}
        />
      )}

      {/* Auth Modal */}
      <AuthModal isOpen={isAuthModalOpen} onClose={() => setIsAuthModalOpen(false)} />
    </div>
  );
}

export default App;
