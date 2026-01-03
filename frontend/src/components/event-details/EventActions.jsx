import React, { useState } from "react";

function EventActions({ eventId, event }) {
  const interestedKey = `interested_${eventId}`;

  const [interested, setInterested] = useState(
    localStorage.getItem(interestedKey) === "true"
  );

  const toggleInterested = () => {
    const next = !interested;
    setInterested(next);
    localStorage.setItem(interestedKey, next);
  };

  const handleShare = () => {
    if (navigator.share) {
      navigator.share({
        title: event.title,
        url: window.location.href,
      });
    } else {
      navigator.clipboard.writeText(window.location.href);
      alert("Link copied to clipboard");
    }
  };

  const handleCalendar = () => {
    alert("Calendar integration coming soon 📅");
  };

  return (
    <div className="flex flex-wrap gap-3">
      <button
        onClick={toggleInterested}
        className="flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-lg border bg-white hover:bg-gray-50"
      >
        {interested ? "⭐ Interested" : "☆ Interested"}
      </button>

      <button
        onClick={handleShare}
        className="flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-lg border bg-white hover:bg-gray-50"
      >
        🔗 Share
      </button>

      <button
        onClick={handleCalendar}
        className="flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-lg border bg-white hover:bg-gray-50"
      >
        📅 Add to Calendar
      </button>
    </div>
  );
}

export default EventActions;
