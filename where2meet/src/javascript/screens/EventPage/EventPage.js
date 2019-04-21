import React from 'react'

import '../../../css/App.css';
import '../../../javascript/components/Modal/InputForm.css';
import './EventPage.css'
import '../../components/Modal/Geosuggest.css'
import CustomizedTable from '../../components/CustomizedTable/CustomizedTable'
import Geosuggest from 'react-geosuggest'

const fakeData = [
    {"venue": 'Frozen yoghurt', "votes": "10", "location": 'Thayer', "url": 'www.something.com'},
    {"venue": 'Ice Cream', "votes": "10", "location": 'Thayer', "url": 'www.something.com'},
    {"venue": 'Cake', "votes": "10", "location": 'Thayer', "url": 'www.something.com'},
];

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
                <div className={"tablesContainer"}>
                    <div className={"leaderBoardContainer"}>
                        <div className={"tableTitle"}>
                            Current LeaderBoard
                        </div>
                        <CustomizedTable data={fakeData}/>
                    </div>
                    <div className={"yourPicksContainer"}>
                        <div className={"tableTitle"}>
                            Your Picks
                        </div>
                        <CustomizedTable data={fakeData}/>
                    </div>
                </div>
                <div className="InputContainer">
                    <div>
                        <div>Your Name:</div>
                        <input className="name-input" id={"nameInput"} type={"text"} placeholder=" "/>
                    </div>
                    <div>
                        <div>Location:</div>
                        <Geosuggest placeholder={""} id={"geoSuggest"} style={{'input': geoSuggestInputStyle}}/>
                    </div>
                </div>
                <div className={"suggestionsBoardContainer"}>
                    <div className={"tableTitle"}>
                        Some Suggestions
                    </div>
                    <CustomizedTable/>
                </div>
            </div>


        )
    }
}

export default EventPage

const geoSuggestInputStyle = {
    "margin-top": "-30px",
    "width": "30vw",
    "height": "8vh",
    "border-width": "2px",
    "border-radius": "10px",
    "border-style": "inset",
    "padding-left": "10px",
    "letter-spacing": "2px",
    "font-size": "15px"
};