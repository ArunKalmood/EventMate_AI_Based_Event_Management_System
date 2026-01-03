import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import api from "../../services/api";

function OrganizerLostFoundEvent() {
  const { eventId } = useParams();

  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadItems();
  }, [eventId]);

  const loadItems = async () => {
    try {
      // 🔒 Safe even if API not ready yet
      const res = await api.get(
        `/organizer/lost-found?eventId=${eventId}`
      );
      setItems(res.data || []);
    } catch (err) {
      console.error("Failed to load lost items", err);
      setItems([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* HEADER */}
      <h1 className="text-2xl font-semibold text-[#0B132B]">
        Lost & Found – Event #{eventId}
      </h1>
      <p className="mt-1 text-gray-600">
        Manage reported lost items for this event
      </p>

      {/* CONTENT */}
      <div className="mt-8">
        {loading && (
          <p className="text-gray-500">Loading items...</p>
        )}

        {!loading && items.length === 0 && (
          <p className="text-gray-500">
            No lost items reported for this event.
          </p>
        )}

        {!loading && items.length > 0 && (
          <div className="bg-white border rounded-xl overflow-hidden">
            <table className="w-full text-sm">
              <thead className="bg-gray-50">
                <tr className="text-left">
                  <th className="px-4 py-3">Item</th>
                  <th className="px-4 py-3">Reported By</th>
                  <th className="px-4 py-3">Status</th>
                  <th className="px-4 py-3">Action</th>
                </tr>
              </thead>

              <tbody>
                {items.map((item) => (
                  <tr
                    key={item.id}
                    className="border-t"
                  >
                    <td className="px-4 py-3">
                      {item.title}
                    </td>

                    <td className="px-4 py-3 text-gray-600">
                      {item.reportedByEmail}
                    </td>

                    <td className="px-4 py-3">
                      <span
                        className={`px-2 py-1 rounded-full text-xs font-medium ${
                          item.status === "RESOLVED"
                            ? "bg-green-100 text-green-700"
                            : "bg-orange-100 text-orange-700"
                        }`}
                      >
                        {item.status}
                      </span>
                    </td>

                    <td className="px-4 py-3">
                      {item.status === "PENDING" ? (
                        <button className="text-[#0B132B] hover:underline">
                          Mark Resolved
                        </button>
                      ) : (
                        <span className="text-gray-400">
                          —
                        </span>
                      )}
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

export default OrganizerLostFoundEvent;
