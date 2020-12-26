import gql from 'graphql-tag';

//전체 목록 불러오기
export const SearchQuery = gql`
    query orders{
        orders(search:"",category:1,index:1,hasNext:false,acdc:"username"){
            _id
            menu
            hi
            username
            createdAt
        }
    }
`;


//유저 목록 불러오기
export const UserSearchQuery = gql`
    query orders($search:String!){
        orders(search:$search,category:4,index:1,hasNext:false,acdc:"username"){
            _id
            menu
            hi
            username
            createdAt
        }
    }
`;


//내 목록 불러오기
export const MeQuery = gql`
    query {
        me {
            _id
            idNum
            username
            status
        }
    }

`



//명수 계산
export const CountQuery = gql`
    query howmany{
       howmany
    }

`







