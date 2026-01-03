function WhyEventMate() {
  return (
    <section className="bg-slate-50 pt-10 pb-20">
      <div className="max-w-6xl mx-auto px-6">

        {/* Heading + subtitle */}
        <div className="max-w-3xl mx-auto text-center mb-14">
          <h2 className="text-3xl md:text-4xl font-semibold text-brand">
            Why <span className="text-blue-500">EventMate</span>?
          </h2>
          <p className="mt-4 text-gray-600 text-base md:text-lg">
            Everything you need to discover, host, and experience events — without the chaos.
          </p>
        </div>

        {/* Feature band */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-10 max-w-5xl mx-auto">

          {/* Feature 1 */}
          <div className="flex items-start gap-4">
            <div className="flex-shrink-0 text-blue-500">
              <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7H3v12a2 2 0 002 2z"
                />
              </svg>
            </div>
            <div>
              <h4 className="font-medium text-brand">
                Discover events
              </h4>
              <p className="text-gray-600 text-sm mt-1 leading-relaxed">
                Find what’s happening around you without overcrowding or guesswork.
              </p>
            </div>
          </div>

          {/* Feature 2 */}
          <div className="flex items-start gap-4">
            <div className="flex-shrink-0 text-blue-500">
              <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M12 6V4m0 2a4 4 0 100 8 4 4 0 000-8zm0 8v6"
                />
              </svg>
            </div>
            <div>
              <h4 className="font-medium text-brand">
                Host with ease
              </h4>
              <p className="text-gray-600 text-sm mt-1 leading-relaxed">
                Organizer-friendly dashboards built for real-world event management.
              </p>
            </div>
          </div>

          {/* Feature 3 */}
          <div className="flex items-start gap-4">
            <div className="flex-shrink-0 text-blue-500">
              <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M3 7h18M5 7v10a2 2 0 002 2h10a2 2 0 002-2V7M9 11h6"
                />
              </svg>
            </div>
            <div>
              <h4 className="font-medium text-brand">
                Never lose memories
              </h4>
              <p className="text-gray-600 text-sm mt-1 leading-relaxed">
                Relive moments and recover lost items even after the event ends.
              </p>
            </div>
          </div>

        </div>

        {/* Soft CTA */}
        <div className="mt-14 flex justify-center">
          <a
            href="/events"
            className="text-sm font-medium text-blue-500 hover:underline"
          >
            Learn more about EventMate →
          </a>
        </div>

      </div>
    </section>
  );
}

export default WhyEventMate;
