import { Navigate, useLocation } from 'react-router-dom'
import AuthContext from "./context/AuthContext";
import { useContext } from "react";
import {userService} from "./services";

interface Props {
  component: React.ComponentType
  path?: string
  roles: Array<string>
}

export const PrivateRoute: React.FC<Props> = ({ component: RouteComponent, roles }) => {
  const { isAuthenticated , role} = useContext(AuthContext);
  const location = useLocation();
  const user = userService.getUserData();
  
  if(isAuthenticated && user && !user.degreeId && location.pathname !== "/onboarding") {
    return <Navigate to={"/onboarding"}/>
  }

  if (isAuthenticated && roles.includes(role as string)) {
    return <RouteComponent />
  }

  if (isAuthenticated && !roles.includes(role as string)) {
    return <Navigate to="/error?code=403" replace={true} />
  }

  return <Navigate to='/login' state={{ prev: location.pathname }} />
}
