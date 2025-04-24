import {useState} from "react";
import {useAuth} from "../context/AuthContext.tsx";
import { useHistory } from "react-router-dom";
import {login} from "../api/auth.ts";

export default function LoginPage() {
    const [form, setForm] = useState({
        email: '',
        password: ''
    });
    const {login: loginUser} = useAuth();
    const history = useHistory();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const data = await login(form)
        if (data.token) {
            loginUser(data.token);
            history.push('/');
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <input type="email" value={form.email} onChange={e => setForm({...form, email: e.target.value})}/>
            <input type="password" value={form.password} onChange={e => setForm({...form, password: e.target.value})}/>
            <button type="submit">Login</button>
        </form>
    )
}