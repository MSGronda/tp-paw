import { Notification } from '@mantine/core';
interface FloatingMessageProps {
    header: string;
    text: string
    bottom: string;
    color: string;
    icon: JSX.Element;
}

export default function FloatingMessage (props: FloatingMessageProps) {
    const {header, text,  bottom, color, icon} = props;
    const style = {
        root: {
            // Adjust the width and height as needed
            width: '20rem',
            height: '5rem',
        },
        title: {
            fontSize: '1rem', // Adjust the font size for the title
        },
        description: {
            fontSize: '1rem', // Adjust the font size for the description
        }
    }

    return (
        <div style={{ position: 'fixed', bottom: bottom, right: '50%', zIndex: 1000 }}>
            <>
            <Notification icon={icon} color={color} title={header} withCloseButton={false} withBorder styles={style}>
                {text}
            </Notification>
            </>
        </div>
    );
}


