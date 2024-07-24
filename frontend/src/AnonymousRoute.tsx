import AuthContext from "./context/AuthContext";
import { useContext } from "react";
import { Navigate } from "react-router-dom";

interface Props {
    component: React.ComponentType
    path?: string
}

export const AnonymousRoute: React.FC<Props> = ({ component: RouteComponent }) => {

const { isAuthenticated } = useContext(AuthContext);

if (!isAuthenticated) {
    return <RouteComponent />
}

return <Navigate to="/error?code=403" replace={true}/>
}
