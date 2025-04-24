import React from "react";
import ReactDOM from "react-dom/client";
import AppRoutes from "./routes/AppRoutes.tsx";
import { AuthProvider } from "./context/AuthContext.tsx";
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <AuthProvider>
            <AppRoutes />
        </AuthProvider>
    </React.StrictMode>
);