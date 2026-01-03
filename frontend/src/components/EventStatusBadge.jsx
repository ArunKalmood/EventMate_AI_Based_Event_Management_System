import React from "react";

function EventStatusBadge({ status }) {
  const styles = {
    AVAILABLE: "bg-blue-600 text-white",
    FILLING_FAST: "bg-orange-500 text-white",
    FULL: "bg-gray-800 text-white",
  };

  return (
    <span
      className={`
        ${styles[status] || styles.AVAILABLE}
        text-xs font-semibold 
        px-3 py-1 
        rounded-full 
        shadow-md
        tracking-wide
      `}
    >
      {status.replace("_", " ")}
    </span>
  );
}

export default EventStatusBadge;
