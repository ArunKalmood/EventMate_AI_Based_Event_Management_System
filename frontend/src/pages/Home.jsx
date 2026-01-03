import Hero from "../components/Hero";
import TrendingChips from "../components/TrendingChips";
import HomeEvents from "../components/HomeEvents";
import WhyEventMate from "../components/WhyEventMate";
import LiveMoments from "../components/LiveMoments";
import HostCTA from "../components/HostCTA";
import Footer from "../components/Footer";

function Home() {
  return (
    <div className="w-full">
      {/* HERO */}
      <Hero />

      {/* TRENDING CATEGORIES (EV-185) */}
      <TrendingChips />

      {/* FEATURED / UPCOMING EVENTS */}
      <HomeEvents />

      {/* WHY EVENTMATE */}
      <div className="mt-24">
        <WhyEventMate />
      </div>

      {/* LIVE MOMENTS / SOCIAL PROOF */}
      <div className="mt-8">
        <LiveMoments />
      </div>

      {/* HOST CTA */}
      <div className="mt-20">
        <HostCTA />
      </div>

      {/* FOOTER */}
      <Footer />
    </div>
  );
}

export default Home;
