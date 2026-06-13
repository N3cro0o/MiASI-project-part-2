import React from 'react';

type SubmissionStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED';

interface SubmissionDummy {
  id: string;
  name: string;
  date: string;
  status: SubmissionStatus;
  rejectionReason?: string;
}

const DUMMY_SUBMISSIONS: SubmissionDummy[] = [
  {
    id: '1',
    name: 'Papa Krasnal',
    date: '2026-06-12',
    status: 'PENDING',
  },
  {
    id: '2',
    name: 'Życzliwek',
    date: '2026-06-10',
    status: 'ACCEPTED',
  },
  {
    id: '3',
    name: 'Fake Dwarf',
    date: '2026-06-08',
    status: 'REJECTED',
    rejectionReason: 'Not a real dwarf. Please submit actual dwarf locations.',
  },
];

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
        <div className="flex flex-col gap-4">
          {DUMMY_SUBMISSIONS.map((sub) => (
            <div
              key={sub.id}
              className="rounded-xl border border-wroclaw-dark/10 bg-white p-4 shadow-sm"
            >
              <div className="flex items-start justify-between">
                <div>
                  <h3 className="text-sm font-semibold text-wroclaw-dark">
                    {sub.name}
                  </h3>
                  <p className="mt-0.5 text-xs text-wroclaw-dark/50">
                    {sub.date}
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
      </div>
    </div>
  );
};

export default MySubmissionsView;
