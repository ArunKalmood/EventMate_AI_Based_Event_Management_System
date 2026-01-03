import api from "./api";

export const sendChatQuery = async (message) => {
  const res = await api.post("/chat/query", {
    message,
  });
  return res.data;
};
