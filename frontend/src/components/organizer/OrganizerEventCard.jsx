import { Link } from "react-router-dom";
import { closeEvent } from "../../services/organizerApi";

function OrganizerEventCard({ event }) {
  const {
    eventId,
    title,
    date,
    time,
    location,
    status,
    capacity,
    bookingCount,
  } = event;

  const handleCloseEvent = async () => {
    const confirmClose = window.confirm(
      "Are you sure you want to close this event? This action cannot be undone."
    );

    if (!confirmClose) return;

    try {
      await closeEvent(eventId);
      window.location.reload(); // safe + simple for now
    } catch (err) {
      console.error("Failed to close event", err);
      alert("Failed to close event");
    }
  };

  return (
    <div className="bg-white rounded-xl border shadow-sm p-6">
      <h3 className="text-lg font-semibold text-[#0B132B]">
        {title}
      </h3>

      <p className="mt-1 text-sm text-gray-600">
        {date} · {time}
      </p>

      <p className="text-sm text-gray-600">
        {location}
      </p>

      {/* STATUS */}
      <div className="mt-3 flex items-center gap-3 text-sm">
        <span
          className={`px-3 py-1 rounded-full text-xs font-medium ${
            status === "ACTIVE"
              ? "bg-green-100 text-green-700"
              : status === "FULL"
              ? "bg-orange-100 text-orange-700"
              : "bg-gray-200 text-gray-700"
          }`}
        >
          {status}
        </span>

        <span className="text-gray-500">
          {bookingCount} / {capacity ?? "∞"} booked
        </span>
      </div>

      {/* ACTIONS */}
      <div className="mt-5 flex gap-4">
        <Link
          to={`/organizer/bookings?eventId=${eventId}`}
          className="text-sm font-medium text-[#0B132B] hover:underline"
        >
          View Bookings
        </Link>

        {status === "ACTIVE" && (
          <button
            onClick={handleCloseEvent}
            className="text-sm font-medium text-red-600 hover:underline"
          >
            Close Event
          </button>
        )}
      </div>
    </div>
  );
}

export default OrganizerEventCard;
