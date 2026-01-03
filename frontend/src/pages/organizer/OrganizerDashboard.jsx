import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getOrganizerEvents } from "../../services/organizerApi";
import OrganizerEventCard from "../../components/organizer/OrganizerEventCard";

function OrganizerDashboard() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadEvents();
  }, []);

  const loadEvents = async () => {
    try {
      const data = await getOrganizerEvents();
      setEvents(data.content || []);
    } catch (err) {
      console.error("Failed to load organizer events", err);
    } finally {
      setLoading(false);
    }
  };

  // ===== OPTION B: STATS (SAFE, DERIVED) =====
  const totalEvents = events.length;
  const activeEvents = events.filter(
    (e) => e.status === "ACTIVE"
  ).length;
  const closedEvents = events.filter(
    (e) => e.status === "CLOSED"
  ).length;

  return (
    <div>
      {/* HEADER */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-[#0B132B]">
            Organizer Dashboard
          </h1>
          <p className="mt-1 text-gray-600">
            Manage events you created
          </p>
        </div>

        {/* STEP A: LOST & FOUND LINK */}
        <Link
          to="/organizer/lost-found"
          className="text-sm font-medium text-[#0B132B] hover:underline"
        >
          Lost & Found
        </Link>
      </div>

      {/* ===== OPTION B: STATS STRIP ===== */}
      {!loading && (
        <div className="mt-6 grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div className="bg-white border rounded-xl p-4">
            <p className="text-sm text-gray-500">Total Events</p>
            <p className="text-2xl font-semibold text-[#0B132B]">
              {totalEvents}
            </p>
          </div>

          <div className="bg-white border rounded-xl p-4">
            <p className="text-sm text-gray-500">Active Events</p>
            <p className="text-2xl font-semibold text-green-600">
              {activeEvents}
            </p>
          </div>

          <div className="bg-white border rounded-xl p-4">
            <p className="text-sm text-gray-500">Closed Events</p>
            <p className="text-2xl font-semibold text-gray-700">
              {closedEvents}
            </p>
          </div>
        </div>
      )}

      {/* CONTENT */}
      <div className="mt-8">
        {loading && (
          <p className="text-gray-500">Loading events...</p>
        )}

        {!loading && events.length === 0 && (
          <p className="text-gray-500">
            You haven’t created any events yet.
          </p>
        )}

        {!loading && events.length > 0 && (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {events.map((event) => (
              <OrganizerEventCard
                key={event.eventId}
                event={event}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default OrganizerDashboard;
