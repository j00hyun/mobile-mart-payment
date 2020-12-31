import gql from 'graphql-tag';

export const loginMutationGQL = gql`
    mutation login($login: String!, $password: String!) {
        login(idNum:$login password:$password){
            token
        }
    }
`;

export const userQueryGQL = gql`
    query user {
        allUsers {
            _id
            username
            idNum
        }
    }
`;

export const registerMutationGQL = gql`
    mutation registerUser($username: String!, $idNum: String!, $password: String! ) {
        registerUser(username: $username, idNum: $idNum, password: $password ){
            token
        }
    }
`;

export const allUserGQL = gql`
    query allUsers{
        allUsers{
            _id
            username
            idNum
        }
    }

`;

export const meGQL = gql`
    query me{
        me{
            _id
            idNum
            username
        }
    }
`;