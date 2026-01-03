// src/components/event-details/SeatsInfo.jsx
import React from "react";
import CapacityBar from "../CapacityBar";

export default function SeatsInfo({
  capacity = 0,
  bookedCount = 0,
  onBook,
  onBookAndView,
  booking,
}) {
  const seatsLeft = capacity - bookedCount;

  return (
    <div className="bg-white rounded-xl p-5 border shadow-sm relative">
      <h4 className="text-lg font-semibold text-gray-900 mb-2">Tickets</h4>

      <p className="text-sm text-gray-600 mb-3">
        Seats left:{" "}
        <span className="font-medium text-gray-800">
          {seatsLeft >= 0 ? seatsLeft : 0}
        </span>
      </p>

      <div className="mb-4">
        <CapacityBar booked={bookedCount} capacity={capacity || 1} />
      </div>

      {/* Buttons */}
      <div className="flex flex-col gap-3">
        <button
          onClick={onBook}
          disabled={booking}
          className="
            w-full py-3 
            bg-blue-600 hover:bg-blue-700 
            text-white rounded-lg font-medium 
            transition disabled:opacity-60
          "
        >
          {booking ? "Booking…" : "Book Now"}
        </button>

        <button
          onClick={onBookAndView}
          disabled={booking}
          className="
            w-full py-3 
            bg-blue-50 text-blue-700 
            rounded-lg border font-medium
            disabled:opacity-60
          "
        >
          {booking ? "Processing…" : "Book & View Confirmation"}
        </button>
      </div>

      {/* Sticky CTA — only on mobile */}
      <div
        className="
          md:hidden fixed bottom-0 left-0 w-full 
          bg-white shadow-2xl px-6 py-4 
          flex items-center justify-between
          z-50
        "
      >
        <span className="text-gray-800 font-semibold text-base">
          {seatsLeft > 0 ? `${seatsLeft} seats left` : "Sold Out"}
        </span>

        <button
          onClick={onBook}
          disabled={booking}
          className="
            px-5 py-2 
            bg-blue-600 text-white rounded-lg font-medium
            disabled:opacity-60
          "
        >
          {booking ? "…" : "Book Now"}
        </button>
      </div>
    </div>
  );
}
