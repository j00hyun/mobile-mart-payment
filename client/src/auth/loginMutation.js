import gql from 'graphql-tag';
import {useMutation} from '@apollo/react-hooks';
import {useAuthToken} from './authToken';

export const loginMutationGQL = gql`
    mutation login($login: String!, $password: String!) {
        login(idNum:$login password:$password){
            token
        }
    }
`;

const userQueryGQL = gql`
    query user {
        allUsers {
            _id
            username
            idNum
        }
    }
`;

export const useLoginMutation = () => {
    const [_, setAuthToken] = useAuthToken();

    const [mutation, mutationResults] = useMutation(loginMutationGQL,
        {
            refetchQueries: [{ query: userQueryGQL }],
            onCompleted: (data) => {
                setAuthToken(data.login.token);
                localStorage.setItem('token',data.login.token);
                console.log(localStorage.getItem('token'));
            }
        });

    //we have rewritten the function to have a cleaner interface
    const login = (login, password) => {
        return mutation({
            variables: {
                login: login,
                password
            }
        });
    };
    return [login, mutationResults];
};

export default useLoginMutation();


