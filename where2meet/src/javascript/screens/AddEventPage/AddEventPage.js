import React, {Component} from 'react';
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

    goToEvent = () => {
        this.props.history.push('/event');
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