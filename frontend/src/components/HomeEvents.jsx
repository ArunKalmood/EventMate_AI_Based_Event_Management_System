import React, { useEffect, useState } from "react";
import EventCard from "./EventCard";
import EventCardSkeleton from "./Skeletons/EventCardSkeleton";
import { getAllEvents } from "../services/eventservice";
import EventsFilterTabs from "./EventsFilterTabs";
import { getCuratedEvents } from "../services/recommendationService";
import GlobalReviews from "./reviews/GlobalReviews";

function HomeEvents() {
  const [events, setEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [selectedFilter, setSelectedFilter] = useState("All");

  const [curatedEvents, setCuratedEvents] = useState([]);
  const [curatedLoading, setCuratedLoading] = useState(true);

  // -------------------------
  // Load all events
  // -------------------------
  useEffect(() => {
    getAllEvents().then((res) => {
      setEvents(res.data);
      setFilteredEvents(res.data.slice(0, 12));
    });
  }, []);

  // -------------------------
  // Load curated events
  // -------------------------
  useEffect(() => {
    let mounted = true;

    const loadCuratedEvents = async () => {
      try {
        const data = await getCuratedEvents();
        if (mounted) {
          setCuratedEvents(data || []);
        }
      } catch {
        // silent fail — UX fallback handles this
      } finally {
        if (mounted) setCuratedLoading(false);
      }
    };

    loadCuratedEvents();
    return () => (mounted = false);
  }, []);

  // -------------------------
  // Filters (unchanged)
  // -------------------------
  const applyFilter = (filter) => {
    setSelectedFilter(filter);
    const today = new Date();

    const filtered = events.filter((ev) => {
      const eventDate = new Date(ev.date);

      switch (filter) {
        case "Today":
          return eventDate.toDateString() === today.toDateString();
        case "Tomorrow": {
          const t = new Date();
          t.setDate(today.getDate() + 1);
          return eventDate.toDateString() === t.toDateString();
        }
        case "This Week": {
          const end = new Date();
          end.setDate(today.getDate() + (7 - today.getDay()));
          return eventDate >= today && eventDate <= end;
        }
        case "This Weekend":
          return eventDate.getDay() === 6 || eventDate.getDay() === 0;
        case "This Month":
          return (
            eventDate.getMonth() === today.getMonth() &&
            eventDate.getFullYear() === today.getFullYear()
          );
        default:
          return true;
      }
    });

    setFilteredEvents(filtered.slice(0, 12));
  };

  return (
    <section className="max-w-7xl mx-auto px-6 py-16">

      {/* =========================
          RECOMMENDED FOR YOU
      ========================= */}
      <div id="curated-events" className="mb-16">

        {curatedLoading && (
          <>
            <h2 className="text-3xl font-bold text-gray-900 mb-8">
              Recommended for You
            </h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
              {[...Array(4)].map((_, i) => (
                <EventCardSkeleton key={i} />
              ))}
            </div>
          </>
        )}

        {!curatedLoading && curatedEvents.length > 0 && (
          <>
            <h2 className="text-3xl font-bold text-gray-900 mb-8">
              Recommended for You
            </h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
              {curatedEvents.slice(0, 4).map((event) => (
                <EventCard key={event.id} event={event} />
              ))}
            </div>
          </>
        )}

        {!curatedLoading && curatedEvents.length === 0 && (
          <div className="bg-slate-50 border rounded-xl p-6 text-slate-600 text-sm">
            We’ll personalize recommendations as you explore events.
          </div>
        )}

      </div>

      {/* =========================
          GLOBAL REVIEWS (SOCIAL PROOF)
      ========================= */}
      <div className="mb-16">
        <GlobalReviews />
      </div>

      {/* =========================
          FILTERS + EVENTS
      ========================= */}
      <EventsFilterTabs selected={selectedFilter} onChange={applyFilter} />

      <h2 className="text-3xl font-bold text-gray-900 mb-8 mt-6">
        Upcoming Events
      </h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {filteredEvents.map((event) => (
          <EventCard key={event.id} event={event} />
        ))}
      </div>

      <div className="text-center mt-12">
        <a
          href="/events"
          className="text-brand-dark font-semibold hover:underline text-lg"
        >
          View All Events →
        </a>
      </div>
    </section>
  );
}

export default HomeEvents;
