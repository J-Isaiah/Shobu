import { useEffect, useState } from "react";

export function useIsMobile(breakpoint = 768) {
    const mediaQuery = `(max-width: ${breakpoint}px)`;

    const [isMobile, setIsMobile] = useState(
        window.matchMedia(mediaQuery).matches
    );

    useEffect(() => {
        const media = window.matchMedia(mediaQuery);

        const handleChange = (event: MediaQueryListEvent) => {
            setIsMobile(event.matches);
        };

        media.addEventListener("change", handleChange);

        return () => {
            media.removeEventListener("change", handleChange);
        };
    }, [mediaQuery]);

    return isMobile;
}