import React from "react";
import { useNavigate } from "react-router-dom";

function HostCTA() {
  const navigate = useNavigate();

  return (
    <section className="relative overflow-hidden bg-gradient-to-r from-blue-900 via-blue-800 to-blue-700">
      <div className="mx-auto max-w-7xl px-6 py-20 flex flex-col md:flex-row items-center gap-12">
        {/* LEFT CONTENT */}
        <div className="flex-1 text-white">
          <h2 className="text-3xl md:text-4xl font-semibold mb-4">
            Do you host events?
          </h2>

          <p className="text-blue-200 text-lg mb-8">
            Host experiences that matter, with @EventMate.
          </p>

          <div className="flex items-center gap-6">
            <button
              onClick={() => navigate("/organizer/create-event")}
              className="bg-white text-blue-800 font-medium px-6 py-3 rounded-md hover:bg-blue-50 transition"
            >
              Create an Event
            </button>

            <span className="text-blue-200 text-sm">Create • Share • Grow</span>
          </div>
        </div>

        {/* RIGHT VISUAL */}
        <div className="relative flex-1 flex justify-center">
          {/* Floating profile card */}
          <div className="absolute left-0 top-10 bg-white rounded-xl shadow-lg px-4 py-3 flex items-center gap-3 z-10">
            <div className="w-10 h-10 rounded-full overflow-hidden">
              <img
                src="https://upload.wikimedia.org/wikipedia/commons/a/a5/Instagram_icon.png"
                alt="Instagram"
                className="w-full h-full object-cover"
                draggable={false}
              />
            </div>

            <div className="text-sm">
              <p className="font-medium text-gray-900">@EventMate</p>
              <p className="text-gray-500">Our Instagram page</p>
            </div>
          </div>

          {/* Host image */}
          <img
            src="/public/f89b5785-35a7-4c9f-a3e2-7cbf3d178087.png"
            alt="Event host"
            className="w-72 md:w-80 object-contain"
            draggable={false}
          />

          {/* Decorative bubbles */}
          <span className="absolute top-6 right-16 w-4 h-4 bg-blue-400 rounded-full opacity-60" />
          <span className="absolute bottom-10 right-6 w-6 h-6 bg-blue-300 rounded-full opacity-50" />
        </div>
      </div>
    </section>
  );
}

export default HostCTA;
