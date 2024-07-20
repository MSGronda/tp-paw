import {useEffect} from "react";

interface TitleProps {
    text: string
}

export default function Title({text}: TitleProps) {
    useEffect(() => {
        // Set the page title when the component mounts
        document.title = text + ' - Uni';

        // Optionally, you can return a cleanup function to reset the title when the component unmounts
        return () => {
            document.title = 'Uni';
        };
    }, [text]);

    return <></>
}
