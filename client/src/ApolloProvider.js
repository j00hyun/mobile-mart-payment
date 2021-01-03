import React from 'react';
import App from './App';
import {ApolloClient, ApolloLink} from 'apollo-boost'
import {onError} from 'apollo-link-error'

import {InMemoryCache} from 'apollo-cache-inmemory';
import {createHttpLink} from 'apollo-link-http';
import {ApolloProvider} from '@apollo/react-hooks';

const httpLink = createHttpLink({
    uri: 'http://localhost:4000/graphql'
});

const errorLink = onError(({graphQLErrors, networkError}) => {
    if (graphQLErrors) {
        console.log('graphQLErrors', graphQLErrors);
    }
    if (networkError) {
        console.log('networkError', networkError);
    }
});


const client = new ApolloClient({
    link: ApolloLink.from([errorLink, httpLink]),
    cache: new InMemoryCache()
});


export default (
    <ApolloProvider client={client}>
        <App/>
    </ApolloProvider>
);