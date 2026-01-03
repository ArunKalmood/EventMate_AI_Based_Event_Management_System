import React, { useEffect, useState, useMemo } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../services/api";
import EventCard from "../components/EventCard";
import EventsFilterTabs from "../components/EventsFilterTabs";
import EventCardSkeleton from "../components/Skeletons/EventCardSkeleton";

function useQuery() {
  const { search } = useLocation();
  return useMemo(() => new URLSearchParams(search), [search]);
}

function EventsList() {
  const [events, setEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [selectedFilter, setSelectedFilter] = useState("All");
  const [loading, setLoading] = useState(true);

  const location = useLocation();
  const queryParams = useQuery();
  const navigate = useNavigate();

  // URL params
  const urlQuery =
    queryParams.get("search") ||
    queryParams.get("query") ||
    queryParams.get("q") ||
    "";

  const urlCity = queryParams.get("city") || queryParams.get("location") || "";

  const urlCategory = queryParams.get("category") || "";

  // ==========================
  // FETCH EVENTS (BACKEND SEARCH)
  // ==========================
  useEffect(() => {
    setLoading(true);

    api
      .get("/events/search", {
        params: {
          q: urlQuery || "",
          category: urlCategory || "",
          page: 0,
          size: 50,
        },
      })
      .then((res) => {
        // backend returns Page<>
        const normalizedEvents = (res.data?.content || []).map((ev) => ({
          ...ev,
          id: ev.id ?? ev.eventId, // ✅ SAFE normalization
        }));

        setEvents(normalizedEvents);
      })
      .catch((err) => {
        console.error("Failed to fetch events:", err);
        setEvents([]);
      })
      .finally(() => setLoading(false));
  }, [urlQuery, urlCategory]);

  // ==========================
  // FRONTEND DATE FILTERS ONLY
  // ==========================
  useEffect(() => {
    if (!events) return;

    const today = new Date();

    const result = events.filter((ev) => {
      if (!selectedFilter || selectedFilter === "All") return true;

      const eventDate = ev.date ? new Date(ev.date) : null;
      if (!eventDate || isNaN(eventDate.getTime())) return false;

      switch (selectedFilter) {
        case "Today":
          return eventDate.toDateString() === today.toDateString();

        case "Tomorrow": {
          const tomorrow = new Date(today);
          tomorrow.setDate(today.getDate() + 1);
          return eventDate.toDateString() === tomorrow.toDateString();
        }

        case "This Week": {
          const weekEnd = new Date(today);
          weekEnd.setDate(today.getDate() + (7 - today.getDay()));
          return eventDate >= today && eventDate <= weekEnd;
        }

        case "This Weekend":
          return eventDate.getDay() === 6 || eventDate.getDay() === 0;

        case "Next Week": {
          const start = new Date(today);
          start.setDate(today.getDate() + (7 - today.getDay()) + 1);
          const end = new Date(start);
          end.setDate(start.getDate() + 6);
          return eventDate >= start && eventDate <= end;
        }

        case "Next Weekend": {
          const nextSat = new Date(today);
          nextSat.setDate(today.getDate() + (6 - today.getDay() + 7));
          const nextSun = new Date(nextSat);
          nextSun.setDate(nextSat.getDate() + 1);
          return (
            eventDate.toDateString() === nextSat.toDateString() ||
            eventDate.toDateString() === nextSun.toDateString()
          );
        }

        case "This Month":
          return (
            eventDate.getMonth() === today.getMonth() &&
            eventDate.getFullYear() === today.getFullYear()
          );

        default:
          return true;
      }
    });

    setFilteredEvents(result);
  }, [events, selectedFilter]);

  // ==========================
  // DATE FILTER URL SYNC
  // ==========================
  const applyFilter = (filter) => {
    setSelectedFilter(filter);

    const params = new URLSearchParams(location.search);
    if (filter && filter !== "All") params.set("filter", filter);
    else params.delete("filter");

    navigate(
      { pathname: "/events", search: params.toString() },
      { replace: false }
    );
  };

  useEffect(() => {
    const f = queryParams.get("filter");
    if (f) setSelectedFilter(f);
  }, [location.search]);

  return (
    <section className="max-w-7xl mx-auto px-6 py-10">
      <EventsFilterTabs selected={selectedFilter} onChange={applyFilter} />

      <h2 className="text-3xl font-bold text-gray-900 mb-8 mt-6">
        Popular Events
        {urlCategory && ` • ${urlCategory}`}
        {urlCity ? ` in ${urlCity}` : " in Hyderabad"}
      </h2>

      {/* Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 sm:gap-8">
        {loading &&
          Array.from({ length: 8 }).map((_, i) => (
            <EventCardSkeleton key={i} />
          ))}

        {!loading &&
          filteredEvents.map((event) => (
            <EventCard key={event.id} event={event} />
          ))}

        {!loading && filteredEvents.length === 0 && (
          <div className="col-span-full py-20 text-center text-gray-600">
            <p className="text-2xl font-semibold mb-2">No events found</p>
            <p>Try changing the category, date, or search term.</p>
          </div>
        )}
      </div>
    </section>
  );
}

export default EventsList;
