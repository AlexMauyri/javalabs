import React, { useEffect, useRef } from "react";

const MatrixBackground: React.FC = () => {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);

    useEffect(() => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext("2d");
        if (!ctx) return;

        let fontSize: number;
        let columns: number;
        let drops: number[];

        const resizeCanvas = () => {
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;

            ctx.fillStyle = "rgb(0,0,0)";
            ctx.fillRect(0, 0, canvas.width, canvas.height);

            fontSize = Math.max(Math.floor(Math.min(canvas.width, canvas.height) / 80), 12);
            columns = Math.floor(canvas.width / fontSize);
            drops = Array(columns).fill(1);
        };

        resizeCanvas();
        ctx.fillStyle = "rgb(0,0,0)";
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        const matrixSymbols = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        const draw = () => {
            ctx.fillStyle = "rgba(0, 0, 0, 0.05)";
            ctx.fillRect(0, 0, canvas.width, canvas.height);

            ctx.fillStyle = "#03727e";
            ctx.font = `${fontSize}px monospace`;

            for (let i = 0; i < drops.length; i++) {
                const text = matrixSymbols.charAt(Math.floor(Math.random() * matrixSymbols.length));
                ctx.fillText(text, i * fontSize, drops[i] * fontSize);

                if (drops[i] * fontSize > canvas.height && Math.random() > 0.965) {
                    drops[i] = 0;
                }

                drops[i]++;
            }
        };

        const handleResize = () => {
            ctx.fillStyle = "rgb(0,0,0)";
            ctx.fillRect(0, 0, canvas.width, canvas.height);
            resizeCanvas();
        };

        const interval = setInterval(draw, 50);
        window.addEventListener("resize", handleResize);

        return () => {
            clearInterval(interval);
            window.removeEventListener("resize", handleResize);
        };
    }, []);

    return <canvas ref={canvasRef} style={{ position: "fixed", top: 0, left: 0, zIndex: -1 }} />;
};

export default MatrixBackground;