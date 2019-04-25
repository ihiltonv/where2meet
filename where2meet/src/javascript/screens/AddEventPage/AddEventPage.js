import React, { Component } from 'react';
import '../../../css/App.css';
import Modal from '../../components/Modal/Modal.js'

export default class AddEventPage extends Component {
    constructor() {
        super();
        this.state = {
            isShowing: false
        }
    };

    openModalHandler = () => {
        this.setState({
            isShowing: true
        });
    };

    closeModalHandler = () => {
        this.setState({
            isShowing: false
        });
    };

    goToEvent = (name, lat, lon, date, time) => {
        this.props.history.push('/event');
        fetch('/event', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: name,
                lat: lat,
                lon: lon,
                date: date,
                time: time,
            })
        }).then(function (response) {
            //TODO: handle response here
        })
    };

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    Welcome to Where2Meet
                </header>
                <Modal
                    goToEvent={this.goToEvent}
                    className="modal"
                    show={this.state.isShowing}
                    close={this.closeModalHandler}>
                </Modal>
                <button className="Add-button" onClick={() => this.openModalHandler()}>
                    +
                </button>
                <p className={"App-label"}>
                    Press the Plus Button to Create An Event
                </p>

            </div>
        );
    }
}