import PoiList from './PoiList';
import PoiDetails from './PoiDetails';
import type { Poi } from '../models/Poi';
import { POI_CATEGORIES } from '../constants/categories';

interface PoiDrawerProps {
  activeCategory: string;
  setActiveCategory: (cat: string) => void;
  selectedPoi: Poi | null;
  setSelectedPoi: (poi: Poi | null) => void;
}

/**
 * POI list drawer — displays the catalog of points of interest.
 *
 * Layout behavior:
 *  • Desktop (md+): side drawer sliding in from the left, ~30% viewport width.
 *  • Mobile (<md):  bottom sheet anchored to the lower portion of the screen.
 *
 * Visual shell only — real data and toggle logic will be added later.
 */
const PoiDrawer: React.FC<PoiDrawerProps> = ({
  activeCategory,
  setActiveCategory,
  selectedPoi,
  setSelectedPoi,
}) => {
  return (
    <aside
      id="poi-drawer"
      className={[
        // ── Shared styles ────────────────────────────────────────────────
        'absolute z-[900] flex flex-col',
        'bg-wroclaw-sand/95 backdrop-blur-sm shadow-2xl',

        // ── Mobile: bottom sheet ─────────────────────────────────────────
        'inset-x-0 bottom-0 h-[45vh]',
        'rounded-t-2xl',

        // ── Desktop: left side drawer ────────────────────────────────────
        'md:inset-y-0 md:left-0 md:bottom-auto',
        'md:h-full md:w-[30%] md:min-w-[320px] md:max-w-[420px]',
        'md:rounded-t-none md:rounded-r-2xl',
      ].join(' ')}
    >
      {/* Drag handle — visual affordance for the bottom sheet on mobile */}
      <div className="flex justify-center pt-3 pb-1 md:hidden">
        <span className="h-1 w-10 rounded-full bg-wroclaw-dark/30" />
      </div>

      {selectedPoi ? (
        <PoiDetails poi={selectedPoi} onBack={() => setSelectedPoi(null)} />
      ) : (
        <>
          {/* ── Header ──────────────────────────────────────────────────────── */}
          <header className="shrink-0 px-5 pt-4 pb-2">
            <div className="flex items-baseline justify-between">
              <h2 className="text-xl font-semibold text-wroclaw-dark">
                Krasnale we Wrocławiu
              </h2>
              <span className="text-xs font-medium text-wroclaw-dark/50">
                Explore POIs
              </span>
            </div>

            {/* ── Category filter chips (horizontal scroll) ───────────────── */}
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

          {/* ── Scrollable POI list ─────────────────────────────────────────── */}
          <div className="flex-1 overflow-y-auto px-5 pb-6">
            <PoiList activeCategory={activeCategory} onPoiSelect={setSelectedPoi} />
          </div>
        </>
      )}
    </aside>
  );
};

export default PoiDrawer;
