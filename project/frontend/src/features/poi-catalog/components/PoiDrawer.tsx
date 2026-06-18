import React, { useEffect, useState } from 'react';
import PoiList from './PoiList';
import PoiDetails from './PoiDetails';
import PoiForm from '../../verification/components/PoiForm';
import MySubmissionsView from '../../verification/components/MySubmissionsView';
import VerificationQueue from '../../verification/components/VerificationQueue';
import UserProfile from '../../profile/components/UserProfile';
import AdminPanel from '../../admin/components/AdminPanel';
import FloatingSearch from '../../../shared/components/FloatingSearch';
import type { Poi } from '../models/Poi';
import { POI_CATEGORIES } from '../constants/categories';

export type DrawerView = 'CATALOG' | 'MY_SUBMISSIONS' | 'MODERATOR_QUEUE' | 'PROFILE' | 'ADMIN';

interface PoiDrawerProps {
  activeCategory: string;
  setActiveCategory: (cat: string) => void;
  selectedPoi: Poi | null;
  setSelectedPoi: (poi: Poi | null) => void;
  draftPosition: { lat: number; lng: number } | null;
  onCancelDraft: () => void;
  searchTerm: string;
  setSearchTerm: (term: string) => void;
  showVisitedOnly: boolean;
  setShowVisitedOnly: (visited: boolean) => void;
  isOpen: boolean;
  onToggle: () => void;
  currentView: DrawerView;
  setCurrentView: (view: DrawerView) => void;
  isAuthenticated?: boolean;
  isModerator?: boolean;
  isAdmin?: boolean;
  setMapFocusPoint: (pos: { lat: number; lng: number }) => void;
  filteredPois: Poi[];
  isLoading: boolean;
  error: Error | null;
}

/** Tab definitions for the navigation bar */
const DRAWER_TABS: { id: DrawerView; label: string; moderatorOnly?: boolean; authenticatedOnly?: boolean; adminOnly?: boolean }[] = [
  { id: 'CATALOG', label: '📍 Catalog' },
  { id: 'MY_SUBMISSIONS', label: '📝 My Submissions', authenticatedOnly: true },
  { id: 'MODERATOR_QUEUE', label: '🛡️ Verification Queue', moderatorOnly: true },
  { id: 'ADMIN', label: '⚙️ Manage Users', adminOnly: true },
];

/**
 * Smart Drawer — multi-view sidebar with tab navigation.
 *
 * Layout behavior:
 *  • Desktop (md+): side drawer sliding in from the left, ~30% viewport width.
 *  • Mobile (<md):  bottom sheet anchored to the lower portion of the screen.
 *
 * Supports smooth slide-in/out via CSS translate transitions.
 */
