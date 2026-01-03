function MomentsMarquee() {
  const images = [
    // Concerts & festivals
    "https://images.unsplash.com/photo-1459749411177-042180ce673c",
    "https://images.unsplash.com/photo-1514525253361-bee8718a74a7",
    "https://images.unsplash.com/photo-1492684223066-81342ee5ff30",
    "https://images.unsplash.com/photo-1506157786151-b8491531f063",

    // Yoga & wellness
    "https://images.unsplash.com/photo-1506126613408-eca07ce68773",
    "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b",

    // Beach & outdoor
    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
    "https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3",

    // Community & workshops
    "https://images.unsplash.com/photo-1528605248644-14dd04022da1",
    "https://images.unsplash.com/photo-1517048676732-d65bc937f952",
  ];

  return (
    <section className="py-14 bg-gradient-to-r from-slate-50 via-white to-slate-50">
      <div className="max-w-7xl mx-auto px-6">
        <h3 className="text-center text-lg font-medium text-slate-700 mb-6">
          Loved by people. Lived at EventMate.
        </h3>

        {/* MARQUEE */}
        <div className="overflow-hidden">
          <div
            className="
              flex gap-6 w-[200%]
              animate-marquee
              hover:pause
            "
          >
            {/* Duplicate content ONCE for seamless loop */}
            {[...images, ...images].map((src, i) => (
              <div
                key={i}
                className="
                  w-64 h-40
                  rounded-xl
                  overflow-hidden
                  bg-slate-200
                  flex-shrink-0
                  transition-transform duration-500
                  hover:scale-105
                "
              >
                <img
                  src={`${src}?auto=format&fit=crop&w=800&q=80`}
                  alt="Event moment"
                  className="w-full h-full object-cover"
                />
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}

export default MomentsMarquee;
