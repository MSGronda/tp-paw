
import { Button } from '@mantine/core';

interface FloatingButtonProps {
    text: string;
    onClick: () => void;
}

const FloatingButton = (props: FloatingButtonProps) => {
    const {text, onClick} = props;
    return (
        <div style={{ position: 'fixed', bottom: '2rem', right: '2rem', zIndex: 1000 }}>
            <Button onClick={onClick} size="xl" color={"green.7"}>
                {text}
            </Button>
        </div>
    );
};

export default FloatingButton;
