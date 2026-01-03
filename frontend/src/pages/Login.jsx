import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const BASE_URL = "http://localhost:8080";

function Login() {
  const navigate = useNavigate();

  const [isSignup, setIsSignup] = useState(false);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("USER");

  const [showPassword, setShowPassword] = useState(false); // 👁️ NEW

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const res = await fetch(`${BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (!res.ok) throw new Error();

      const data = await res.json();
      localStorage.setItem("token", data.token);

      const payload = JSON.parse(atob(data.token.split(".")[1]));
      const userRole = payload.role || "USER";
      localStorage.setItem("role", userRole);

      // ✅ ONLY CHANGE IS HERE
      navigate(userRole === "ORGANIZER" ? "/organizer" : "/home");
    } catch {
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const res = await fetch(`${BASE_URL}/auth/signup`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password, role }),
      });

      if (!res.ok) throw new Error();

      setIsSignup(false);
    } catch {
      setError("Signup failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen w-full bg-black relative overflow-hidden">
      {/* Logo */}
      <div className="absolute top-0 left-0 w-full z-20">
        <div className="max-w-6xl mx-auto px-6 py-6">
          <Link to="/login" className="text-white text-2xl font-bold">
            EventMate
          </Link>
        </div>
      </div>

      {/* Background */}
      <div
        className="absolute inset-0 bg-cover bg-center opacity-60"
        style={{
          backgroundImage:
            "url('https://images.unsplash.com/photo-1516450360452-9312f5e86fc7')",
        }}
      />
      <div className="absolute inset-0 bg-black/70" />

      {/* Content */}
      <div className="relative z-10 min-h-screen flex items-center justify-center">
        <div className="w-full max-w-6xl px-6 flex items-center justify-between">
          {/* Left text */}
          <div className="hidden md:block max-w-xl text-white">
            <h1 className="text-5xl font-bold">
              Discover. Host. <br /> Experience Events.
            </h1>
            <p className="mt-6 text-gray-300">
              EventMate brings people and experiences together.
            </p>
          </div>

          {/* Card */}
          <div className="w-full max-w-md bg-white rounded-2xl p-8 transition-all duration-300">
            <h2 className="text-2xl font-semibold">
              {isSignup ? "Create your account" : "Login to EventMate"}
            </h2>
            <p className="text-sm text-gray-500 mt-1">
              {isSignup
                ? "Sign up to get started"
                : "Enter your credentials to continue"}
            </p>

            {error && (
              <p className="mt-4 text-sm text-red-600 text-center">{error}</p>
            )}

            <form
              onSubmit={isSignup ? handleSignup : handleLogin}
              className="mt-6 space-y-4"
            >
              {isSignup && (
                <input
                  type="text"
                  placeholder="Full name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                  className="w-full px-4 py-3 border rounded-lg"
                />
              )}

              <input
                type="email"
                placeholder="Email address"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="w-full px-4 py-3 border rounded-lg"
              />

              {/* PASSWORD WITH TOGGLE */}
              <div className="relative">
                <input
                  type={showPassword ? "text" : "password"}
                  placeholder="Password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  className="w-full px-4 py-3 border rounded-lg pr-16"
                />

                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2
               text-xs font-medium text-gray-500 hover:text-black"
                >
                  {showPassword ? "HIDE" : "SHOW"}
                </button>
              </div>

              {/* ROLE TOGGLE (Signup only) */}
              {isSignup && (
                <div className="flex gap-2 mt-2">
                  <button
                    type="button"
                    onClick={() => setRole("USER")}
                    className={`flex-1 py-2 rounded-lg border text-sm font-medium transition ${
                      role === "USER"
                        ? "bg-black text-white border-black"
                        : "bg-white text-gray-700 border-gray-300 hover:bg-gray-100"
                    }`}
                  >
                    User
                  </button>

                  <button
                    type="button"
                    onClick={() => setRole("ORGANIZER")}
                    className={`flex-1 py-2 rounded-lg border text-sm font-medium transition ${
                      role === "ORGANIZER"
                        ? "bg-black text-white border-black"
                        : "bg-white text-gray-700 border-gray-300 hover:bg-gray-100"
                    }`}
                  >
                    Organizer
                  </button>
                </div>
              )}

              <button
                type="submit"
                disabled={loading}
                className="w-full py-3 bg-black text-white rounded-lg"
              >
                {loading ? "Please wait..." : isSignup ? "Sign up" : "Login"}
              </button>
            </form>

            <p className="text-xs text-center text-gray-400 mt-6">
              {isSignup ? (
                <>
                  Already have an account?{" "}
                  <button
                    onClick={() => setIsSignup(false)}
                    className="text-black font-medium"
                  >
                    Login
                  </button>
                </>
              ) : (
                <>
                  New here?{" "}
                  <button
                    onClick={() => setIsSignup(true)}
                    className="text-black font-medium"
                  >
                    Sign up
                  </button>
                </>
              )}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
