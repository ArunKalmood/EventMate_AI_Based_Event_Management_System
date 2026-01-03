import api from "./api";

// Fetch ALL events
export const getAllEvents = () => {
  return api.get("/events");
};

// Fetch event by ID (future use)
export const getEventById = (id) => {
  return api.get(`/events/${id}`);
};

// Create event (organizer only)
export const createEvent = (data) => {
  return api.post("/events", data);
};
