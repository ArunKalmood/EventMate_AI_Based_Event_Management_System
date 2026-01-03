import api from "../../services/api";

function LostItemRow({ item, onStatusUpdated }) {
  const { id, title, description, status } = item;

  const updateStatus = async (newStatus) => {
    try {
      await api.put(`/organizer/lost-items/${id}/status`, {
        status: newStatus,
      });
      onStatusUpdated();
    } catch (err) {
      console.error("Failed to update lost item status", err);
      alert("Failed to update status");
    }
  };

  return (
    <div className="flex items-center justify-between px-6 py-4 text-sm">
      <div>
        <p className="font-medium text-[#0B132B]">{title}</p>
        <p className="text-gray-600">{description}</p>
      </div>

      <div className="flex items-center gap-3">
        <span className="px-3 py-1 rounded-full bg-gray-100 text-gray-700 text-xs">
          {status}
        </span>

        {status !== "RETURNED" && (
          <select
            value={status}
            onChange={(e) => updateStatus(e.target.value)}
            className="border rounded-md px-2 py-1 text-xs"
          >
            <option value="REPORTED">Reported</option>
            <option value="FOUND">Found</option>
            <option value="RETURNED">Returned</option>
          </select>
        )}
      </div>
    </div>
  );
}

export default LostItemRow;
