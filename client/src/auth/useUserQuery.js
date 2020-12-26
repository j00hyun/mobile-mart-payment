import gql from 'graphql-tag';
import { useQuery } from '@apollo/react-hooks';

const userQueryGQL = gql`
    query user {
        me {
            _id
            username
            idNum
        }
    }
`;

export const useUserQuery = () => useQuery(userQueryGQL);