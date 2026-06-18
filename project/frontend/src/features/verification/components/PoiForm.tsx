import { useState } from 'react';
import { POI_CATEGORIES } from '../../poi-catalog/constants/categories';
// Changed import to reflect the Verification Context
import { useCreateSubmission } from '../../verification/api/useCreateSubmission';

interface PoiFormProps {
  draftPosition: { lat: number; lng: number };
  onCancel: () => void;
  onSuccess?: () => void;
}

/**
 * Visual shell for the POI submission form.
 * Rendered when a user drops a pin in adding mode.
 * Dispatches a Submission (PENDING state) rather than directly creating a POI.
 */
const PoiForm: React.FC<PoiFormProps> = ({ draftPosition, onCancel, onSuccess }) => {
  const [name, setName] = useState('');
  const [category, setCategory] = useState('');
  const [description, setDescription] = useState('');

  // Replaced useCreatePoi with useCreateSubmission
  const { mutate: createSubmission, isPending, error } = useCreateSubmission();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name || !category || !description) return;

    createSubmission(
      {
        name,
        category,
        description,
        // Flattened coordinates to match tactical.md SubmissionPayload VO
        latitude: draftPosition.lat,
        longitude: draftPosition.lng,
      },
      {
        onSuccess: () => {
          // trigger the success Toast notification here
          // e.g., toast.success('Submission sent! Waiting for verification.');

          if (onSuccess) onSuccess();
        },
      }
    );
  };

  return (
    <div className="flex h-full flex-col">
      {/* ── Header ──────────────────────────────────────────────────────── */}
      <header className="shrink-0 px-5 pt-4 pb-2">
        <h2 className="text-xl font-semibold text-wroclaw-dark">
          Add New Location
        </h2>
        <span className="text-xs font-medium text-wroclaw-dark/50">
          Fill in the details for the selected pin.
        </span>
      </header>

      {/* ── Form Content (Scrollable) ─────────────────────────────────── */}
      <div className="flex-1 overflow-y-auto px-5 pb-6">
        <form onSubmit={handleSubmit} className="mt-4 flex flex-col gap-4">
          {/* Coordinates display */}
          <div className="rounded-lg bg-wroclaw-dark/5 p-3 text-sm text-wroclaw-dark/70">
            📍 <span className="font-mono">{draftPosition.lat.toFixed(6)}, {draftPosition.lng.toFixed(6)}</span>
          </div>

          {/* Error Message */}
          {error && (
            <div className="rounded-lg bg-red-50 p-3 text-sm text-red-600">
              Failed to submit: {error.message}
            </div>
          )}

          {/* Name Input */}
          <div className="flex flex-col gap-1">
            <label htmlFor="poi-name" className="text-sm font-semibold text-wroclaw-dark">
              Name
            </label>
            <input
              id="poi-name"
              type="text"
              required
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g., Papa Krasnal"
              className="rounded-lg border border-wroclaw-dark/10 bg-white px-3 py-2 text-sm outline-none transition-colors focus:border-wroclaw-brick focus:ring-1 focus:ring-wroclaw-brick"
              disabled={isPending}
            />
          </div>

          {/* Category Select */}
          <div className="flex flex-col gap-1">
            <label htmlFor="poi-category" className="text-sm font-semibold text-wroclaw-dark">
              Category
            </label>
            <select
              id="poi-category"
              required
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              className="rounded-lg border border-wroclaw-dark/10 bg-white px-3 py-2 text-sm outline-none transition-colors focus:border-wroclaw-brick focus:ring-1 focus:ring-wroclaw-brick"
              disabled={isPending}
            >
              <option value="" disabled>Select a category</option>
              {POI_CATEGORIES.filter(c => c.id !== 'ALL').map((cat) => (
                <option key={cat.id} value={cat.id}>
                  {cat.emoji} {cat.label}
                </option>
              ))}
            </select>
          </div>

          {/* Description Textarea */}
          <div className="flex flex-col gap-1">
            <label htmlFor="poi-description" className="text-sm font-semibold text-wroclaw-dark">
              Description
            </label>
            <textarea
              id="poi-description"
              required
              rows={4}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Describe this location..."
              className="resize-none rounded-lg border border-wroclaw-dark/10 bg-white px-3 py-2 text-sm outline-none transition-colors focus:border-wroclaw-brick focus:ring-1 focus:ring-wroclaw-brick"
              disabled={isPending}
            />
          </div>

          {/* Action Buttons */}
          <div className="mt-4 flex gap-3">
            <button
              type="button"
              onClick={onCancel}
              disabled={isPending}
              className="flex-1 rounded-lg border border-wroclaw-dark/10 py-2.5 text-sm font-semibold text-wroclaw-dark transition-colors hover:bg-wroclaw-dark/5 disabled:opacity-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isPending}
              className="flex-1 rounded-lg bg-wroclaw-brick py-2.5 text-sm font-semibold text-white shadow-md transition-colors hover:bg-wroclaw-brick/90 disabled:opacity-50"
            >
              {isPending ? 'Submitting...' : 'Submit'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PoiForm;
