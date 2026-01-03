import { Link, useNavigate } from "react-router-dom";
import { useState, useRef, useEffect } from "react";

function EventDetailsNavbar() {
  const [profileOpen, setProfileOpen] = useState(false);
  const navigate = useNavigate();
  const role = localStorage.getItem("role");
  const token = localStorage.getItem("token");

  const [query, setQuery] = useState("");

  const runSearch = () => {
    if (!query.trim()) return;
    navigate(`/events?search=${encodeURIComponent(query.trim())}`);
  };

  const onKeyDown = (e) => {
    if (e.key === "Enter") runSearch();
  };

  const profileRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (profileRef.current && !profileRef.current.contains(e.target)) {
        setProfileOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <header className="fixed top-0 left-0 w-full z-50 bg-white border-b">
      <nav className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
        {/* LOGO — FIXED */}
        <Link
          to={
            token
              ? role === "ORGANIZER"
                ? "/organizer"
                : "/home"
              : "/login"
          }
          className="text-2xl font-bold text-[#0B132B]"
        >
          EventMate
        </Link>

        <div className="hidden md:flex flex-1 max-w-xl mx-8">
          <div className="flex items-center w-full bg-gray-100 rounded-full px-4 py-2">
            <svg
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
              className="text-gray-400"
            >
              <circle cx="11" cy="11" r="7" />
              <line x1="20" y1="20" x2="16.5" y2="16.5" />
            </svg>

            <input
              type="text"
              placeholder="Search events, categories..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              onKeyDown={onKeyDown}
              className="ml-3 w-full bg-transparent text-sm focus:outline-none text-gray-700 placeholder-gray-500"
            />
          </div>
        </div>

        <div className="hidden md:flex items-center gap-8 text-sm font-medium">
          <button
            onClick={() => navigate("/curated")}
            className="flex items-center gap-2 text-[#0B132B] hover:text-[#162040]"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-4 w-4"
              viewBox="0 0 24 24"
              fill="currentColor"
            >
              <path d="M12 2l1.8 4.6L18 8.4l-4.2 1.8L12 15l-1.8-4.8L6 8.4l4.2-1.8L12 2z" />
              <path
                d="M19 13l.9 2.3L22 16l-2.1.7L19 19l-.9-2.3L16 16l2.1-.7L19 13z"
                opacity="0.7"
              />
            </svg>
            <span>Curated Events</span>
          </button>

          <Link
            to="/organizer/create-event"
            className="text-[#0B132B] hover:text-[#162040]"
          >
            Create Event
          </Link>

          <div ref={profileRef} className="relative">
            <button
              onClick={() => setProfileOpen(!profileOpen)}
              className="flex items-center gap-2 transition hover:scale-[1.03]"
            >
              <div className="h-9 w-9 rounded-full bg-[#0B132B] flex items-center justify-center">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-5 w-5 text-white"
                  viewBox="0 0 24 24"
                  fill="currentColor"
                >
                  <path d="M12 12c2.7 0 4.5-2.1 4.5-4.5S14.7 3 12 3 7.5 5.1 7.5 7.5 9.3 12 12 12z" />
                  <path d="M4 20c0-3.3 3.6-6 8-6s8 2.7 8 6v1H4v-1z" />
                </svg>
              </div>

              <svg
                xmlns="http://www.w3.org/2000/svg"
                className={`h-4 w-4 transition-transform ${
                  profileOpen ? "rotate-180" : ""
                }`}
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                strokeWidth={2}
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M19 9l-7 7-7-7"
                />
              </svg>
            </button>

            {profileOpen && (
              <div className="absolute right-0 mt-3 w-44 bg-white rounded-xl shadow-lg py-2 text-sm animate-fadeIn">
                {role === "USER" && (
                  <Link
                    to="/my-bookings"
                    className="block px-4 py-2 hover:bg-gray-100"
                  >
                    My Bookings
                  </Link>
                )}

                {role === "ORGANIZER" && (
                  <Link
                    to="/organizer"
                    className="block px-4 py-2 hover:bg-gray-100"
                  >
                    Organizer Panel
                  </Link>
                )}

                <button
                  onClick={handleLogout}
                  className="w-full text-left px-4 py-2 hover:bg-gray-100 text-red-600"
                >
                  Logout
                </button>
              </div>
            )}
          </div>
        </div>
      </nav>
    </header>
  );
}

export default EventDetailsNavbar;
