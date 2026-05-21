import "./stone.css"
type StoneProps = {
    color: "black" | "white";
};

export default function Stone({ color }: StoneProps) {
    return <div className={`stone stone--${color}`} />;
}