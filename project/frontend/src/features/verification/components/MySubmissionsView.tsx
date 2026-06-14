import React, { useState } from 'react';
import { useMySubmissions, type SubmissionStatus, type SubmissionReturn } from '../api/useMySubmissions';

const safelyFormatDate = (dateString?: string | null) => {
  if (!dateString) return 'Unknown date';
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return 'Invalid date';
  return new Intl.DateTimeFormat('pl-PL', { dateStyle: 'medium', timeStyle: 'short' }).format(date);
};

const getStatusBadge = (status: SubmissionStatus) => {
  switch (status) {
    case 'PENDING':
      return (
        <span className="inline-flex items-center rounded-full bg-amber-100 px-2.5 py-0.5 text-xs font-medium text-amber-800">
          Pending
        </span>
      );
    case 'ACCEPTED':
      return (
        <span className="inline-flex items-center rounded-full bg-green-100 px-2.5 py-0.5 text-xs font-medium text-green-800">
          Accepted
        </span>
      );
    case 'REJECTED':
      return (
        <span className="inline-flex items-center rounded-full bg-red-100 px-2.5 py-0.5 text-xs font-medium text-red-800">
          Rejected
        </span>
      );
    default:
      return null;
  }
};

interface MySubmissionsViewProps {
  setMapFocusPoint?: (pos: { lat: number; lng: number }) => void;
}

const MySubmissionsView: React.FC<MySubmissionsViewProps> = ({ setMapFocusPoint }) => {
  const { data, isLoading, isError } = useMySubmissions();
  const [expandedId, setExpandedId] = useState<number | null>(null);

  // Safely ensure submissions is an array and sort descending by time
  const submissions = Array.isArray(data) 
    ? [...data].sort((a, b) => new Date(b.time).getTime() - new Date(a.time).getTime())
    : [];

  return (
    <div className="flex h-full flex-col">
      {/* ── Header ──────────────────────────────────────────────────────── */}
      <header className="shrink-0 px-5 pt-4 pb-4">
        <h2 className="text-xl font-semibold text-wroclaw-dark">
          My Submissions
        </h2>
        <p className="mt-1 text-xs font-medium text-wroclaw-dark/60">
          Track the status of your reported locations
        </p>
      </header>

      {/* ── Scrollable List ─────────────────────────────────────────────── */}
      <div className="flex-1 overflow-y-auto px-5 pb-6">
        {isLoading && (
          <div className="p-5 text-center text-wroclaw-dark/50">
            Loading submissions...
          </div>
        )}

        {isError && (
          <div className="p-5 text-center text-red-600">
            Failed to load submissions.
          </div>
        )}

        {!isLoading && !isError && submissions.length === 0 && (
          <div className="p-5 text-center text-wroclaw-dark/50">
            You haven't reported any locations yet.
          </div>
        )}

        {!isLoading && !isError && submissions.length > 0 && (
          <div className="flex flex-col gap-4">
            {submissions.map((sub: SubmissionReturn) => {
              const isExpanded = expandedId === sub.id;
              return (
                <div
                  key={sub.id}
                  onClick={() => {
                    setExpandedId(isExpanded ? null : sub.id);
                    if (!isExpanded && sub.krasnalPos && setMapFocusPoint) {
                      setMapFocusPoint({ lat: sub.krasnalPos.latitude, lng: sub.krasnalPos.longitude });
                    }
                  }}
                  className={`rounded-xl border bg-white p-4 shadow-sm cursor-pointer transition-all ${
                    isExpanded 
                      ? 'border-wroclaw-brick ring-1 ring-wroclaw-brick' 
                      : 'border-wroclaw-dark/10 hover:border-wroclaw-dark/30'
                  }`}
                >
                  <div className="flex items-start justify-between">
                    <div>
                      <h4 className="font-bold text-wroclaw-dark">
                        {sub.krasnalName}
                      </h4>
                      <p className="mt-0.5 text-xs text-wroclaw-dark/50">
                        {safelyFormatDate(sub.time)}
                      </p>
                    </div>
                    {getStatusBadge(sub.status)}
                  </div>

                  {isExpanded && (
                    <div className="mt-4 pt-4 border-t border-wroclaw-dark/10">
                      <p className="text-sm text-wroclaw-dark/80">
                        <strong>Submission ID:</strong> {sub.id}
                      </p>
                      {sub.krasnalPos && (
                        <p className="text-sm text-wroclaw-dark/80">
                          <strong>Location:</strong> {sub.krasnalPos.latitude.toFixed(4)}, {sub.krasnalPos.longitude.toFixed(4)}
                        </p>
                      )}
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
};

export default MySubmissionsView;
