import React from 'react';
import { useMySubmissions, type SubmissionStatus, type EnrichedSubmission } from '../api/useMySubmissions';

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

const MySubmissionsView: React.FC = () => {
  const { data, isLoading, isError } = useMySubmissions();

  // Safely ensure submissions is an array to avoid map() errors
  const submissions = Array.isArray(data) ? data : [];

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
            {submissions.map((sub: EnrichedSubmission) => (
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
                      {new Intl.DateTimeFormat('en-US', {
                        dateStyle: 'medium',
                        timeStyle: 'short',
                      }).format(new Date(sub.submittedTime))}
                    </p>
                  </div>
                  {getStatusBadge(sub.status)}
                </div>

                {sub.status === 'REJECTED' && sub.rejectionReason && (
                  <div className="mt-3 rounded-lg bg-red-50 p-3 text-xs text-red-800">
                    <span className="font-semibold">Reason:</span>{' '}
                    {sub.rejectionReason}
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default MySubmissionsView;
