// src/services/recommendationService.js
import api from "./api";

export const getCuratedEvents = async () => {
  const response = await api.get("/events/curated");
  return response.data;
};
