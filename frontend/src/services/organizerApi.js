import api from "./api";

/**
 * Organizer APIs
 * All calls assume JWT is already handled by api interceptor
 */

/**
 * Get organizer events (dashboard list)
 * GET /organizer/events
 */
export const getOrganizerEvents = async (page = 0, size = 10) => {
  const res = await api.get("/organizer/events", {
    params: { page, size },
  });
  return res.data; // Page<OrganizerEventSummaryDTO>
};

/**
 * Get bookings for a specific event (organizer view)
 * GET /organizer/bookings?eventId=
 */
export const getOrganizerEventBookings = async (eventId) => {
  const res = await api.get("/organizer/bookings", {
    params: { eventId },
  });
  return res.data;
};

/**
 * Close an event
 * PUT /events/{id}/close
 */
export const closeEvent = async (eventId) => {
  const res = await api.put(`/events/${eventId}/close`);
  return res.data;
};

/**
 * Organizer Lost & Found overview
 * GET /organizer/events/lost-found
 */
export const getOrganizerLostFoundOverview = async (page = 0, size = 10) => {
  const res = await api.get("/organizer/events/lost-found", {
    params: { page, size },
  });
  return res.data;
};
