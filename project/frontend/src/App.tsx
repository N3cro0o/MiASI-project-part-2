import { useState } from 'react';
import MapView from './features/poi-catalog/components/MapView';
import PoiDrawer from './features/poi-catalog/components/PoiDrawer';
import type { DrawerView } from './features/poi-catalog/components/PoiDrawer';
import FloatingAvatar from './features/iam/components/FloatingAvatar';
import AuthModal from './features/iam/components/AuthModal';
import FabAdd from './features/verification/components/FabAdd';
import type { Poi } from './features/poi-catalog/models/Poi';
import { useAuth } from './features/iam/context/AuthContext';
import { useKrasnals } from './features/poi-catalog/api/useKrasnals';

/**
 * Root application shell.
 * Composes the full-screen map with floating UI overlays.
 */
function App() {
  const [activeCategory, setActiveCategory] = useState<string>('ALL');
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [selectedPoi, setSelectedPoi] = useState<Poi | null>(null);

  // Adding mode states
  const [isAddingMode, setIsAddingMode] = useState(false);
  const [draftPosition, setDraftPosition] = useState<{ lat: number, lng: number } | null>(null);

  // Drawer visibility
  const [isDrawerOpen, setIsDrawerOpen] = useState(true);

  // Map Focus State
  const [mapFocusPoint, setMapFocusPoint] = useState<{ lat: number; lng: number } | null>(null);

  // Drawer view navigation
  const [currentView, setCurrentView] = useState<DrawerView>('CATALOG');

  // Auth modal visibility
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);

  // Auth state
  const { isAuthenticated, user } = useAuth();
  
  const isModerator = user?.role === 'EDITOR' || user?.role === 'ADMIN';
  const isAdmin = user?.role === 'ADMIN';

  // Fetch all pois here to synchronize map and list
  const { data: krasnals, isLoading, error } = useKrasnals();

  // Filter pois
  const filteredKrasnals = krasnals?.filter((poi) => {
    if (activeCategory !== 'ALL' && poi.category !== activeCategory) return false;
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      if (!poi.name.toLowerCase().includes(term) && !poi.description.toLowerCase().includes(term)) {
        return false;
      }
    }
    return true;
  }) || [];

  return (
    <div className="relative h-full w-full">
      {/* Base layer — full-screen Leaflet map */}
      <MapView
        selectedPoi={selectedPoi}
        setSelectedPoi={setSelectedPoi}
        isAddingMode={isAddingMode}
        setDraftPosition={(pos) => {
          setDraftPosition(pos);
          if (pos) {
            setCurrentView('MY_SUBMISSIONS');
            setIsDrawerOpen(true);
          }
        }}
        draftPosition={draftPosition}
        currentView={currentView}
        mapFocusPoint={mapFocusPoint}
        activeCategory={activeCategory}
        searchTerm={searchTerm}
        filteredPois={filteredKrasnals}
      />

      {/* Panel layer — POI list drawer */}
      <PoiDrawer
        activeCategory={activeCategory}
        setActiveCategory={setActiveCategory}
        searchTerm={searchTerm}
        setSearchTerm={setSearchTerm}
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
        isModerator={isModerator}
        isAdmin={isAdmin}
        setMapFocusPoint={setMapFocusPoint}
        filteredPois={filteredKrasnals}
        isLoading={isLoading}
        error={error}
      />

      {/* Overlay layer — floating UI elements */}
      <FloatingAvatar onClick={() => {
        if (isAuthenticated) {
          setCurrentView('PROFILE');
          setIsDrawerOpen(true);
        } else {
          setIsAuthModalOpen(true);
        }
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
