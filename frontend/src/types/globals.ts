export {};

declare global {
    interface Window {
        __RUNTIME_CONFIG__: {
            NODE_ENV: string;
            REACT_APP_KEYCLOAK_URL: string;
            REACT_APP_FRONTEND_URL: string;
            DATA_API_PATH: string;
            CONTROL_API_PATH: string;
        };
    }
}