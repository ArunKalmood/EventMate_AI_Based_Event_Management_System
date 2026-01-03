import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

function Navbar() {
  const [scrolled, setScrolled] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);

  const navigate = useNavigate();
  const role = localStorage.getItem("role");
  const token = localStorage.getItem("token");

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  const brandColor = scrolled ? "text-[#0B132B]" : "text-white";
  const hoverColor = scrolled ? "hover:text-[#162040]" : "hover:text-white";

  return (
    <header
      className={`fixed top-0 left-0 w-full z-50 transition-all duration-300
        ${scrolled ? "bg-white shadow-md" : "bg-transparent"}
      `}
    >
      <nav className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
        {/* LOGO */}
        <Link
          to={
            token
              ? role === "ORGANIZER"
                ? "/organizer"
                : "/home"
              : "/login"
          }
          className={`text-2xl font-bold ${brandColor}`}
        >
          EventMate
        </Link>

        {/* DESKTOP */}
        <div className="hidden md:flex items-center gap-8 text-sm font-medium">
          {/* CURATED EVENTS */}
          <button
            onClick={() => {
              const el = document.getElementById("curated-events");
              if (el) {
                el.scrollIntoView({ behavior: "smooth", block: "start" });
              }
            }}
            className={`${brandColor} ${hoverColor} flex items-center gap-2`}
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
            className={`${brandColor} ${hoverColor}`}
          >
            Create Event
          </Link>

          {/* PROFILE */}
          <div className="relative">
            <button
              onClick={() => setProfileOpen(!profileOpen)}
              className={`flex items-center gap-2 transition ${brandColor} hover:scale-[1.03]`}
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
                className={`h-4 w-4 transition-transform duration-200 ${
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
                  <>
                    <Link
                      to="/my-bookings"
                      className="block px-4 py-2 hover:bg-gray-100"
                    >
                      My Bookings
                    </Link>
                    <Link
                      to="/my-lost-items"
                      className="block px-4 py-2 hover:bg-gray-100"
                    >
                      My Lost Items
                    </Link>
                  </>
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

        {/* MOBILE */}
        <button
          className={`md:hidden text-3xl ${brandColor}`}
          onClick={() => setMenuOpen(!menuOpen)}
        >
          {menuOpen ? "✕" : "☰"}
        </button>
      </nav>
    </header>
  );
}

export default Navbar;
