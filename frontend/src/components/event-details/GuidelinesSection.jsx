// src/components/event-details/GuidelinesSection.jsx
import React from "react";

export default function GuidelinesSection() {
  return (
    <section className="bg-white rounded-xl p-4 border shadow-sm">
      <h3 className="text-xl font-semibold text-gray-900 mb-3">Event Guidelines</h3>
      <ul className="text-gray-700 list-disc pl-6 space-y-2">
        <li>Please arrive 15 minutes early.</li>
        <li>Carry a valid ID for verification.</li>
        <li>Outside food & drinks may not be allowed.</li>
        <li>Follow event staff instructions for safety.</li>
      </ul>
    </section>
  );
}
