// src/components/event-details/EventAbout.jsx
import React from "react";

function EventAbout({ description }) {
  return (
    <div className="rounded-xl border bg-white p-6">
      <h3 className="text-sm font-semibold text-gray-900 mb-2">
        About the event
      </h3>

      <p className="text-sm text-gray-700 leading-relaxed">
        {description || "No description provided."}
      </p>
    </div>
  );
}

export default EventAbout;
