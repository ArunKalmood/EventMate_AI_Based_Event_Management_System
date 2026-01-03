import { useLocation } from "react-router-dom";
import Navbar from "@/components/Navbar";
import EventDetailsNavbar from "@/components/event-details/EventDetailsNavbar";
import ChatWidget from "@/components/chat/ChatWidget";

function Layout({ children }) {
  const location = useLocation();

  // 👉 Use EventDetailsNavbar ONLY for My Lost Items
  const isMyLostItems = location.pathname === "/my-lost-items";

  return (
    <div className="min-h-screen bg-[#F8FAFC]">
      {isMyLostItems ? <EventDetailsNavbar /> : <Navbar />}

      {/* Allow pages to control width — Hero needs full-width */}
      <main className="w-full">{children}</main>

      {/* ✅ Global Chatbot (Phase 7.1) */}
      <ChatWidget />
    </div>
  );
}

export default Layout;
