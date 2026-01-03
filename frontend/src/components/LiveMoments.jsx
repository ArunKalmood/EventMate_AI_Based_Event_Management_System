import React from "react";
import LiveRow from "./LiveRow";

function LiveMoments() {
  return (
    <section className="relative py-16 bg-slate-50">
      <div className="text-center mb-10">
        <h3 className="text-2xl md:text-3xl font-semibold text-gray-900">
          Loved by people. Lived at EventMate.
        </h3>
        <p className="mt-2 text-gray-500">
          Real moments from real events — happening everywhere.
        </p>
      </div>

      <LiveRow />
    </section>
  );
}

export default LiveMoments;
