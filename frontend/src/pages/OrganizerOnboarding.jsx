import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function OrganizerOnboarding() {
  const navigate = useNavigate();

  const [showConfirm, setShowConfirm] = useState(false);
  const [upgrading, setUpgrading] = useState(false);

  const handleUpgrade = async () => {
    try {
      setUpgrading(true);

      const res = await api.post("/auth/upgrade-to-organizer");

      // Replace old JWT with new organizer JWT
      localStorage.setItem("token", res.data.token);

      // Redirect directly to create event
      navigate("/organizer/create-event");
    } catch (err) {
      console.error("Upgrade failed", err);
      alert("Failed to upgrade to organizer");
    } finally {
      setUpgrading(false);
      setShowConfirm(false);
    }
  };

  return (
    <div className="max-w-3xl mx-auto bg-white border rounded-xl p-10 text-center">
      <h1 className="text-2xl font-semibold mb-4">
        Become an Organizer
      </h1>

      <p className="text-gray-600 mb-8">
        Host events, manage attendees, and create real-world experiences —
        using your existing account.
      </p>

      <div className="grid grid-cols-2 gap-4 text-left mb-8">
        <div>🎟 Create & manage events</div>
        <div>📊 Track bookings & capacity</div>
        <div>🔔 Real-time notifications</div>
        <div>📸 Memories & Lost & Found</div>
      </div>

      <button
        onClick={() => setShowConfirm(true)}
        className="bg-[#0B132B] text-white px-6 py-3 rounded-lg"
      >
        Become an Organizer
      </button>

      <p className="text-sm text-gray-400 mt-4">
        Same account • Same email • No logout required
      </p>

      {/* Confirmation Modal */}
      {showConfirm && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 w-full max-w-sm text-center">
            <h2 className="text-lg font-semibold mb-4">
              Are you sure?
            </h2>

            <p className="text-gray-600 mb-6">
              Do you really want to become an organizer?
            </p>

            <div className="flex gap-4 justify-center">
              <button
                onClick={() => setShowConfirm(false)}
                className="px-4 py-2 border rounded-lg"
              >
                No
              </button>

              <button
                onClick={handleUpgrade}
                disabled={upgrading}
                className="px-4 py-2 bg-[#0B132B] text-white rounded-lg disabled:opacity-60"
              >
                {upgrading ? "Upgrading..." : "Yes"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default OrganizerOnboarding;
