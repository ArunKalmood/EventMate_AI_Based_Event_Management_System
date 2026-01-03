import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { getOrganizerEventBookings } from "../../services/organizerApi";

function OrganizerBookings() {
  const [searchParams] = useSearchParams();
  const eventId = searchParams.get("eventId");

  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (eventId) {
      loadBookings();
    }
  }, [eventId]);

  const loadBookings = async () => {
    try {
      const data = await getOrganizerEventBookings(eventId);
      setBookings(data);
    } catch (err) {
      console.error("Failed to load bookings", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-semibold text-[#0B132B]">
        Event Bookings
      </h1>
      <p className="mt-1 text-gray-600">
        Users who booked this event
      </p>

      <div className="mt-8">
        {loading && (
          <p className="text-gray-500">Loading bookings...</p>
        )}

        {!loading && bookings.length === 0 && (
          <p className="text-gray-500">
            No bookings for this event yet.
          </p>
        )}

        {!loading && bookings.length > 0 && (
          <div className="overflow-x-auto bg-white rounded-xl border">
            <table className="w-full text-sm">
              <thead className="bg-gray-50 text-gray-600">
                <tr>
                  <th className="px-6 py-4 text-left">Name</th>
                  <th className="px-6 py-4 text-left">Email</th>
                  <th className="px-6 py-4 text-left">Booked At</th>
                  <th className="px-6 py-4 text-left">Status</th>
                </tr>
              </thead>

              <tbody>
                {bookings.map((b, index) => (
                  <tr
                    key={index}
                    className="border-t hover:bg-gray-50"
                  >
                    <td className="px-6 py-4">{b.userName}</td>
                    <td className="px-6 py-4">{b.userEmail}</td>
                    <td className="px-6 py-4">
                      {new Date(b.bookedAt).toLocaleString()}
                    </td>
                    <td className="px-6 py-4">
                      <span
                        className={`px-3 py-1 rounded-full text-xs font-medium ${
                          b.status === "CONFIRMED"
                            ? "bg-green-100 text-green-700"
                            : "bg-gray-200 text-gray-700"
                        }`}
                      >
                        {b.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default OrganizerBookings;
