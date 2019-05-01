import React, {Component} from 'react';
import '../../../css/App.css';
import Modal from '../../components/Modal/Modal.js'
import API from '../../utils/API'

let config = {
    headers: {'Access-Control-Allow-Origin': '*'}
};

export default class AddEventPage extends Component {
    constructor() {
        super();
        this.state = {
            isShowing: false,
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

    goToEvent = (name, lat, lon, date, time, categories) => {

        if ((lat === undefined || lon === undefined || name === "" || date === "" || time === "" || categories === undefined || categories.length === 0)) {
            alert("Please enter all fields!");
        } else {
            let body = {
                name: name,
                lat: lat,
                lon: lon,
                date: date,
                time: time,
                categories: categories,
            };

            API.post('/event', body).then((response) => {
                let id = response.data.id;
                this.props.history.push(`/events/${id}`);
            })
                .catch(function (error) {
                    console.log(error);
                });
        }
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