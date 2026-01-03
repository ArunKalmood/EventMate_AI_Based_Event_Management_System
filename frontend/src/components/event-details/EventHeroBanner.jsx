// src/components/event-details/EventHeroBanner.jsx
import React from "react";

const FALLBACK_IMAGE = "/placeholder-event.jpg";

function formatDateTime(date, time) {
  if (!date) return "";

  const dateObj = new Date(date);
  const datePart = dateObj.toLocaleDateString("en-US", {
    weekday: "short",
    day: "2-digit",
    month: "short",
    year: "numeric",
  });

  if (!time) return datePart;

  const [h, m] = time.split(":");
  const hour = parseInt(h, 10);
  const suffix = hour >= 12 ? "PM" : "AM";
  const timePart = `${hour % 12 || 12}:${m} ${suffix}`;

  return `${datePart} · ${timePart}`;
}

function EventHeroBanner({ event }) {
  const { title, date, time, location, category, bannerImageUrl } = event;

  return (
    <section className="max-w-7xl mx-auto px-6">
      {/* IMAGE */}
      <div className="w-full overflow-hidden rounded-2xl border bg-gray-100">
        <img
          src={bannerImageUrl || FALLBACK_IMAGE}
          alt={title}
          className="w-full h-[300px] md:h-[360px] object-cover"
        />
      </div>

      {/* CONTENT */}
      <div className="mt-6 space-y-3">
        {category && (
          <span className="inline-block px-3 py-1 text-xs font-semibold rounded-full bg-blue-50 text-blue-700">
            {category}
          </span>
        )}

        <h1 className="text-3xl md:text-4xl font-bold text-gray-900">
          {title}
        </h1>

        <p className="text-sm md:text-base text-gray-600">
          {formatDateTime(date, time)}
          {location && ` • ${location}`}
        </p>
      </div>
    </section>
  );
}

export default EventHeroBanner;
