import EventDetailsNavbar from "../components/event-details/EventDetailsNavbar";
import { NavLink } from "react-router-dom";

function OrganizerLayout({ children }) {
  return (
    <div className="min-h-screen bg-[#F8FAFC]">
      {/* TOP NAVBAR (UNCHANGED) */}
      <EventDetailsNavbar />

      {/* PAGE BODY */}
      <div className="flex max-w-7xl mx-auto px-6 pt-24 pb-10 gap-8">
        {/* SIDEBAR */}
        <aside className="w-64 bg-white border rounded-xl p-5 h-fit">
          <h2 className="text-lg font-semibold text-[#0B132B] mb-6">
            Organizer
          </h2>

          <nav className="flex flex-col gap-2 text-sm">
            {/* DASHBOARD */}
            <NavLink
              to="/organizer"
              end
              className={({ isActive }) =>
                `px-3 py-2 rounded-lg ${
                  isActive
                    ? "bg-[#0B132B] text-white"
                    : "text-gray-700 hover:bg-gray-100"
                }`
              }
            >
              Dashboard
            </NavLink>

            {/* CREATE EVENT (NEW) */}
            <NavLink
              to="/organizer/create-event"
              className={({ isActive }) =>
                `px-3 py-2 rounded-lg font-medium ${
                  isActive
                    ? "bg-[#0B132B] text-white"
                    : "bg-[#EEF2FF] text-[#0B132B] hover:bg-[#E0E7FF]"
                }`
              }
            >
              Create Event
            </NavLink>

            {/* LOST & FOUND */}
            <NavLink
              to="/organizer/lost-found"
              className={({ isActive }) =>
                `px-3 py-2 rounded-lg ${
                  isActive
                    ? "bg-[#0B132B] text-white"
                    : "text-gray-700 hover:bg-gray-100"
                }`
              }
            >
              Lost & Found
            </NavLink>
          </nav>
        </aside>

        {/* MAIN CONTENT */}
        <main className="flex-1">
          {children}
        </main>
      </div>
    </div>
  );
}

export default OrganizerLayout;
