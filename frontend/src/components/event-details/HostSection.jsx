// src/components/event-details/HostSection.jsx
import React from "react";

export default function HostSection({ organizer }) {
  const name = organizer?.name || "Event Organizer";
  const contact = organizer?.email || "organizer@example.com";

  return (
    <section className="bg-white rounded-xl p-4 border shadow-sm">
      <h4 className="text-lg font-semibold text-gray-900 mb-3">Hosted By</h4>
      <div className="flex items-center gap-4">
        <div className="w-12 h-12 rounded-full bg-blue-100 flex items-center justify-center text-xl">👤</div>
        <div>
          <p className="font-semibold text-gray-900">{name}</p>
          <p className="text-sm text-gray-600">{contact}</p>
        </div>
      </div>
    </section>
  );
}
