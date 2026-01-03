import { useState } from "react";
import { useNavigate } from "react-router-dom";
import TicketModal from "./TicketModal";

export default function BookingCard({ booking, event, onCancel, readonly }) {
  const navigate = useNavigate();
  const [showTicket, setShowTicket] = useState(false);

  const {
    eventId,
    title,
    date,
    time,
    location,
    bookedAt,
    status,
    ticketId,
    ticketCode,
    qrPayload,
  } = booking;

  const imageUrl =
    event?.bannerImageUrl ||
    "https://images.unsplash.com/photo-1507874457470-272b3c8d8ee2";

  const statusColor = {
    CONFIRMED: "bg-green-100 text-green-700 border-green-300",
    CANCELLED: "bg-red-100 text-red-700 border-red-300",
  };

  return (
    <>
      <div className="bg-white rounded-2xl border shadow-sm p-4 flex flex-col gap-4">
        <div className="relative w-full h-44 rounded-xl overflow-hidden">
          <img src={imageUrl} className="w-full h-full object-cover" />
          <span
            className={`absolute top-3 right-3 px-3 py-1 text-xs font-semibold rounded-full border ${statusColor[status]}`}
          >
            {status}
          </span>
        </div>

        <h3 className="text-xl font-bold">{title}</h3>

        <div className="text-sm text-gray-700 space-y-1">
          <p>📅 {date}</p>
          <p>⏰ {time || "—"}</p>
          <p>📍 {location}</p>
        </div>

        <p className="text-xs text-gray-500">
          Booked on {new Date(bookedAt).toLocaleString()}
        </p>

        <div className="flex gap-3">
          <button
            onClick={() => navigate(`/events/${eventId}`)}
            className="flex-1 py-2 rounded-lg border bg-blue-50 text-blue-700"
          >
            View Event
          </button>

          {!readonly && (
            <button
              onClick={() => setShowTicket(true)}
              className="flex-1 py-2 rounded-lg bg-blue-600 text-white"
            >
              Ticket
            </button>
          )}
        </div>

        {!readonly && (
          <button
            onClick={() => onCancel(booking.bookingId)}
            className="text-sm text-red-600 hover:underline self-start"
          >
            Cancel Booking
          </button>
        )}
      </div>

      {showTicket && (
        <TicketModal
          title={title}
          date={date}
          time={time}
          location={location}
          ticketCode={ticketCode}
          qrPayload={qrPayload}
          onClose={() => setShowTicket(false)}
        />
      )}
    </>
  );
}
