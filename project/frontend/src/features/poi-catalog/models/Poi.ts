// /**
//  * Represents a Point of Interest (POI) fetched from the backend.
//  */
// export interface Poi {
//   id: string | number;
//   name: string;
//   description: string;
//   latitude: number;
//   longitude: number;
//   rating?: number;
//   imageUrl?: string;
//   category?: string;
//   reviewCount?: number;
// }

/**
 * Represents a Point of Interest (POI) fetched from the backend.
 */
export interface Poi {
  id: string | number;
  name: string;
  description: string;
  position: {
    latitude: number;
    longitude: number;
  };
  category: string;
  status: string;
  // Opcjonalne pola, jeśli chcesz ich używać:
  rating?: number;
  averageRating?: number;
  imageUrl?: string;
}