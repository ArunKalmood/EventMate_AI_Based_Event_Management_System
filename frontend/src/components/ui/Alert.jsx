// src/components/ui/Alert.jsx
import React, { useEffect, useState } from "react";

export default function Alert({ type = "info", children, duration = 4000 }) {
  const [visible, setVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => setVisible(false), duration);
    return () => clearTimeout(timer);
  }, [duration]);

  if (!visible) return null;

  const styles = {
    success: "bg-green-50 text-green-700 border-green-300",
    error: "bg-red-50 text-red-700 border-red-300",
    info: "bg-blue-50 text-blue-700 border-blue-300",
  };

  return (
    <div
      role="alert"
      className={`
        px-4 py-3 rounded-xl border shadow-sm mb-4
        transition-all duration-300 animate-fadeIn
        ${styles[type]}
      `}
    >
      {children}
    </div>
  );
}
