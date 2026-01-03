import React, { useEffect, useState } from "react";
import EventCard from "../components/EventCard";
import { getCuratedEvents } from "../services/recommendationService";

function CuratedEvents() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const data = await getCuratedEvents();
        setEvents(data);
      } catch (e) {
        console.error("Failed to load curated events", e);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-6 py-32 text-center text-gray-500">
        Loading curated events…
      </div>
    );
  }

  if (events.length === 0) {
    return (
      <div className="max-w-7xl mx-auto px-6 py-32 text-center text-gray-500">
        No recommendations yet. Explore some events first 🙂
      </div>
    );
  }

  return (
    <section className="max-w-7xl mx-auto px-6 py-24">
      <h1 className="text-3xl font-bold text-gray-900 mb-10">
        Recommended for You
      </h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {events.map((event) => (
          <EventCard key={event.id} event={event} />
        ))}
      </div>
    </section>
  );
}

export default CuratedEvents;
