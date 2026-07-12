import type {AuthUser} from "../../types/ApiResponses/AuthResponses.ts";

export function getStoredAuthUser(): AuthUser | null {
    const value = localStorage.getItem("authUser");
    console.log(value)

    if (!value) {
        return null;
    }

    try {
        return JSON.parse(value) as AuthUser;
    } catch {
        localStorage.removeItem("authUser");
        return null;
    }
}