const PoiDrawer: React.FC<PoiDrawerProps> = ({
  activeCategory,
  setActiveCategory,
  selectedPoi,
  setSelectedPoi,
  draftPosition,
  onCancelDraft,
  searchTerm,
  setSearchTerm,
  showVisitedOnly,
  setShowVisitedOnly,
  isOpen,
  onToggle,
  currentView,
  setCurrentView,
  isAuthenticated = false,
  isModerator = false,
  isAdmin = false,
  setMapFocusPoint,
  filteredPois,
  isLoading,
  error,
}) => {
  // Redirect to CATALOG if the user is unauthenticated or unprivileged but sitting on a restricted view
  useEffect(() => {
    if (!isAuthenticated && ['MY_SUBMISSIONS', 'PROFILE'].includes(currentView)) {
      setCurrentView('CATALOG');
    }
    if (!isModerator && currentView === 'MODERATOR_QUEUE') {
      setCurrentView('CATALOG');
    }
    if (!isAdmin && currentView === 'ADMIN') {
      setCurrentView('CATALOG');
    }
  }, [isAuthenticated, isModerator, isAdmin, currentView, setCurrentView]);

  return (
    <div
      className={[
        'absolute z-[900] inset-y-0 left-0',
        'transition-transform duration-300 ease-in-out',
        isOpen ? 'translate-x-0' : '-translate-x-full',
      ].join(' ')}
    >
      {/* ── Drawer panel ──────────────────────────────────────────────── */}
      <aside
        id="poi-drawer"
        className={[
          'flex flex-col h-full',
          'bg-wroclaw-sand/95 backdrop-blur-sm shadow-2xl',
          'w-[30vw] min-w-[320px] max-w-[420px]',
          'rounded-r-2xl',
        ].join(' ')}
      >
        {/* Drag handle — visual affordance for the bottom sheet on mobile */}
        <div className="flex justify-center pt-3 pb-1 md:hidden">
          <span className="h-1 w-10 rounded-full bg-wroclaw-dark/30" />
        </div>

        {/* ── Tab Navigation Bar ─────────────────────────────────────── */}
        <nav className="shrink-0 flex border-b border-wroclaw-dark/10 px-2">
          {DRAWER_TABS
            .filter((tab) => {
              if (tab.adminOnly && !isAdmin) return false;
              if (tab.moderatorOnly && !isModerator) return false;
              if (tab.authenticatedOnly && !isAuthenticated) return false;
              return true;
            })
            .map((tab) => {
              const isActive = currentView === tab.id;
              return (
                <button
                  key={tab.id}
                  type="button"
                  onClick={() => setCurrentView(tab.id)}
                  className={[
                    'flex-1 px-2 py-2.5 text-xs font-medium transition-colors',
                    'border-b-2 -mb-px',
                    isActive
                      ? 'border-wroclaw-brick text-wroclaw-dark'
                      : 'border-transparent text-wroclaw-dark/50 hover:text-wroclaw-dark/80 hover:border-wroclaw-dark/20',
                  ].join(' ')}
                >
                  {tab.label}
                </button>
              );
            })}
        </nav>

        {/* ── View Content ───────────────────────────────────────────── */}

        {currentView === 'CATALOG' && (
          <>
            {selectedPoi ? (
              <PoiDetails poi={selectedPoi} onBack={() => setSelectedPoi(null)} />
            ) : (
              <>
                {/* ── Search bar — always visible at the top ─────────────── */}
                <FloatingSearch searchTerm={searchTerm} onSearchChange={setSearchTerm} />
                {/* ── Header ──────────────────────────────────────────── */}
                <header className="shrink-0 px-5 pt-4 pb-2">
                  <div className="flex items-baseline justify-between">
                    <h2 className="text-xl font-semibold text-wroclaw-dark">
                      Krasnale we Wrocławiu
                    </h2>
                    {isAuthenticated && (
                      <label className="flex items-center gap-2 text-xs font-medium text-wroclaw-dark cursor-pointer">
                        <input 
                          type="checkbox" 
                          checked={showVisitedOnly} 
                          onChange={(e) => setShowVisitedOnly(e.target.checked)}
                          className="rounded border-wroclaw-dark/20 text-wroclaw-brick focus:ring-wroclaw-brick"
                        />
                        👁️ Visited Only
                      </label>
                    )}
                  </div>
                  {/* ── Category filter chips (horizontal scroll) ────── */}
                  <div className="-mx-5 mt-3 flex gap-2 overflow-x-auto px-5 pb-2 scrollbar-none">
                    {POI_CATEGORIES.map((cat) => {
                      const isActive = activeCategory === cat.id;
                      return (
                        <button
                          key={cat.id}
                          type="button"
                          onClick={() => setActiveCategory(cat.id)}
                          className={[
                            'flex shrink-0 items-center gap-1.5 rounded-full px-3.5 py-1.5',
                            'text-xs font-medium transition-colors',
                            isActive
                              ? 'bg-wroclaw-brick text-white shadow-sm'
                              : 'bg-white/60 text-wroclaw-dark/70 hover:bg-white',
                          ].join(' ')}
                        >
                          <span>{cat.emoji}</span>
                          {cat.label}
                        </button>
                      );
                    })}
                  </div>
                </header>
                {/* ── Scrollable POI list ──────────────────────────────── */}
                <div className="flex-1 overflow-y-auto px-5 pb-6">
                  <PoiList 
                    activeCategory={activeCategory} 
                    onPoiSelect={setSelectedPoi} 
                    searchTerm={searchTerm} 
                    filteredPois={filteredPois}
                    isLoading={isLoading}
                    error={error}
                  />
                </div>
              </>
            )}
          </>
        )}

        {currentView === 'MY_SUBMISSIONS' && (
          draftPosition ? (
            <PoiForm draftPosition={draftPosition} onCancel={onCancelDraft} onSuccess={onCancelDraft} />
          ) : (
            <MySubmissionsView setMapFocusPoint={setMapFocusPoint} />
          )
        )}

        {currentView === 'MODERATOR_QUEUE' && (
          <VerificationQueue setMapFocusPoint={setMapFocusPoint} />
        )}

        {currentView === 'PROFILE' && (
          <UserProfile />
        )}

        {currentView === 'ADMIN' && (
          <AdminPanel />
        )}
      </aside>

      {/* ── Toggle button — absolutely positioned on the wrapper edge ── */}
      <button
        type="button"
        onClick={onToggle}
        aria-label={isOpen ? 'Hide sidebar' : 'Show sidebar'}
        className={[
          'hidden md:flex',
          'absolute top-1/2 -right-6 -translate-y-1/2',
          'h-12 w-6 items-center justify-center',
          'rounded-r-lg',
          'bg-wroclaw-sand/95 backdrop-blur-sm shadow-lg',
          'text-wroclaw-dark/60 hover:text-wroclaw-dark',
          'transition-colors',
        ].join(' ')}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className={`h-4 w-4 transition-transform duration-300 ${isOpen ? '' : 'rotate-180'}`}
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={2.5}
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M15 19l-7-7 7-7" />
        </svg>
      </button>
    </div>
  );
};

export default PoiDrawer;
