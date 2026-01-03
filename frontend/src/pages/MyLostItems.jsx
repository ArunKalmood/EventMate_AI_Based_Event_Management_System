import { useEffect, useState } from "react";
import api from "../services/api";

function MyLostItems() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadMyLostItems();
  }, []);

  const loadMyLostItems = async () => {
    try {
      const res = await api.get("/users/me/lost-items");
      setItems(res.data || []);
    } catch (err) {
      console.error("Failed to load my lost items", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto px-6 mt-24 py-10">
      <h1 className="text-2xl font-semibold text-[#0B132B]">
        My Lost Items
      </h1>

      <p className="mt-1 text-gray-600">
        Items you reported across events
      </p>

      <div className="mt-8">
        {loading && (
          <p className="text-gray-500">
            Loading your lost items...
          </p>
        )}

        {!loading && items.length === 0 && (
          <p className="text-gray-500">
            You haven’t reported any lost items yet.
          </p>
        )}

        {!loading && items.length > 0 && (
          <div className="bg-white border rounded-xl divide-y">
            {items.map((item) => (
              <div
                key={item.id}
                className="px-6 py-4 flex items-center justify-between text-sm"
              >
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
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default MyLostItems;
