import Keycloak from "keycloak-js";

const _kc = new Keycloak({
    realm: "SmartHome",
    url: `${window.__RUNTIME_CONFIG__.REACT_APP_KEYCLOAK_URL}/auth/`,
    clientId: "smarthome"
});

const initKeycloak = (onAuthenticatedCallback: () => void) => {
    _kc.init({})
        .then((authenticated) => {
            if (!authenticated) {
                console.log("Keycloak Set up correctly");
            }
            onAuthenticatedCallback();
        })
        .catch(console.error);
};

const doLogin = () => _kc.login({ redirectUri: `${window.__RUNTIME_CONFIG__.REACT_APP_FRONTEND_URL}` });

const doLogout = () => _kc.logout({ redirectUri: `${window.__RUNTIME_CONFIG__.REACT_APP_FRONTEND_URL}` });

const getToken = () => _kc.token;

const isLoggedIn = () => !!_kc.token;

const updateToken = (successCallback: any) =>
    _kc.updateToken(5)
        .then(successCallback)
        .catch(doLogin);

const hasRole = (roles: string[]) => roles.some((role) => _kc.hasRealmRole(role));

const getUserId = () => {
    if (_kc.tokenParsed && _kc.tokenParsed.sub) {
        return _kc.tokenParsed.sub
    }

    return "";
};

const UserService = {
    initKeycloak,
    doLogin,
    doLogout,
    isLoggedIn,
    getToken,
    updateToken,
    hasRole,
    getUserId
};

export default UserService;