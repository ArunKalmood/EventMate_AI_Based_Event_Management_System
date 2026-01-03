import React from "react";

function Footer() {
  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  return (
    <footer className="bg-slate-900 text-slate-300">
      <div className="max-w-7xl mx-auto px-6 py-16">
        {/* TOP GRID */}
        <div className="grid grid-cols-1 md:grid-cols-5 gap-12">
          {/* BRAND */}
          <div className="md:col-span-2">
            <h3 className="text-white text-2xl font-semibold mb-3">
              EventMate
            </h3>
            <p className="text-slate-400 max-w-md">
              Discover, host, and book events effortlessly. EventMate brings
              people together through unforgettable experiences.
            </p>
          </div>

          {/* DISCOVER */}
          <div>
            <h4 className="text-white font-medium mb-4">Discover</h4>
            <ul className="space-y-2 text-sm">
              <li className="hover:text-white cursor-pointer">Events</li>
              <li className="hover:text-white cursor-pointer">Categories</li>
              <li className="hover:text-white cursor-pointer">Trending</li>
            </ul>
          </div>

          {/* ORGANIZERS */}
          <div>
            <h4 className="text-white font-medium mb-4">For Organizers</h4>
            <ul className="space-y-2 text-sm">
              <li className="hover:text-white cursor-pointer">Create Event</li>
              <li className="hover:text-white cursor-pointer">Dashboard</li>
              <li className="hover:text-white cursor-pointer">Guidelines</li>
            </ul>
          </div>

          {/* COMPANY */}
          <div>
            <h4 className="text-white font-medium mb-4">Company</h4>
            <ul className="space-y-2 text-sm">
              <li className="hover:text-white cursor-pointer">About</li>
              <li className="hover:text-white cursor-pointer">Contact</li>
              <li className="hover:text-white cursor-pointer">Careers</li>
            </ul>
          </div>
        </div>

        {/* DIVIDER */}
        <div className="border-t border-slate-700 mt-12 pt-6 flex flex-col md:flex-row items-center justify-between gap-4">
          {/* COPYRIGHT */}
          <p className="text-sm text-slate-400">
            © {new Date().getFullYear()} EventMate. All rights reserved.
          </p>

          {/* RIGHT ACTIONS */}
          <div className="flex items-center gap-6">
            
            {/* SOCIAL ICONS */}
            <div className="flex items-center gap-4">
              {/* Instagram */}
              <a href="#" aria-label="Instagram" className="hover:text-white">
                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M7.75 2h8.5C19.44 2 22 4.56 22 7.75v8.5C22 19.44 19.44 22 16.25 22h-8.5C4.56 22 2 19.44 2 16.25v-8.5C2 4.56 4.56 2 7.75 2zm0 1.5A4.25 4.25 0 003.5 7.75v8.5A4.25 4.25 0 007.75 20.5h8.5a4.25 4.25 0 004.25-4.25v-8.5A4.25 4.25 0 0016.25 3.5h-8.5z" />
                  <path d="M12 7a5 5 0 100 10 5 5 0 000-10zm0 1.5a3.5 3.5 0 110 7 3.5 3.5 0 010-7z" />
                  <circle cx="17.5" cy="6.5" r="1" />
                </svg>
              </a>

              {/* Twitter / X */}
              <a href="#" aria-label="Twitter" className="hover:text-white">
                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M18.244 2H21l-6.29 7.19L22 22h-6.828l-4.49-5.86L5.6 22H2.844l6.726-7.69L2 2h7l4.06 5.3L18.244 2z" />
                </svg>
              </a>

              {/* LinkedIn */}
              <a href="#" aria-label="LinkedIn" className="hover:text-white">
                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M4.98 3.5C4.98 4.88 3.88 6 2.5 6S0 4.88 0 3.5 1.12 1 2.5 1 4.98 2.12 4.98 3.5zM.5 8.5h4V23h-4zM8.5 8.5h3.8v2h.05c.53-1 1.83-2.05 3.77-2.05 4.03 0 4.78 2.65 4.78 6.1V23h-4v-6.5c0-1.55-.03-3.55-2.17-3.55-2.17 0-2.5 1.7-2.5 3.45V23h-4z" />
                </svg>
              </a>
            </div>

            {/* BACK TO TOP */}
            <button
              onClick={scrollToTop}
              className="text-sm text-slate-400 hover:text-white transition"
            >
              ↑ Top
            </button>
          </div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
