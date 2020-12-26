import gql from "graphql-tag";


//Order Create

export const CreateMutation = gql`
    mutation createOrder($menu:String! $hi: String! $username: String!){
        createOrder(orderInput:{
            menu:$menu,
            hi:$hi,
            username:$username
        }){
            _id
            menu
            hi
            username
            createdAt
        }
    }
`

export const RemoveMutation = gql`
    mutation removeOrder($id:ID!){
        removeOrder(_id:$id){
            _id
            menu
            hi
            username
            createdAt
        }
    }`


//로그아웃
export const LogoutMutation = gql`
    mutation logout{
        logout
    }
`

