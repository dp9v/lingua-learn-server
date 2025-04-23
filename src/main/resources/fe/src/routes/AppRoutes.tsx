import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import HomePage from "../pages/HomePage";
import { useAuth } from "../context/AuthContext";

export default function AppRoutes() {
    const { token } = useAuth();

    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/login" render={() => (
                    token ? <Redirect to="/" /> : <LoginPage />
                )} />
                <Route path="/register" component={RegisterPage} />
                <Route path="/" render={() => (
                    token ? <HomePage /> : <Redirect to="/login" />
                )} />
            </Switch>
        </BrowserRouter>
    );
}
