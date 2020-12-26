import { useApolloClient } from "@apollo/react-hooks";
import { useAuthToken } from './authToken';


export const useLogout = () => {
    const [, , removeAuthToken] = useAuthToken();
    const apolloClient = useApolloClient();

    const logout = async () => {
        await apolloClient.clearStore(); // we remove all information in the store
        removeAuthToken(); //we clear the authToken
    };
    return logout;
};

