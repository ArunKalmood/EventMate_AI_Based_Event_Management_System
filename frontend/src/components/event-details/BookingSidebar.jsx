import React, { useState } from "react";
import CapacityBar from "../CapacityBar";

function BookingSidebar({ event, bookingDisabled = false, onBook }) {
  const {
    capacity = 0,
    bookedCount = 0,
    availableSeats = 0,
    demandState,
    overcrowdingRisk,
    bookedByUser,
    status,
    price = 0,
  } = event;

  // ✅ SINGLE SOURCE OF TRUTH for quantity
  const [quantity, setQuantity] = useState(1);

  const isSoldOut =
    status === "FULL" || availableSeats <= 0 || capacity === 0;

  const isPaidEvent = price > 0;

  const totalAmount = price * quantity;

  // ✅ IMPROVED PRIMARY CTA (ONLY CHANGE)
  const primaryLabel = bookedByUser
    ? "Booked ✓"
    : isSoldOut
    ? "Sold Out"
    : bookingDisabled
    ? "Booking..."
    : isPaidEvent
    ? `Proceed to Pay ₹${totalAmount}`
    : "Book Free Ticket";

  const increaseQty = () => {
    if (bookingDisabled) return;
    if (quantity < availableSeats) {
      setQuantity((q) => q + 1);
    }
  };

  const decreaseQty = () => {
    if (bookingDisabled) return;
    if (quantity > 1) {
      setQuantity((q) => q - 1);
    }
  };

  const handleClick = () => {
    if (isSoldOut || bookedByUser || bookingDisabled) return;
    if (isPaidEvent && quantity <= 0) return;
    onBook(quantity);
  };

  return (
    <div className="bg-white rounded-xl border shadow-sm p-6 space-y-5">
      <h3 className="text-lg font-semibold text-gray-900">Tickets</h3>

      <p className="text-sm text-gray-600">
        Seats left:{" "}
        <span className="font-medium text-gray-900">
          {availableSeats}
        </span>
      </p>

      <CapacityBar booked={bookedCount} capacity={capacity || 1} />

      {price > 0 && (
        <p className="text-sm text-gray-700">
          Price per ticket:{" "}
          <span className="font-semibold">₹{price}</span>
        </p>
      )}

      {price === 0 && (
        <p className="text-sm text-green-700 font-medium">
          🎟️ Free Event — No payment required
        </p>
      )}

      {!isSoldOut && !bookedByUser && (
        <div className="flex items-center justify-between">
          <span className="text-sm font-medium text-gray-700">
            Quantity
          </span>

          <div className="flex items-center gap-3">
            <button
              onClick={decreaseQty}
              disabled={quantity === 1 || bookingDisabled}
              className="w-8 h-8 rounded border text-lg disabled:opacity-40"
            >
              −
            </button>

            <span className="font-medium">{quantity}</span>

            <button
              onClick={increaseQty}
              disabled={quantity >= availableSeats || bookingDisabled}
              className="w-8 h-8 rounded border text-lg disabled:opacity-40"
            >
              +
            </button>
          </div>
        </div>
      )}

      {price > 0 && (
        <div className="flex justify-between text-sm font-medium">
          <span>Total</span>
          <span>₹{totalAmount}</span>
        </div>
      )}

      {price > 0 && (
        <p className="text-xs text-gray-500">
          Secure payment via PayPal (Sandbox)
        </p>
      )}

      {demandState && demandState !== "NONE" && (
        <div className="text-sm">
          {demandState === "TRENDING" && (
            <span className="text-orange-600 font-medium">
              🔥 Trending
            </span>
          )}
          {demandState === "FILLING_FAST" && (
            <span className="text-red-600 font-medium">
              ⚠️ Filling fast
            </span>
          )}
        </div>
      )}

      {overcrowdingRisk && (
        <div className="text-xs text-red-600 bg-red-50 border border-red-200 rounded-md p-2">
          High demand detected. Entry may be regulated.
        </div>
      )}

      <button
        onClick={handleClick}
        disabled={isSoldOut || bookedByUser || bookingDisabled}
        className={`
          w-full py-3 rounded-lg font-medium transition
          ${
            isSoldOut
              ? "bg-gray-200 text-gray-500 cursor-not-allowed"
              : bookedByUser
              ? "bg-green-100 text-green-700 cursor-default"
              : "bg-blue-600 hover:bg-blue-700 text-white"
          }
        `}
      >
        {primaryLabel}
      </button>
    </div>
  );
}

export default BookingSidebar;
