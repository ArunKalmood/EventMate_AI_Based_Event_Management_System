// src/components/event-details/EventDateLocation.jsx
import React from "react";

function EventDateLocation({ event }) {
  const { date, time, location } = event;

  return (
    <div className="rounded-xl border bg-white p-5 space-y-3">
      <h3 className="text-sm font-semibold text-gray-900">
        Date & Location
      </h3>

      <div className="text-sm text-gray-700 space-y-1">
        <p>📅 {date}{time && ` · ${time}`}</p>
        <p>📍 {location}</p>
      </div>
    </div>
  );
}

export default EventDateLocation;
