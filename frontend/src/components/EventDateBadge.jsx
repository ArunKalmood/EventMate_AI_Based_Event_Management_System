import React from "react";

function EventDateBadge({ day, month }) {
  return (
    <div className="
      bg-white 
      shadow-lg 
      rounded-xl 
      px-3 py-1.5 
      text-center 
      min-w-[42px]
      border border-gray-100
    ">
      <p className="text-sm font-bold text-gray-900 leading-none">
        {day}
      </p>
      <p className="text-[11px] text-gray-600 -mt-0.5">
        {month}
      </p>
    </div>
  );
}

export default EventDateBadge;
