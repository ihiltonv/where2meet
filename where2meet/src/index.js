import React from 'react';
import ReactDOM from 'react-dom';
import './css/index.css';

import * as serviceWorker from './javascript/serviceWorker';
import { Route, BrowserRouter as Router } from 'react-router-dom'

import AddEventPage from "./javascript/screens/AddEventPage/AddEventPage";
import EventPage from './javascript/screens/EventPage/EventPage'

const routing = (
    <Router>
        <div>
            <Route exact path="/" component={AddEventPage} />
            <Route path="/event" component={EventPage} />
        </div>
    </Router>
);

ReactDOM.render(routing, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
