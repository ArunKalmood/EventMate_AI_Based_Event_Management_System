import React from "react";

export default function EventDetailsSkeleton() {
  return (
    <div className="animate-fadeIn">

      {/* Banner */}
      <div className="w-full h-64 md:h-80 shimmer rounded-xl mb-6"></div>

      <div className="max-w-4xl mx-auto px-4 md:px-0">

        {/* Title */}
        <div className="h-8 shimmer rounded-md w-3/4 mb-4"></div>

        {/* Date / Location lines */}
        <div className="flex flex-col gap-2 mb-6">
          <div className="h-4 shimmer rounded-md w-1/2"></div>
          <div className="h-4 shimmer rounded-md w-1/3"></div>
        </div>

        {/* Description Block */}
        <div className="space-y-3">
          <div className="h-4 shimmer rounded-md w-full"></div>
          <div className="h-4 shimmer rounded-md w-5/6"></div>
          <div className="h-4 shimmer rounded-md w-4/6"></div>
          <div className="h-4 shimmer rounded-md w-2/3"></div>
          <div className="h-4 shimmer rounded-md w-1/2"></div>
        </div>

      </div>
    </div>
  );
}
