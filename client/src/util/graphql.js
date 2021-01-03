import gql from 'graphql-tag';

export const FETCH_POSTS_QUERY = gql`
    query contents($index:Int!){
        contents(search:"",category:0,index:$index,hasNext:true){
            _id,
            content,
            createdAt,
            title
        }
    }
`;

export const SearchQuery = gql`
    query contents($search:String!,$category:Int!,$index:Int!, $hasNext:Boolean!){
        contents(search:$search,category:$category,index:$index,hasNext:$hasNext){
            _id
            title
            content
            createdAt
        }
    }
`;

export const PageQuery = gql`
    query {
        maxIndex
    }
`;
