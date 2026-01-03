import { Link } from "react-router-dom";

function LostFoundCard({ item }) {
  const {
    eventId,
    eventTitle,
    totalItems,
    resolvedItems,
    pendingItems,
  } = item;

  // Safety: if eventId is missing, disable navigation
  const Wrapper = eventId ? Link : "div";
  const wrapperProps = eventId
    ? { to: `/organizer/lost-found/${eventId}` }
    : {};

  return (
    <Wrapper
      {...wrapperProps}
      className={`block bg-white rounded-xl border shadow-sm p-6 transition
        ${eventId ? "hover:shadow-md cursor-pointer" : "opacity-60"}
      `}
    >
      <h3 className="text-lg font-semibold text-[#0B132B]">
        {eventTitle}
      </h3>

      <div className="mt-4 grid grid-cols-3 gap-4 text-sm">
        <div>
          <p className="text-gray-500">Total</p>
          <p className="font-semibold text-[#0B132B]">
            {totalItems}
          </p>
        </div>

        <div>
          <p className="text-gray-500">Resolved</p>
          <p className="font-semibold text-green-600">
            {resolvedItems}
          </p>
        </div>

        <div>
          <p className="text-gray-500">Pending</p>
          <p className="font-semibold text-orange-600">
            {pendingItems}
          </p>
        </div>
      </div>
    </Wrapper>
  );
}

export default LostFoundCard;
