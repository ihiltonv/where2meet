import React from 'react'

import '../../../css/App.css';
import '../../../javascript/components/Modal/InputForm.css';
import './EventPage.css'
import '../../components/Modal/Geosuggest.css'
import Geosuggest from 'react-geosuggest';
import CustomizedTable from '../../components/CustomizedTable/CustomizedTable'

class EventPage extends React.Component {
    constructor() {
        super();
        this.state = {
            GroupName: "Weekend Shenanigans"
        };
    }

    render() {
        return (
            <div className="EventContainer">
                <header className="App-header">
                    {this.state.GroupName}
                </header>
                <div className="InputContainer">
                    <div>Your Name:</div>
                    <input className="name-input" id={"nameInput"} type={"text"} placeholder=" "/>
                    <div>Location:</div>
                    <div className="location-input">
                        <Geosuggest placeholder={""} id={"geoSuggest"} style={{'input': geoSuggestInputStyle}}/>
                    </div>
                </div>
                <div className={"leaderBoardContainer"}>
                    <div className={"leaderboardTitle"}>
                        Current LeaderBoard
                    </div>
                    <CustomizedTable/>
                </div>
                {/*<div className={"choiceButtonsContainer"}>*/}

                {/*<button style={{"background-color": "lawngreen"}}>*/}
                {/*1*/}
                {/*</button>*/}
                {/*<button style={{"background-color": "limegreen"}}/>*/}
                {/*<button style={{"background-color": "lightgreen"}}/>*/}
                {/*</div>*/}
                <div className={"suggestionsBoardContainer"}>
                    <CustomizedTable/>
                </div>
            </div>


        )
    }
}

export default EventPage

const geoSuggestInputStyle = {
    "margin-top": "-25px",
    "width": "30vw",
    "height": "9vh",
    "border-width": "2px",
    "border-radius": "10px",
    "border-style": "inset",
    "padding-left": "10px",
    "letter-spacing": "2px",
    "font-size": "15px"
};