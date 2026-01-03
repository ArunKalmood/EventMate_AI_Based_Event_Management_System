// src/components/BookingSuccessModal.jsx
import React from "react";

export default function BookingSuccessModal({
  open,
  onClose,
  bookingData,
  onGoToConfirmation,
}) {
  if (!open) return null;

  const { bookingId, event } = bookingData || {};

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      {/* Overlay */}
      <div
        className="absolute inset-0 bg-black/40 backdrop-blur-sm"
        onClick={onClose}
      />

      {/* Modal */}
      <div
        className="
          relative bg-white rounded-2xl shadow-xl max-w-lg w-full p-6 z-10
          animate-fadeIn
        "
      >
        {/* Header */}
        <div className="flex items-center gap-4">
          <div className="w-14 h-14 rounded-full bg-green-100 flex items-center justify-center text-3xl">
            🎉
          </div>
          <div>
            <h2 className="text-2xl font-bold text-gray-900">Booking Confirmed</h2>
            <p className="text-gray-600">{event?.title || "Your event"}</p>
          </div>
        </div>

        {/* Booking ID */}
        {bookingId && (
          <p className="mt-4 text-sm text-gray-700">
            Booking ID:{" "}
            <span className="font-mono bg-gray-100 px-2 py-1 rounded">
              {bookingId}
            </span>
          </p>
        )}

        {/* Buttons */}
        <div className="mt-6 flex gap-3">
          <button
            onClick={onGoToConfirmation}
            className="flex-1 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition"
          >
            View Booking
          </button>

          <button
            onClick={onClose}
            className="flex-1 py-2 bg-gray-100 rounded-lg hover:bg-gray-200 text-gray-800 transition"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
}
