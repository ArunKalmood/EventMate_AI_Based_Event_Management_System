// src/pages/BookingConfirmation.jsx
import { useLocation, useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import api from "../services/api";
import LoadingSpinner from "../components/LoadingSpinner";

export default function BookingConfirmation() {
  const { bookingId } = useParams();
  const navigate = useNavigate();
  const fromState = useLocation().state?.bookingData;

  const [booking, setBooking] = useState(fromState || null);
  const [loading, setLoading] = useState(!fromState && !!bookingId);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!booking && bookingId) {
      api
        .get(`/bookings/${bookingId}`)
        .then((res) => setBooking(res.data))
        .catch(() => setError("Unable to load booking details."))
        .finally(() => setLoading(false));
    }
  }, [bookingId, booking]);

  if (loading) return <LoadingSpinner />;

  if (error)
    return (
      <div className="max-w-3xl mx-auto p-10 text-center">
        <h2 className="text-xl font-bold text-red-600 mb-2">Error</h2>
        <p className="text-gray-600">{error}</p>
        <button
          onClick={() => navigate(-1)}
          className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md"
        >
          Go Back
        </button>
      </div>
    );

  if (!booking)
    return (
      <div className="max-w-3xl mx-auto p-10 text-center">
        <h2 className="text-lg font-semibold text-gray-800">No Booking Data</h2>
        <p className="text-gray-600">We couldn't find your booking.</p>
      </div>
    );

  const event = booking.event || {};
  const bookedAt = booking.createdAt || null;

  return (
    <div className="max-w-4xl mx-auto px-6 py-10">
      <div className="bg-white p-8 rounded-2xl shadow border">
        <div className="flex items-center gap-4">
          <div className="w-20 h-20 rounded-xl bg-blue-50 flex items-center justify-center text-4xl">
            🎟️
          </div>

          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              Booking Confirmed
            </h1>
            <p className="text-gray-600">Here are your ticket details.</p>
          </div>
        </div>

        {/* Ticket Info */}
        <div className="mt-6 grid md:grid-cols-2 gap-6">
          <div>
            <p className="text-sm font-medium text-gray-500">Booking ID</p>
            <p className="font-mono bg-gray-100 px-3 py-2 rounded mt-1 inline-block">
              {booking.id || bookingId}
            </p>

            <p className="text-sm font-medium text-gray-500 mt-4">
              Event Details
            </p>
            <p className="mt-1 text-gray-800 font-semibold">{event.title}</p>
            <p className="text-gray-600 text-sm">{event.location}</p>

            {bookedAt && (
              <p className="text-sm text-gray-500 mt-2">
                Booked on {new Date(bookedAt).toLocaleString()}
              </p>
            )}
          </div>

          {/* Download ticket */}
          <div className="mt-4">
            <button
              onClick={() => {
                const txt = `Booking ID: ${
                  booking.id || bookingId
                }\nEvent: ${event.title}\nLocation: ${
                  event.location
                }\nDate: ${event.date}`;
                const blob = new Blob([txt], { type: "text/plain" });
                const url = URL.createObjectURL(blob);
                const a = document.createElement("a");
                a.href = url;
                a.download = "ticket.txt";
                a.click();
                URL.revokeObjectURL(url);
              }}
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
            >
              Download Ticket
            </button>
          </div>
        </div>

        <button
          onClick={() => navigate("/")}
          className="mt-8 px-4 py-2 bg-gray-100 rounded-md"
        >
          Go Home
        </button>
      </div>
    </div>
  );
}
