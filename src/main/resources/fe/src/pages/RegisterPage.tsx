import {useState} from "react";
import {useHistory} from "react-router-dom";
import {register} from "../api/auth.ts";

export default function RegisterPage() {
    const [form, setForm] = useState({
        email: '',
        password: '',
        name: '',
        surname: ''
    });
    const history = useHistory();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        await register(form)
        history.push('/login');
    }

    return (
        <form onSubmit={handleSubmit}>
            <input type="email" value={form.email} onChange={e => setForm({...form, email: e.target.value})}/>
            <input type="password" value={form.password} onChange={e => setForm({...form, password: e.target.value})}/>
            <input type="text" value={form.name} onChange={e => setForm({...form, name: e.target.value})}/>
            <input type="text" value={form.surname} onChange={e => setForm({...form, surname: e.target.value})}/>
            <button type="submit">Login</button>
        </form>
    )
}