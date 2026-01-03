import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../services/api";

function EventLostItems() {
  const { eventId } = useParams();

  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadLostItems();
  }, [eventId]);

  const loadLostItems = async () => {
    try {
      // ✅ Organizer-safe endpoint
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

  // ✅ Status update (uses existing backend logic)
  const updateStatus = async (lostItemId, newStatus) => {
    try {
      await api.put(
        `/organizer/lost-items/${lostItemId}/status`,
        { status: newStatus }
      );
      loadLostItems(); // refresh after update
    } catch (err) {
      console.error("Failed to update status", err);
      alert("Failed to update status");
    }
  };

  // ✅ Valid next status (mirrors backend rules)
  const getNextStatus = (current) => {
    if (current === "PENDING") return "FOUND";
    if (current === "FOUND") return "RETURNED";
    return null;
  };

  return (
    <div>
      {/* HEADER */}
      <h1 className="text-2xl font-semibold text-[#0B132B]">
        Lost Items
      </h1>
      <p className="mt-1 text-gray-600">
        Items reported for this event
      </p>

      {/* CONTENT */}
      <div className="mt-8">
        {loading && (
          <p className="text-gray-500">Loading lost items...</p>
        )}

        {!loading && items.length === 0 && (
          <p className="text-gray-500">
            No lost items reported for this event.
          </p>
        )}

        {!loading && items.length > 0 && (
          <div className="bg-white border rounded-xl divide-y">
            {items.map((item) => {
              const nextStatus = getNextStatus(item.status);

              return (
                <div
                  key={item.id}
                  className="px-6 py-4 text-sm flex justify-between items-start"
                >
                  <div>
                    <p className="font-medium text-[#0B132B]">
                      {item.title}
                    </p>

                    <p className="text-gray-600">
                      {item.description}
                    </p>

                    <span className="inline-block mt-2 px-3 py-1 rounded-full text-xs bg-gray-100 text-gray-700">
                      {item.status}
                    </span>
                  </div>

                  {nextStatus && (
                    <button
                      onClick={() =>
                        updateStatus(item.id, nextStatus)
                      }
                      className="text-sm font-medium text-[#0B132B] hover:underline"
                    >
                      Mark as {nextStatus}
                    </button>
                  )}
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}

export default EventLostItems;
