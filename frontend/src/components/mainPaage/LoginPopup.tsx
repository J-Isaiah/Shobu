import "./LoginPopup.css"
import * as React from "react";

type LoginModalProps = {
    onClose: () => void;
};

export default function LoginPopup({onClose}: LoginModalProps) {
    function onBgClick() {
        onClose()
    }

    function handleLogin(event: React.SubmitEvent<HTMLFormElement>) {

        event.preventDefault();
        onClose()
    }

    return (
        <div className="login-bg" onClick={onBgClick}>
            <div className="form-bg wood-pattern" onClick={(e) => e.stopPropagation()}>
                <form onSubmit={handleLogin} className="form-content ">
                    <input type="text" placeholder="Username " className="form-inputs"/>
                    <input type="password" placeholder="Password" className="form-inputs"/>
                    <button type="submit" className="form-login">LogIn!</button>

                </form>
            </div>
        </div>

    );
}