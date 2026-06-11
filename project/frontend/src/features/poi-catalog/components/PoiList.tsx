import PoiCard from './PoiCard';
import type { PoiCardProps } from './PoiCard';

/** Dummy data representing well-known Wrocław dwarfs */
const DUMMY_POIS: PoiCardProps[] = [
  {
    name: 'Życzliwek',
    description: 'The very first dwarf of Wrocław, spreading good wishes since 2001.',
    rating: 4.9,
    reviewCount: 312,
  },
  {
    name: 'Papa Krasnal',
    description: 'The wise elder of the dwarf community, found near the Town Hall.',
    rating: 4.8,
    reviewCount: 245,
  },
  {
    name: 'Spioch',
    description: 'A sleepy dwarf resting on a tiny bed beside Świdnicka Street.',
    rating: 4.7,
    reviewCount: 189,
  },
  {
    name: 'W-Skers',
    description: 'Two dwarfs riding a tandem bicycle near the Oder River promenade.',
    rating: 4.6,
    reviewCount: 156,
  },
];

/**
 * Scrollable vertical list of POI cards.
 * Receives items as props in the future; uses dummy data for now.
 */
const PoiList: React.FC = () => {
  return (
    <div className="flex flex-col gap-2">
      {DUMMY_POIS.map((poi) => (
        <PoiCard key={poi.name} {...poi} />
      ))}
    </div>
  );
};

export default PoiList;
