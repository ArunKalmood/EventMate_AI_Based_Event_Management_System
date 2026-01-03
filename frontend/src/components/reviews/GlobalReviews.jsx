import { useEffect, useState } from "react";
import api from "../../services/api";

const mascotImage =
  "/pngtree-singer-on-stage-performing-rock-music-concert-png-image_16259618.png";

function GlobalReviews() {
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    api
      .get("/reviews/latest")
      .then(res => setReviews(res.data.content || []))
      .catch(() => setReviews([]));
  }, []);

  if (reviews.length === 0) return null;

  return (
    <section className="max-w-7xl mx-auto px-6 py-20">
      <div className="flex gap-12 items-center">
        
        {/* LEFT — VISUAL ANCHOR */}
        <div className="hidden md:flex items-center justify-center w-[260px] shrink-0">
          <img
            src={mascotImage}
            alt="EventMate moments"
            className="
              h-[260px]
              w-auto
              object-contain
              opacity-90
            "
          />
        </div>

        {/* RIGHT — CONTENT */}
        <div className="flex-1 overflow-hidden">
          <h2 className="text-2xl font-bold text-gray-900 mb-1">
            EventMate Moments
          </h2>
          <p className="text-sm text-gray-500 mb-6">
            Real experiences from people who attended
          </p>

          {/* SCROLLABLE STRIP */}
          <div
            className="
              flex gap-5
              overflow-x-auto
              pb-4
              scrollbar-hide
              snap-x snap-mandatory
            "
          >
            {reviews.map(r => (
              <div
                key={r.id}
                className="
                  min-w-[300px]
                  flex-shrink-0
                  snap-start
                  rounded-xl
                  border border-gray-200
                  bg-white
                  p-4
                  transition
                  hover:shadow-md
                "
              >
                {/* USER + RATING */}
                <div className="flex items-center justify-between mb-1">
                  <p className="font-medium text-sm text-gray-900">
                    {r.userName}
                  </p>
                  <span className="text-yellow-500 text-sm">
                    {"★".repeat(r.rating)}
                  </span>
                </div>

                {/* EVENT TITLE */}
                <p className="text-xs text-indigo-600 font-medium mb-2 truncate">
                  {r.eventTitle}
                </p>

                {/* COMMENT */}
                <p className="text-sm text-gray-600 leading-relaxed line-clamp-3">
                  {r.comment}
                </p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}

export default GlobalReviews;
