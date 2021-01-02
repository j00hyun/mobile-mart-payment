import gql from "graphql-tag";

export const DELETE_MUTATION = gql`
    mutation removeContent($id: ID!){
        removeContent(_id:$id) {
            _id
            title
            content
        }
    }
`;

export const CREATEMUTATION = gql`
    mutation createContent($title:String! $content:String!){
        createContent(contentInput:{
            title:$title,
            content:$content
        }){
            _id
            title
            content
            createdAt
        }
    }
`;

export const UPDATEMUTATION = gql`
    mutation updateContent($id:ID! $title:String! $content:String! ){
        updateContent(
            _id:$id,
            title:$title,
            content:$content
        ){
            _id
            title
            content
            createdAt
        }
    }

`;
