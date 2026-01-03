import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import api from "../../services/api";

function OrganizerEventLostItems() {
  const { eventId } = useParams();

  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadLostItems();
  }, [eventId]);

  const loadLostItems = async () => {
    try {
      // Organizer-safe endpoint (backend will be aligned in Step 3B)
      const res = await api.get(
        `/organizer/lost-items/event/${eventId}`
      );
      setItems(res.data || []);
    } catch (err) {
      console.error("Failed to load lost items", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* HEADER */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-[#0B132B]">
            Lost Items
          </h1>
          <p className="mt-1 text-gray-600">
            Items reported for this event
          </p>
        </div>

        <Link
          to="/organizer/lost-found"
          className="text-sm font-medium text-[#0B132B] hover:underline"
        >
          ← Back
        </Link>
      </div>

      {/* CONTENT */}
      <div className="mt-8">
        {loading && (
          <p className="text-gray-500">
            Loading lost items...
          </p>
        )}

        {!loading && items.length === 0 && (
          <p className="text-gray-500">
            No lost items reported for this event.
          </p>
        )}

        {!loading && items.length > 0 && (
          <div className="bg-white border rounded-xl divide-y">
            {items.map((item) => (
              <div
                key={item.id}
                className="px-6 py-4 text-sm"
              >
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium text-[#0B132B]">
                      {item.title}
                    </p>
                    <p className="text-gray-600">
                      {item.description}
                    </p>
                  </div>

                  <span
                    className={`px-3 py-1 rounded-full text-xs font-medium ${
                      item.status === "PENDING"
                        ? "bg-orange-100 text-orange-700"
                        : item.status === "FOUND"
                        ? "bg-blue-100 text-blue-700"
                        : "bg-green-100 text-green-700"
                    }`}
                  >
                    {item.status}
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default OrganizerEventLostItems;
