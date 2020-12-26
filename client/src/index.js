import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import ApolloProvider from './ApolloProvider';


ReactDOM.render(ApolloProvider, document.getElementById('root'));
registerServiceWorker();
