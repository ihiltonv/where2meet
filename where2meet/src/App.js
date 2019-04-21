import React, { Component } from 'react';
import './css/App.css';
import Modal from './javascript/components/Modal/Modal'
import {AddEventPage} from "./javascript/screens/AddEventPage/AddEventPage";

class App extends Component {
  constructor() {
    super();
  }

  render() {
    return (
      <AddEventPage props={this.history}/>
    );
  }
}

export default App;
