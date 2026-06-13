/** Category metadata definition */
export interface PoiCategoryMeta {
  id: string;
  emoji: string;
  label: string;
}

export const CATEGORY_ALL_ID = 'ALL';

export const POI_CATEGORIES: PoiCategoryMeta[] = [
  { id: CATEGORY_ALL_ID, emoji: '📍', label: 'All' },
  { id: 'KRASNAL_FIGURINE', emoji: '🧙', label: 'Dwarfs' },
  { id: 'MONUMENT', emoji: '🏛️', label: 'Landmarks' },
  { id: 'BUILDING', emoji: '🏢', label: 'Buildings' },
  { id: 'FLORA', emoji: '🌳', label: 'Nature' },
  { id: 'PLACE', emoji: '📍', label: 'Places' },
];

export const getCategoryMeta = (categoryId: string): PoiCategoryMeta => {
  return (
    POI_CATEGORIES.find((c) => c.id === categoryId) || {
      id: categoryId,
      emoji: '📍',
      label: categoryId,
    }
  );
};
