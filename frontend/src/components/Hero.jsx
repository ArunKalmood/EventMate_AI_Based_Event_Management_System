import { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Hero() {
  const navigate = useNavigate();

  const [query, setQuery] = useState("");
  const [city, setCity] = useState("Hyderabad");
  const [open, setOpen] = useState(false);

  const dropdownRef = useRef(null);

  const cities = ["Hyderabad", "Mumbai", "Bangalore", "Chennai", "Delhi"];

  const runSearch = () => {
    const params = new URLSearchParams();
    if (query.trim()) params.set("search", query.trim());
    if (city.trim()) params.set("city", city.trim());
    navigate(`/events?${params.toString()}`);
  };

  const onKeyDown = (e) => {
    if (e.key === "Enter") runSearch();
  };

  // Close dropdown on outside click
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
        setOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () =>
      document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <section className="relative w-full pt-24 h-[75vh] md:h-[80vh] flex items-center justify-center overflow-hidden">
      {/* Background */}
      <div
        className="absolute inset-0 bg-cover bg-center md:bg-top"
        style={{ backgroundImage: "url('/src/assets/hero.webp')" }}
      />

      {/* Overlay */}
      <div className="absolute inset-0 bg-gradient-to-b from-black/70 via-black/60 to-black/40 md:from-black/60 md:via-black/40 md:to-black/20" />

      {/* CONTENT */}
      <div className="relative z-10 text-center text-white px-4 max-w-4xl mx-auto">
        <h1 className="text-3xl sm:text-4xl md:text-6xl font-extrabold tracking-tight leading-tight">
          <span className="text-blue-300">Live More.</span>{" "}
          <span>Stay Home Less.</span>
        </h1>

        <p className="mt-3 sm:mt-4 text-base sm:text-lg md:text-xl text-white/90 px-4 md:px-6 leading-relaxed">
          Find events that turn free time into memories — right here in your
          city.
        </p>

        {/* SEARCH BAR */}
        <div className="mt-8 sm:mt-10 w-full flex justify-center px-2">
          <div className="flex items-center bg-white rounded-full shadow-lg w-full max-w-3xl focus-within:ring-2 focus-within:ring-blue-400 transition">
            {/* SEARCH INPUT */}
            <div className="flex items-center flex-1 px-5 py-4">
              <svg
                width="20"
                height="20"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2.2"
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
                className="ml-3 w-full focus:outline-none text-gray-700 text-sm sm:text-base placeholder-gray-500"
              />
            </div>

            {/* DIVIDER */}
            <div className="hidden sm:block h-6 w-px bg-gray-200/70" />

            {/* LOCATION DROPDOWN */}
            <div ref={dropdownRef} className="relative px-4">
              <button
                onClick={() => setOpen(!open)}
                className="flex items-center gap-2 text-gray-700 text-sm sm:text-base focus:outline-none"
              >
                <svg
                  width="18"
                  height="18"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2.2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  className="text-gray-400"
                >
                  <path d="M12 21s-5-5.5-5-10a5 5 0 0110 0c0 4.5-5 10-5 10z" />
                  <circle cx="12" cy="11" r="1.8" />
                </svg>

                <span>{city}</span>

                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className={`h-4 w-4 transition-transform ${
                    open ? "rotate-180" : ""
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

              {open && (
                <div className="absolute right-0 mt-2 w-48 max-h-60 overflow-auto bg-white rounded-xl shadow-2xl py-1 text-sm animate-fadeIn z-[9999] border border-gray-100">
                  {cities.map((c) => (
                    <button
                      key={c}
                      onClick={() => {
                        setCity(c);
                        setOpen(false);
                      }}
                      className={`block w-full text-left px-4 py-2.5 transition rounded-md mx-1 ${
                        city === c
                          ? "bg-blue-50 text-blue-600 font-semibold"
                          : "text-gray-700 hover:bg-gray-100"
                      }`}
                    >
                      {c}
                    </button>
                  ))}
                </div>
              )}
            </div>

            {/* SEARCH BUTTON */}
            <button
              onClick={runSearch}
              className="bg-blue-500 hover:bg-blue-600 text-white px-6 sm:px-8 py-4 font-medium transition rounded-full m-1"
            >
              Search
            </button>
          </div>
        </div>
      </div>
    </section>
  );
}

export default Hero;
