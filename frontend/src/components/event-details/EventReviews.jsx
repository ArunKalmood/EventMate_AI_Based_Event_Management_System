import { useEffect, useState } from "react";
import api from "../../services/api";
import AllReviewsModal from "../reviews/AllReviewsModal";

function EventReviews({ eventId }) {
  const [reviews, setReviews] = useState([]);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState("");
  const [loading, setLoading] = useState(false);
  const [showAll, setShowAll] = useState(false);

  const loadReviews = async () => {
    const res = await api.get(`/reviews/events/${eventId}?size=3`);
    setReviews(res.data.content || []);
  };

  useEffect(() => {
    loadReviews();
  }, [eventId]);

  const submitReview = async () => {
    if (loading || !comment.trim()) return;
    setLoading(true);

    try {
      await api.post(
        `/reviews/events/${eventId}?rating=${rating}&comment=${comment}`
      );
      setComment("");
      await loadReviews();
    } catch {
      alert("You must attend the event to review");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <h3 className="text-xl font-semibold">Reviews</h3>

      {/* ADD REVIEW */}
      <div className="bg-white border rounded-lg p-4 space-y-3 shadow-sm">
        {/* STAR RATING */}
        <div className="flex gap-1">
          {[1, 2, 3, 4, 5].map((r) => (
            <button
              key={r}
              onClick={() => setRating(r)}
              className={`text-xl transition ${
                r <= rating ? "text-[#0B132B]" : "text-gray-300"
              }`}
            >
              ★
            </button>
          ))}
        </div>

        <textarea
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="Share your experience..."
          rows={3}
          className="w-full border rounded-md p-3 text-sm resize-none focus:outline-none focus:ring-2 focus:ring-[#0B132B]"
        />

        <button
          onClick={submitReview}
          disabled={loading}
          className="bg-[#0B132B] text-white px-5 py-2 rounded-md text-sm font-medium hover:bg-opacity-90 disabled:opacity-50"
        >
          Submit Review
        </button>
      </div>

      {/* REVIEWS LIST */}
      <div className="space-y-3">
        {reviews.map((r) => (
          <div
            key={r.id}
            className="bg-gray-50 rounded-lg p-3"
          >
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-full bg-[#0B132B] text-white flex items-center justify-center text-sm">
                {r.userName?.[0]?.toUpperCase()}
              </div>
              <p className="font-medium text-sm">
                {r.userName}
                <span className="ml-2 text-[#0B132B]">
                  {"★".repeat(r.rating)}
                </span>
              </p>
            </div>
            <p className="text-sm text-gray-600 mt-1">{r.comment}</p>
          </div>
        ))}
      </div>

      <button
        onClick={() => setShowAll(true)}
        className="text-sm underline hover:text-[#0B132B]"
      >
        See all reviews
      </button>

      {showAll && (
        <AllReviewsModal
          eventId={eventId}
          onClose={() => setShowAll(false)}
        />
      )}
    </div>
  );
}

export default EventReviews;
