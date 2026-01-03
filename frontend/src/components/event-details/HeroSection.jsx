// src/components/event-details/HeroSection.jsx
import React from "react";
import EventDateBadge from "../EventDateBadge";
import EventStatusBadge from "../EventStatusBadge";

export default function HeroSection({ event }) {
  if (!event) return null;

  const imageUrl =
    event.bannerImageUrl || "https://placehold.co/1200x600?text=Event+Image";

  const dateOnly = event.date?.split("T")[0];
  const timeOnly = event.time || event.date?.split("T")[1]?.slice(0, 5);

  return (
    <div className="relative w-full rounded-2xl overflow-hidden shadow-sm">
      {/* Poster */}
      <img
        src={imageUrl}
        alt={event.title}
        className="
          w-full 
          h-56 sm:h-64 md:h-72 lg:h-80
          object-cover
          transition-all
        "
      />

      {/* Dark overlay for readability */}
      <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-black/40 to-transparent"></div>

      {/* Badges */}
      <div className="absolute top-3 sm:top-4 left-3 sm:left-4 flex items-center gap-2 z-10">
        <EventDateBadge date={event.date} />
        <EventStatusBadge
          capacity={event.capacity}
          bookedCount={event.bookedCount}
        />
      </div>

      {/* Title + Info bar */}
      <div
        className="
          absolute bottom-4 left-1/2 transform -translate-x-1/2 
          w-[92%] sm:w-[85%] md:w-[75%] 
          bg-white/70 backdrop-blur-xl 
          px-4 sm:px-6 py-3 sm:py-4 
          rounded-2xl shadow-lg
          text-center
          z-10
        "
      >
        <h1 className="text-xl sm:text-2xl md:text-3xl font-bold text-gray-900 leading-tight">
          {event.title}
        </h1>

        {/* Responsive text layout */}
        <p
          className="
            text-gray-800 mt-1 
            text-xs sm:text-sm md:text-base 
            leading-relaxed break-words
          "
        >
          📅 {dateOnly} • ⏰ {timeOnly} • 📍 {event.location} • 🏷{" "}
          {event.category}
        </p>
      </div>
    </div>
  );
}
