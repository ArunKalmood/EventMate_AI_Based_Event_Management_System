import { useEffect, useState } from "react";
import api from "../../services/api";

function AllReviewsModal({ eventId, onClose }) {
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    api
      .get(`/reviews/events/${eventId}?size=50`)
      .then((res) => setReviews(res.data.content || []));
  }, [eventId]);

  return (
    <div className="fixed inset-0 bg-black/60 flex justify-center items-center z-50">
      <div className="bg-white rounded-xl p-6 w-full max-w-lg">
        <h3 className="text-lg font-semibold mb-2">All Reviews</h3>

        <div className="space-y-3 max-h-96 overflow-y-auto">
          {reviews.map((r) => (
            <div key={r.id}>
              <p className="font-medium">
                {r.userName} · {r.rating}★
              </p>
              <p className="text-sm">{r.comment}</p>
            </div>
          ))}
        </div>

        <button onClick={onClose} className="mt-4 underline">
          Close
        </button>
      </div>
    </div>
  );
}

export default AllReviewsModal;
