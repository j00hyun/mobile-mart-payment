import React, {Component} from 'react';
import {BrowserRouter as Router, Route} from 'react-router-dom';

import './App.css';

import Main from "./pages/Main";
import Search from "./pages/Search";
import Crud from "./pages/Crud";
import Login from "./pages/Login";


class App extends Component {
    render() {
        return (
            <Router>
                <div>
                    <Route exact path="/" component={Main}/>
                    <Route exact path="/search" component={Search}/>
                    <Route exact path="/crud" component={Crud}/>
                    <Route exact path="/login" component={Login}/>
                </div>
            </Router>
        );
    }
}

export default App;
