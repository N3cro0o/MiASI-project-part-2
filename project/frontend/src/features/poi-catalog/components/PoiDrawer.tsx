import PoiList from './PoiList';
import PoiDetails from './PoiDetails';
import PoiForm from '../../verification/components/PoiForm';
import FloatingSearch from '../../../shared/components/FloatingSearch';
import type { Poi } from '../models/Poi';
import { POI_CATEGORIES } from '../constants/categories';

interface PoiDrawerProps {
  activeCategory: string;
  setActiveCategory: (cat: string) => void;
  selectedPoi: Poi | null;
  setSelectedPoi: (poi: Poi | null) => void;
  draftPosition: { lat: number; lng: number } | null;
  onCancelDraft: () => void;
  isOpen: boolean;
  onToggle: () => void;
}

/**
 * POI list drawer — displays the catalog of points of interest.
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
  isOpen,
  onToggle,
}) => {
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
        {/* ── Search bar — always visible at the top ───────────────────── */}
        <FloatingSearch />
        {draftPosition ? (
          <PoiForm draftPosition={draftPosition} onCancel={onCancelDraft} onSuccess={onCancelDraft} />
        ) : selectedPoi ? (
          <PoiDetails poi={selectedPoi} onBack={() => setSelectedPoi(null)} />
        ) : (
          <>
            {/* ── Header ────────────────────────────────────────────────── */}
            <header className="shrink-0 px-5 pt-4 pb-2">
              <div className="flex items-baseline justify-between">
                <h2 className="text-xl font-semibold text-wroclaw-dark">
                  Krasnale we Wrocławiu
                </h2>
                <span className="text-xs font-medium text-wroclaw-dark/50">
                  Explore POIs
                </span>
              </div>
              {/* ── Category filter chips (horizontal scroll) ─────────── */}
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
            {/* ── Scrollable POI list ───────────────────────────────────── */}
            <div className="flex-1 overflow-y-auto px-5 pb-6">
              <PoiList activeCategory={activeCategory} onPoiSelect={setSelectedPoi} />
            </div>
          </>
        )}
      </aside>
      {/* ── Toggle button — absolutely positioned on the wrapper edge ── */}      <button
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
