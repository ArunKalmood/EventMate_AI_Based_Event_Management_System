import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function CreateEvent() {
  const navigate = useNavigate();

  // ==================================================
  // STATE
  // ==================================================
  const [status, setStatus] = useState("loading"); // loading | organizer | not_organizer
  const [upgrading, setUpgrading] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

  const [form, setForm] = useState({
    title: "",
    description: "",
    date: "",
    time: "",
    location: "",
    capacity: "",
    price: "",          // ✅ ADDED
    category: "",
  });

  const [tagsInput, setTagsInput] = useState("");
  const [bannerImage, setBannerImage] = useState(null);
  const [preview, setPreview] = useState(null);
  const [loading, setLoading] = useState(false);

  // ==================================================
  // 🔑 ROLE RESOLUTION — JWT IS SOURCE OF TRUTH
  // ==================================================
  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      navigate("/login");
      return;
    }

    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const role = payload.role || payload.roles?.[0];

      if (role === "ORGANIZER") {
        setStatus("organizer");
      } else {
        setStatus("not_organizer");
      }
    } catch (err) {
      console.error("Invalid JWT", err);
      navigate("/login");
    }
  }, [navigate]);

  // ==================================================
  // UPGRADE TO ORGANIZER
  // ==================================================
  const becomeOrganizer = async () => {
    try {
      setUpgrading(true);

      const res = await api.post("/auth/upgrade-to-organizer");

      // ✅ Replace token with upgraded JWT
      localStorage.setItem("token", res.data.token);

      // ✅ Immediately reflect new role
      setStatus("organizer");

      // ✅ Move user into organizer flow
      navigate("/organizer/create-event");
    } catch (err) {
      console.error("Upgrade failed", err);
      alert("Failed to upgrade to organizer");
    } finally {
      setUpgrading(false);
      setShowConfirm(false);
    }
  };

  // ==================================================
  // FORM HANDLERS
  // ==================================================
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleBannerChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setBannerImage(file);
    setPreview(URL.createObjectURL(file));
  };

  const submit = async () => {
    try {
      setLoading(true);

      const data = new FormData();
      Object.entries(form).forEach(([k, v]) => data.append(k, v));

      tagsInput
        .split(",")
        .map((t) => t.trim())
        .filter(Boolean)
        .slice(0, 10)
        .forEach((tag) => data.append("tags", tag));

      if (bannerImage) data.append("bannerImage", bannerImage);

      await api.post("/events", data, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      navigate("/organizer");
    } catch (err) {
      console.error("Create Event failed", err);
      alert("Failed to create event");
    } finally {
      setLoading(false);
    }
  };

  // ==================================================
  // LOADING STATE
  // ==================================================
  if (status === "loading") {
    return <div className="p-6">Loading...</div>;
  }

  // ==================================================
  // ORGANIZER ONBOARDING
  // ==================================================
  if (status === "not_organizer") {
    return (
      <div className="max-w-3xl mx-auto bg-white border rounded-xl p-10 text-center">
        <h1 className="text-2xl font-semibold mb-4">Become an Organizer</h1>

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
          disabled={upgrading}
          className="bg-[#0B132B] text-white px-6 py-3 rounded-lg disabled:opacity-60"
        >
          Become an Organizer
        </button>

        <p className="text-sm text-gray-400 mt-4">
          Same account • Same email • No logout required
        </p>

        {showConfirm && (
          <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
            <div className="bg-white rounded-xl p-6 w-full max-w-sm text-center">
              <h2 className="text-lg font-semibold mb-4">Are you sure?</h2>
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
                  onClick={becomeOrganizer}
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

  // ==================================================
  // CREATE EVENT FORM — ORGANIZER ONLY
  // ==================================================
  return (
    <div className="bg-white border rounded-xl p-6 max-w-2xl mx-auto">
      <h1 className="text-xl font-semibold mb-6">Create Event</h1>

      <input
        name="title"
        placeholder="Event title"
        className="w-full border p-2 mb-3 rounded"
        onChange={handleChange}
      />

      <textarea
        name="description"
        placeholder="Event description"
        className="w-full border p-2 mb-3 rounded"
        rows={4}
        onChange={handleChange}
      />

      <div className="flex gap-3 mb-3">
        <input
          type="date"
          name="date"
          className="flex-1 border p-2 rounded"
          onChange={handleChange}
        />
        <input
          type="time"
          name="time"
          className="flex-1 border p-2 rounded"
          onChange={handleChange}
        />
      </div>

      <input
        name="location"
        placeholder="Location"
        className="w-full border p-2 mb-3 rounded"
        onChange={handleChange}
      />

      <input
        name="capacity"
        type="number"
        min="0"
        placeholder="Capacity"
        className="w-full border p-2 mb-3 rounded"
        onChange={handleChange}
      />

      {/* ✅ PRICE INPUT */}
      <input
        name="price"
        type="number"
        min="0"
        step="0.01"
        placeholder="Ticket Price (₹) — enter 0 for free event"
        className="w-full border p-2 mb-1 rounded"
        onChange={handleChange}
      />

      <p className="text-xs text-gray-500 mb-3">
        Leave 0 for free events. Paid events will use PayPal at checkout.
      </p>

      <input
        name="category"
        placeholder="Category"
        className="w-full border p-2 mb-3 rounded"
        onChange={handleChange}
      />

      <input
        placeholder="Tags (comma separated)"
        className="w-full border p-2 mb-3 rounded"
        onChange={(e) => setTagsInput(e.target.value)}
      />

      <div className="mb-4">
        <input type="file" accept="image/*" onChange={handleBannerChange} />
        {preview && (
          <img
            src={preview}
            alt="Preview"
            className="mt-3 rounded-lg h-40 object-cover"
          />
        )}
      </div>

      <button
        onClick={submit}
        disabled={loading}
        className="bg-[#0B132B] text-white px-5 py-2 rounded-lg disabled:opacity-60"
      >
        {loading ? "Creating..." : "Create Event"}
      </button>
    </div>
  );
}

export default CreateEvent;
