import React from "react";

export default function EventCardSkeleton() {
  return (
    <div className="bg-white rounded-2xl border shadow-sm p-4 w-full animate-fadeIn">
      {/* Poster */}
      <div className="w-full h-40 shimmer rounded-xl mb-4"></div>

      {/* Title bar */}
      <div className="h-4 shimmer rounded w-3/4 mb-3"></div>

      {/* Line 1 */}
      <div className="h-3 shimmer rounded w-1/2 mb-2"></div>

      {/* Line 2 */}
      <div className="h-3 shimmer rounded w-2/3 mb-2"></div>

      {/* Line 3 */}
      <div className="h-3 shimmer rounded w-1/3"></div>
    </div>
  );
}
