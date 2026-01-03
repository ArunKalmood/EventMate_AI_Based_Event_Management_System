// src/components/event-details/DescriptionSection.jsx
import React from "react";

export default function DescriptionSection({ description }) {
  return (
    <section className="bg-white rounded-xl p-6 border shadow-sm">
      <h3 className="text-xl font-semibold text-gray-900 mb-3">About This Event</h3>
      <p className="text-gray-700 leading-relaxed">{description || "No description provided."}</p>
    </section>
  );
}
