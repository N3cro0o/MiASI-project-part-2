import React from 'react';
import { usePendingSubmissions, useAcceptSubmission, useRejectSubmission } from '../api/useModeration';

const safelyFormatDate = (dateString?: string | null) => {
  if (!dateString) return 'Unknown date';
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return 'Invalid date';
  return new Intl.DateTimeFormat('pl-PL', { dateStyle: 'medium', timeStyle: 'short' }).format(date);
};

interface VerificationQueueProps {
  setMapFocusPoint?: (pos: { lat: number; lng: number }) => void;
}

const VerificationQueue: React.FC<VerificationQueueProps> = ({ setMapFocusPoint }) => {
  const { data: submissions, isLoading } = usePendingSubmissions();
  const { mutate: accept, isPending: isAccepting } = useAcceptSubmission();
  const { mutate: reject, isPending: isRejecting } = useRejectSubmission();

  if (isLoading) {
    return (
      <div className="flex h-full items-center justify-center p-5 text-wroclaw-dark/50">
        <span className="animate-pulse">Loading queue...</span>
      </div>
    );
  }

  if (!submissions || submissions.length === 0) {
    return (
      <div className="flex h-full flex-col items-center justify-center p-5 text-center">
        <div className="text-4xl mb-3">🎉</div>
        <h3 className="text-lg font-semibold text-wroclaw-dark">Queue is empty. Great job!</h3>
        <p className="mt-1 text-sm text-wroclaw-dark/60">No pending submissions to review.</p>
      </div>
    );
  }

  const handleAccept = (id: string | number) => {
    accept(id);
  };

  const handleReject = (id: string | number) => {
    const reason = window.prompt("Enter rejection reason:");
    if (reason !== null && reason.trim() !== '') {
      reject({ subId: id, reason: reason.trim() });
    }
  };

  return (
    <div className="flex h-full flex-col">
      <header className="shrink-0 px-5 pt-4 pb-4 border-b border-wroclaw-dark/10">
        <h2 className="text-xl font-semibold text-wroclaw-dark">Verification Queue</h2>
        <p className="mt-1 text-xs font-medium text-wroclaw-dark/60">
          Review pending dwarf submissions
        </p>
      </header>

      <div className="flex-1 overflow-y-auto px-5 py-6">
        <div className="flex flex-col gap-4">
          {submissions.map((sub) => (
            <div
              key={sub.id}
              className="rounded-xl border border-wroclaw-dark/10 bg-white p-4 shadow-sm"
            >
              <div className="flex items-start justify-between">
                <div>
                  <h4 className="font-bold text-wroclaw-dark">
                    {sub.krasnalName}
                  </h4>
                  <p className="mt-0.5 text-xs text-wroclaw-dark/50">
                    Submitted: {safelyFormatDate(sub.time)}
                  </p>
                </div>
              </div>
              <div className="flex gap-2 mt-3">
                <button
                  type="button"
                  disabled={isAccepting || isRejecting}
                  onClick={() => handleAccept(sub.id)}
                  className="flex-1 rounded-lg bg-green-600 px-3 py-1.5 text-sm font-medium text-white transition-colors hover:bg-green-700 disabled:opacity-50"
                >
                  Accept
                </button>
                <button
                  type="button"
                  disabled={isAccepting || isRejecting}
                  onClick={() => handleReject(sub.id)}
                  className="flex-1 rounded-lg bg-red-600 px-3 py-1.5 text-sm font-medium text-white transition-colors hover:bg-red-700 disabled:opacity-50"
                >
                  Reject
                </button>
              </div>
              
              {sub.krasnalPos && (
                <button
                  type="button"
                  onClick={() => setMapFocusPoint && setMapFocusPoint({ lat: sub.krasnalPos.latitude, lng: sub.krasnalPos.longitude })}
                  className="mt-3 flex items-center text-xs font-medium text-wroclaw-brick hover:underline focus:outline-none"
                >
                  📍 Locate on Map
                </button>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default VerificationQueue;
