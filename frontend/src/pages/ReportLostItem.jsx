import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";
import EventDetailsNavbar from "../components/event-details/EventDetailsNavbar";

function ReportLostItem() {
  const { id: eventId } = useParams();
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!title.trim()) return;

    try {
      setLoading(true);
      setError(null);

      await api.post(`/events/${eventId}/lost`, {
        title,
        description,
      });

      // ✅ SUCCESS FEEDBACK
      alert("Lost item reported successfully ✅");

      // ✅ INTENTIONAL NAVIGATION
      navigate(`/events/${eventId}`);
    } catch (err) {
      console.error("Failed to report lost item", err);
      setError("Failed to submit report. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {/* ✅ EVENT DETAILS NAVBAR */}
      <EventDetailsNavbar />

      {/* Push content below fixed navbar */}
      <div className="pt-24">
        <div className="max-w-xl mx-auto bg-white p-6 rounded-xl border">
          <h1 className="text-2xl font-semibold text-[#0B132B]">
            Report Lost Item
          </h1>

          <p className="mt-1 text-gray-600">
            Tell us what you lost at this event
          </p>

          <form onSubmit={handleSubmit} className="mt-6 space-y-4">
            {error && (
              <p className="text-sm text-red-600">
                {error}
              </p>
            )}

            <div>
              <label className="text-sm text-gray-600">
                Item name
              </label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                className="mt-1 w-full border rounded-lg px-3 py-2"
                placeholder="Wallet, Phone, Bag..."
                required
              />
            </div>

            <div>
              <label className="text-sm text-gray-600">
                Description (optional)
              </label>
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                className="mt-1 w-full border rounded-lg px-3 py-2"
                rows={4}
                placeholder="Where did you last see it?"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-[#0B132B] text-white py-2 rounded-lg text-sm font-medium disabled:opacity-60"
            >
              {loading ? "Submitting..." : "Submit Report"}
            </button>
          </form>
        </div>
      </div>
    </>
  );
}

export default ReportLostItem;
