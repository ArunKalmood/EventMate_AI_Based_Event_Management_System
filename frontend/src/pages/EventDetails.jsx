// src/pages/EventDetails.jsx
import { useEffect, useState, useCallback, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";

import EventDetailsNavbar from "../components/event-details/EventDetailsNavbar";
import EventHeroBanner from "../components/event-details/EventHeroBanner";
import BookingSidebar from "../components/event-details/BookingSidebar";
import EventActions from "../components/event-details/EventActions";
import EventDateLocation from "../components/event-details/EventDateLocation";
import EventAbout from "../components/event-details/EventAbout";
import EventReviews from "../components/event-details/EventReviews";
import { loadPayPal } from "../utils/loadPayPal";

function EventDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [bookingLoading, setBookingLoading] = useState(false);
  const [error, setError] = useState(null);

  const [quantity, setQuantity] = useState(1);
  const [bookingId, setBookingId] = useState(null);
  const paypalRef = useRef(null);
  const paypalRendered = useRef(false);

  const fetchEvent = useCallback(async () => {
    try {
      setLoading(true);
      const res = await api.get(`/events/${id}`);
      setEvent(res.data);
    } catch (err) {
      setError("Failed to load event");
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    fetchEvent();
  }, [fetchEvent]);

  const handleBookNow = async (selectedQuantity) => {
    const token = localStorage.getItem("token");

    if (!token) {
      navigate("/login");
      return;
    }

    if (bookingLoading) return;

    try {
      setBookingLoading(true);

      const res = await api.post(
        `/events/${id}/book?quantity=${selectedQuantity}`
      );

      const bookingIdFromResponse = Number(res.data.split("Booking ID: ")[1]);
      setQuantity(selectedQuantity);

      // ✅ FREE EVENT
      if (event.price === 0) {
        setBookingLoading(false);
        navigate("/my-bookings");
        return;
      }

      setBookingId(bookingIdFromResponse);
    } catch (err) {
      alert(err?.response?.data || "Booking failed");
      setBookingLoading(false);
    }
  };

  useEffect(() => {
    if (!paypalRef.current || !bookingId || event.price === 0) return;
    if (paypalRendered.current) return;

    paypalRendered.current = true;
    paypalRef.current.innerHTML = "";

    loadPayPal(
      "ASebR-VZJ3pstl2mnaExN9LG39Ylba6itFk_bjf37pn3Z8jxjy0L4urJgNY3U1J4TXT6lufcF3Gym83R"
    )
      .then((paypal) => {
        paypal
          .Buttons({
            createOrder: async () => {
              const res = await api.post(
                `/payments/create-order?bookingId=${bookingId}`
              );
              return res.data.paypalOrderId;
            },
            onApprove: async (data, actions) => {
              const capture = await actions.order.capture();
              await api.post("/payments/verify", {
                paypalOrderId: data.orderID,
                paypalCaptureId: capture.id,
              });

              paypalRendered.current = false;
              setBookingId(null);
              setBookingLoading(false);
              await fetchEvent();
              navigate("/my-bookings");
            },
            onCancel: () => {
              paypalRendered.current = false;
              setBookingLoading(false);
            },
            onError: () => {
              paypalRendered.current = false;
              setBookingLoading(false);
            },
          })
          .render(paypalRef.current);
      })
      .catch(() => {
        paypalRendered.current = false;
        setBookingLoading(false);
      });
  }, [bookingId]);

  const handleReportLost = () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/login");
      return;
    }
    navigate(`/events/${id}/report-lost`);
  };

  if (loading) return null;

  if (error || !event) {
    return (
      <div className="pt-32 text-center">
        <h2 className="text-2xl font-semibold">Event not found</h2>
      </div>
    );
  }

  return (
    <>
      <EventDetailsNavbar />

      <main className="pt-20">
        <EventHeroBanner event={event} />

        {/* =========================
            ⭐ RATING / QUALITY STRIP (ADDED)
        ========================= */}
        {event.reviewCount > 0 && (
          <div className="max-w-7xl mx-auto px-6 mt-4">
            <div className="flex items-center gap-4 text-sm">
              <span className="font-medium text-gray-900">
                ⭐ {Number(event.avgRating).toFixed(1)}
              </span>

              <span className="text-gray-500">
                ({event.reviewCount} reviews)
              </span>

              {event.isQualified && (
                <span className="px-2 py-0.5 rounded-full bg-black text-white text-xs">
                  Top Rated
                </span>
              )}
            </div>
          </div>
        )}

        <section className="max-w-7xl mx-auto px-6 py-12">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
            <div className="lg:col-span-2 space-y-8">
              <EventActions eventId={id} event={event} />

              <button
                onClick={handleReportLost}
                className="text-sm font-medium text-[#0B132B] hover:underline"
              >
                Report Lost Item
              </button>

              <EventDateLocation event={event} />
              <EventAbout description={event.description} />
              <EventReviews eventId={id} />
            </div>

            <aside className="relative">
              <div className="sticky top-24 space-y-4">
                
                {/* ✅ PRICE DISPLAY (ADDED) */}
                <div className="border rounded-lg p-4 bg-gray-50">
                  {event.price === 0 ? (
                    <p className="text-lg font-semibold text-green-600">
                      🎉 Free Event
                    </p>
                  ) : (
                    <p className="text-lg font-semibold">
                      Ticket Price: ₹{event.price}
                    </p>
                  )}
                </div>

                <BookingSidebar
                  event={event}
                  bookingDisabled={bookingLoading}
                  onBook={handleBookNow}
                />

                {bookingLoading && event.price > 0 && (
                  <div ref={paypalRef} />
                )}
              </div>
            </aside>
          </div>
        </section>
      </main>
    </>
  );
}

export default EventDetails;
