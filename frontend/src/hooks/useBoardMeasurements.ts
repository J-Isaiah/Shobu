import { useCallback, useLayoutEffect, useRef, useState } from "react";
export type BoardMeasurements = {

    boardWidth: number;
    boardHeight: number;
    cellSize: number;
};
export type Point = {
    x: number,
    y: number,
}

export function getCellCenter(row: number, col: number, cellSize: number, padding = 0) {
    return {
        x: padding + col * cellSize + cellSize / 2,
        y: padding + row * cellSize + cellSize / 2,
    };
}

export function useBoardMeasurements() {
    const boardRef = useRef<HTMLDivElement | null>(null);

    const [measurements, setMeasurements] = useState<BoardMeasurements>({
        boardWidth: 0,
        boardHeight: 0,
        cellSize: 0,
    });

    const measure = useCallback(() => {
        const boardElement = boardRef.current;
        if (!boardElement) return;

        const rect = boardElement.getBoundingClientRect();

        setMeasurements({
            boardWidth: rect.width,
            boardHeight: rect.height,
            cellSize: rect.width / 4,
        });
    }, []);

    useLayoutEffect(() => {
        measure();

        window.addEventListener("resize", measure);

        return () => {
            window.removeEventListener("resize", measure);
        };
    }, [measure]);

    return {
        boardRef,
        measurements,
    };
}
