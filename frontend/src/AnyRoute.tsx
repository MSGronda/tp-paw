import AuthContext from "./context/AuthContext";
import {useContext} from "react";
import {Navigate} from "react-router-dom";
import {userService} from "./services";

interface Props {
  component: React.ComponentType
  path?: string
}

export const AnyRoute: React.FC<Props> = ({component: RouteComponent}) => {
  const {isAuthenticated} = useContext(AuthContext);
  const user = userService.getCachedUser();

  if (isAuthenticated && user && !user.degreeId && location.pathname !== "/onboarding") {
    return <Navigate to={"/onboarding"}/>
  }

  return <RouteComponent/>
}
