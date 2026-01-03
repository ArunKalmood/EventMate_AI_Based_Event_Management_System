// src/components/event-details/RecommendedSection.jsx
import React from "react";
import EventCard from "../EventCard";

export default function RecommendedSection({ events = [] }) {
  if (!events || events.length === 0) return null;

  return (
    <section className="mt-12">
      <h3 className="text-2xl font-bold text-gray-900 mb-4">You may also like</h3>
      <div className="flex gap-6 overflow-x-auto pb-3">
        {events.map((e) => (
          <div key={e.id} className="min-w-[260px]">
            <EventCard event={e} />
          </div>
        ))}
      </div>
    </section>
  );
}
