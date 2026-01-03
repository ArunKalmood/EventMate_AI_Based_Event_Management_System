import React from "react";
import { useNavigate } from "react-router-dom";

const fallbackImage = "/placeholder-event.jpg";

function EventCard({ event }) {
  const navigate = useNavigate();
  const dateObj = new Date(event.date);

  const formatDateTime = () => {
    const datePart = dateObj.toLocaleDateString("en-US", {
      weekday: "short",
      day: "2-digit",
      month: "short",
    });

    if (!event.time) return datePart;

    const [h, m] = event.time.split(":");
    const hour = parseInt(h);
    const suffix = hour >= 12 ? "PM" : "AM";
    const timePart = `${hour % 12 || 12}:${m} ${suffix}`;

    return `${datePart} · ${timePart}`;
  };

  const getAvailabilityText = () => {
    if (event.status === "FULL" || event.availableSeats === 0) {
      return "Sold out";
    }

    if (event.demandState === "FILLING_FAST") {
      return "Filling fast";
    }

    if (event.demandState === "TRENDING") {
      return "Trending";
    }

    return "Seats available";
  };

  return (
    <div
      onClick={() => navigate(`/events/${event.id}`)}
      className="
        group cursor-pointer
        transition-all duration-300 ease-out
        hover:-translate-y-1 hover:scale-[1.01]
      "
    >
      {/* IMAGE */}
      <div className="relative w-full aspect-[16/9] rounded-2xl overflow-hidden shadow-md">
        <img
          src={event.bannerImageUrl || fallbackImage}
          alt={event.title}
          className="
            h-full w-full object-cover
            transition-transform duration-700 ease-out
            group-hover:scale-110
          "
        />

        {/* =========================
            ⭐ RATING + BADGE (ADDED)
        ========================= */}
        {event.reviewCount > 0 && (
          <div className="absolute top-3 left-3 flex items-center gap-2">
            <span className="bg-white/90 text-xs font-semibold px-2 py-1 rounded-full shadow">
              ⭐ {Number(event.avgRating).toFixed(1)}
            </span>

            {event.isQualified && (
              <span className="bg-black text-white text-xs px-2 py-1 rounded-full">
                Top Rated
              </span>
            )}
          </div>
        )}
      </div>

      {/* TEXT CONTENT */}
      <div className="mt-3 px-1">
        <p className="text-xs text-slate-500">{formatDateTime()}</p>

        <h3 className="mt-1 text-sm font-semibold text-slate-900 leading-snug line-clamp-2">
          {event.title}
        </h3>

        <p className="mt-1 text-sm text-slate-500 truncate">
          {event.location}
        </p>

        <p className="mt-2 text-xs text-slate-600">
          {getAvailabilityText()}
        </p>

        {event.recommendationReason && (
          <p className="mt-2 text-xs text-indigo-600 italic leading-snug">
            {event.recommendationReason}
          </p>
        )}
      </div>
    </div>
  );
}

export default EventCard;
