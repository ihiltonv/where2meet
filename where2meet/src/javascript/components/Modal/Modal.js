import React from 'react';
import Geosuggest from 'react-geosuggest';
import {browserHistory} from "react-router";
import './Modal.css';
import './Geosuggest.css';
import './InputForm.css';


class modal extends React.Component {
    constructor() {
        super();
        this.state = {
            groupName: "",
            meetingTime: "",
            meetingDate: "",
            meetingLocation: "",
        }
    }

    render() {
        return (
            <div>
                <div className="modal-wrapper"
                     style={{
                         transform: this.props.show ? 'translateY(0vh)' : 'translateY(-100vh)',
                         opacity: this.props.show ? '1' : '0'
                     }}>
                    <div className="modal-header">
                        <h3>Create an Event!</h3>
                        <span className="close-modal-btn" onClick={this.props.close}>×</span>
                    </div>
                    <div className="modal-body">
                        <div className="floating-label">
                            <input className="floating-input" type="text" placeholder=" " value={this.state.groupName}
                                   onChange={(event) => {
                                       this.setState({groupName: event.target.value});
                                   }}/>
                            <span className="highlight"></span>
                            <label>Group Name</label>
                        </div>

                        <div className="floating-label">
                            <input className="floating-input" id={"timeInput"} type={"text"}
                                   onClick={() => document.getElementById("timeInput").setAttribute("type", 'time')}
                                   placeholder=" " value={this.state.meetingTime}
                                   onChange={(event) => {
                                       this.setState({meetingTime: event.target.value});
                                       console.log(this.state.meetingTime)
                                   }}/>
                            <span className="highlight"></span>
                            <label>Meeting Time</label>
                        </div>

                        <div className="floating-label">
                            <input className="floating-input" type="text" id={"dateInput"}
                                   onClick={() => document.getElementById("dateInput").setAttribute("type", 'date')}
                                   placeholder=" " value={this.state.meetingDate}
                                   onChange={(event) => {
                                       this.setState({meetingDate: event.target.value});
                                   }}/>
                            <span className="highlight"></span>
                            <label>Meeting Date</label>
                        </div>


                        {/*<div className="floating-label">*/}
                        {/*<select className="floating-select" onClick="this.setAttribute('value', this.value);" value="">*/}
                        {/*<option value=""></option>*/}
                        {/*<option value="1">Alabama</option>*/}
                        {/*<option value="2">Boston</option>*/}
                        {/*<option value="3">Ohaio</option>*/}
                        {/*<option value="4">New York</option>*/}
                        {/*<option value="5">Washington</option>*/}
                        {/*</select>*/}
                        {/*<span className="highlight"></span>*/}
                        {/*<label>Select</label>*/}
                        {/*</div>*/}

                        <div className="floating-label">
                            <Geosuggest className="floating-input" id={"geoSuggest"} placeholder={"Meeting Location"}
                                        onClick={() => {
                                            document.getElementById("locationLabel").removeAttribute("hidden");
                                            document.getElementById("geoSuggest").setAttribute("placeholder", " ")
                                        }}
                                        onSuggestSelect={(suggests) => suggests ? this.setState({meetingLocation: suggests.location}) : {}}
                            />

                            <span className="highlight"></span>
                            <label id="locationLabel" hidden={true}>Meeting Location</label>
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button className="btn-cancel" onClick={this.props.close}>CLOSE</button>
                        <button className="btn-continue"
                                onClick={() => this.props.goToEvent(this.state.groupName, this.state.meetingLocation.lat, this.state.meetingLocation.lng, this.state.meetingDate, this.state.meetingTime)}>CONTINUE
                        </button>
                    </div>

                </div>
            </div>
        );
    }
}

export default modal;