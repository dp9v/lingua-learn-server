import {useEffect, useState} from "react";
import {useAuth} from "../context/AuthContext";
import {me} from "../api/auth.ts";


export default function HomePage() {
    const [name, setName] = useState("");
    const {token} = useAuth();

    useEffect(() => {
        if (!token) return;
        me(token).then(setName)
    }, [token]);
    return (
        <div>
            <h1>Hello {name}</h1>
        </div>
    )
}