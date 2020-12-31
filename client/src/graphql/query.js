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


//User Order 목록 불러오기
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

//누적 금액 계산
export const CostQuery = gql`
    query
    {
        howmuch
    }

`

//누적 잔 수
export const CupQuery = gql`
    query{coffeeAmount}`


//TASK QUERY


export const TaskQuery = gql`
    query{
        tasks(search:"",category:0,index:1,hasNext:true,acdc:""){
            _id
            title
        }
    }


`


//모든 유저 불러오기
export const AllUserQuery= gql`
 query{
    allUsers{
    _id
    username
    idNum
}
}
`







