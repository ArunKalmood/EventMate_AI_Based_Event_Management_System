// src/pages/MyBookings.jsx
import { useEffect, useState } from "react";
import api from "../services/api";
import LoadingSpinner from "../components/LoadingSpinner";
import BookingCard from "../components/BookingCard";
import EventDetailsNavbar from "../components/event-details/EventDetailsNavbar";

export default function MyBookings() {
  const [bookings, setBookings] = useState([]);
  const [eventMap, setEventMap] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      try {
        // 1️⃣ Fetch bookings
        const bookingRes = await api.get("/bookings/me");
        const bookingData = bookingRes.data || [];
        setBookings(bookingData);

        // 2️⃣ Fetch related events
        const eventIds = [...new Set(bookingData.map(b => b.eventId))];

        const eventRequests = eventIds.map(id =>
          api.get(`/events/${id}`).then(res => [id, res.data])
        );

        const eventEntries = await Promise.all(eventRequests);

        const map = {};
        eventEntries.forEach(([id, event]) => {
          map[id] = event;
        });

        setEventMap(map);
      } catch (err) {
        console.error("[MY BOOKINGS ERROR]", err);
        setError("Unable to fetch your bookings.");
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  // ✅ FIXED: use bookingId everywhere
  const handleCancel = async (bookingId) => {
    if (!window.confirm("Cancel this booking?")) return;

    try {
      await api.post(`/bookings/${bookingId}/cancel`);

      // 🔥 instant UI update (no refresh needed)
      setBookings(prev =>
        prev.map(b =>
          b.bookingId === bookingId
            ? { ...b, status: "CANCELLED" }
            : b
        )
      );
    } catch (err) {
      console.error("[CANCEL ERROR]", err);
      alert(err?.response?.data || "Failed to cancel booking");
    }
  };

  if (loading) return <LoadingSpinner />;

  if (error)
    return (
      <>
        <EventDetailsNavbar />
        <div className="pt-32 text-center text-red-600">{error}</div>
      </>
    );

  const upcoming = bookings.filter(b => b.status === "CONFIRMED");
  const past = bookings.filter(b => b.status !== "CONFIRMED");

  return (
    <>
      <EventDetailsNavbar />

      <div className="pt-32 max-w-6xl mx-auto px-6 space-y-12">
        <h1 className="text-3xl font-bold">My Bookings</h1>

        {/* UPCOMING */}
        <section>
          <h2 className="text-xl font-semibold mb-4">Upcoming</h2>
          {upcoming.length === 0 ? (
            <p className="text-gray-500">No upcoming bookings.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {upcoming.map(b => (
                <BookingCard
                  key={b.bookingId}
                  booking={b}
                  event={eventMap[b.eventId]}
                  onCancel={handleCancel}
                />
              ))}
            </div>
          )}
        </section>

        {/* PAST / CANCELLED */}
        <section>
          <h2 className="text-xl font-semibold mb-4">Past / Cancelled</h2>
          {past.length === 0 ? (
            <p className="text-gray-500">Nothing here yet.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 opacity-80">
              {past.map(b => (
                <BookingCard
                  key={b.bookingId}
                  booking={b}
                  event={eventMap[b.eventId]}
                  readonly
                />
              ))}
            </div>
          )}
        </section>
      </div>
    </>
  );
}
