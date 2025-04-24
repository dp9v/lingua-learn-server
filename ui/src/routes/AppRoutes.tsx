import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';
import LoginPage from '../pages/LoginPage.tsx';
import RegisterPage from '../pages/RegisterPage.tsx';
import HomePage from '../pages/HomePage.tsx';
import { useAuth } from '../context/AuthContext.tsx';

export default function AppRoutes() {
    const { token } = useAuth();

    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/login" render={() => (token ? <Redirect to="/" /> : <LoginPage />)} />
                <Route path="/register" component={RegisterPage} />
                <Route path="/" render={() => (token ? <HomePage /> : <Redirect to="/login" />)} />
            </Switch>
        </BrowserRouter>
    );
}
