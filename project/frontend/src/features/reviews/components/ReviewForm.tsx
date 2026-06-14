import React, { useState } from 'react';
import { useCreateReview } from '../api/useReviews';

interface ReviewFormProps {
  krasnalId: number;
}

const ReviewForm: React.FC<ReviewFormProps> = ({ krasnalId }) => {
  const [rating, setRating] = useState<number>(0);
  const [comment, setComment] = useState('');
  const [error, setError] = useState('');
  const { mutate: createReview, isPending } = useCreateReview();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (rating === 0) {
      setError('Please select a rating.');
      return;
    }

    if (comment.trim() === '') {
      setError('Please enter a comment.');
      return;
    }

    createReview(
      { krasnalId, rating, comment },
      {
        onSuccess: () => {
          setRating(0);
          setComment('');
        },
        onError: () => {
          setError('Failed to submit review. Please try again later.');
        },
      }
    );
  };

  return (
    <div className="mt-6 rounded-xl bg-white/80 p-4 shadow-sm border border-wroclaw-dark/10">
      <h3 className="mb-3 text-sm font-semibold text-wroclaw-dark">Leave a Review</h3>
      <form onSubmit={handleSubmit} className="flex flex-col gap-3">
        {/* Star Rating */}
        <div className="flex gap-1">
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              key={star}
              type="button"
              onClick={() => setRating(star)}
              className="focus:outline-none"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className={`h-6 w-6 ${star <= rating ? 'text-amber-500' : 'text-gray-300'} hover:text-amber-400 transition-colors`}
                fill="currentColor"
                viewBox="0 0 24 24"
              >
                <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" />
              </svg>
            </button>
          ))}
        </div>

        {/* Comment Textarea */}
        <textarea
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="Share your thoughts about this dwarf..."
          className="w-full rounded-lg border border-gray-200 bg-white px-3 py-2 text-sm text-gray-700 outline-none placeholder:text-gray-400 focus:border-wroclaw-brick focus:ring-1 focus:ring-wroclaw-brick resize-none"
          rows={3}
        />

        {error && <p className="text-xs text-red-600">{error}</p>}

        <button
          type="submit"
          disabled={isPending}
          className="self-end rounded-lg bg-wroclaw-brick px-4 py-1.5 text-sm font-medium text-white shadow-sm transition-colors hover:bg-wroclaw-brick/90 disabled:opacity-50"
        >
          {isPending ? 'Submitting...' : 'Submit Review'}
        </button>
      </form>
    </div>
  );
};

export default ReviewForm;
