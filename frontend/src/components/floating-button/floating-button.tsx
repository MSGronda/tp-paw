
import { Button } from '@mantine/core';

interface FloatingButtonProps {
    text: string;
    onClick: () => void;
    bottom: string;
    right: string;
    color: string;
}

const FloatingButton = (props: FloatingButtonProps) => {
    const {text, onClick, bottom, right, color} = props;
    return (
        <div style={{ position: 'fixed', bottom: bottom, right: right, zIndex: 1000 }}>
            <Button  onClick={onClick} size="xl" color={color}>
                {text}
            </Button>
        </div>
    );
};

export default FloatingButton;
