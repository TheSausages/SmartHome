import React from "react";
import UserService from "../../service/UserService";

interface propsInt {
    children: JSX.Element
}

const PrivateRoute = (props: propsInt) => {
    return UserService.isLoggedIn() ? props.children : <>
        {UserService.doLogin()}
    </>;
};

export default PrivateRoute;