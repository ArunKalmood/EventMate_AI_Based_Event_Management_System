import { useEffect, useState } from "react";
import { getOrganizerLostFoundOverview } from "../../services/organizerApi";
import LostFoundCard from "../../components/organizer/LostFoundCard";

function OrganizerLostFound() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadOverview();
  }, []);

  const loadOverview = async () => {
    try {
      const data = await getOrganizerLostFoundOverview();
      setItems(data.content || []);
    } catch (err) {
      console.error("Failed to load lost & found overview", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-semibold text-[#0B132B]">
        Lost & Found
      </h1>
      <p className="mt-1 text-gray-600">
        Overview of lost items reported for your events
      </p>

      <div className="mt-8">
        {loading && (
          <p className="text-gray-500">Loading lost items...</p>
        )}

        {!loading && items.length === 0 && (
          <p className="text-gray-500">
            No lost items reported yet.
          </p>
        )}

        {!loading && items.length > 0 && (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {items.map((item, index) => (
              <LostFoundCard key={index} item={item} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default OrganizerLostFound;
