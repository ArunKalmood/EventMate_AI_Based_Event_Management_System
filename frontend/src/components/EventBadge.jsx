import React from "react";

function EventBadge({ status }) {
  const styles = {
    AVAILABLE: "bg-green-100 text-green-700 border border-green-300",
    FILLING_FAST: "bg-amber-100 text-amber-800 border border-amber-400",
    FULL: "bg-red-100 text-red-700 border border-red-400",
  };

  return (
    <span className={`px-3 py-1 rounded-md text-xs font-semibold ${styles[status]}`}>
      {status.replace("_", " ")}
    </span>
  );
}

export default EventBadge;
