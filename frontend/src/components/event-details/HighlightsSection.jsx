// src/components/event-details/HighlightsSection.jsx
import React from "react";

export default function HighlightsSection({ event }) {
  return (
    <section className="bg-white rounded-xl p-4 border shadow-sm">
      <h3 className="text-xl font-semibold text-gray-900 mb-3">Event Highlights</h3>
      <ul className="grid md:grid-cols-2 gap-3 text-gray-700">
        <li>✨ Professional & well-organized</li>
        <li>🎤 Expert hosts</li>
        <li>📸 Photo opportunities</li>
        <li>👨‍👩‍👧 Family-friendly</li>
      </ul>
    </section>
  );
}
