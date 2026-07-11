import "./LoginPopup.css"
import * as React from "react";
import {useState} from "react";

type LoginModalProps = {
    onClose: () => void;
};

type LoginForm = {
    username: string;
    password: string;
}
type AuthMode = "SIGNUP" | "LOGIN"

export default function LoginPopup({onClose}: LoginModalProps) {
    const [loginInfo, setLoginInfo] = useState<LoginForm>({username: "", password: ""})
    const [authMode, setAuthMode] = useState<AuthMode>("LOGIN")

    function onBgClick() {
        onClose()
    }

    async function handleLogin(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();
        const endpoint =
            authMode === "LOGIN"
                ? "/api/auth/login"
                : "/api/auth/signup";

        const response = await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(loginInfo),
        })

        console.log(await response.json())
        if (response.ok) {
            onClose()
        }


    }

    return (
        <div className="login-bg" onClick={onBgClick}>
            <div className="form-bg wood-pattern" onClick={(e) => e.stopPropagation()}>
                <form onSubmit={handleLogin} className="form-content ">
                    <input type="text" value={loginInfo.username} onChange={(e) => {
                        setLoginInfo({...loginInfo, username: e.target.value})
                    }} placeholder="Username" className="form-inputs"/>
                    <input type="password" onChange={(e) => {
                        setLoginInfo({...loginInfo, password: e.target.value})
                    }}
                           value={loginInfo.password} placeholder="Password" className="form-inputs"/>
                    <button type="submit" className="form-login">{authMode === "LOGIN" ? "Login" : "Signup"}!</button>
                    <button
                        type="button"
                        className="toggle-auth"
                        onClick={() =>
                            setAuthMode(authMode === "LOGIN" ? "SIGNUP" : "LOGIN")
                        }
                    >
                        {authMode === "LOGIN"
                            ? "Need an account? Sign up"
                            : "Already have an account? Log in"}
                    </button>


                </form>
            </div>
        </div>

    );
}