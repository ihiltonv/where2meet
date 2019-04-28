import React from 'react';
import 'rheostat/initialize';
import Geosuggest from 'react-geosuggest';

import ThemedStyleSheet from 'react-with-styles/lib/ThemedStyleSheet';

import DefaultTheme from 'rheostat/lib/themes/DefaultTheme';
import aphroditeInterface from 'react-with-styles-interface-aphrodite';
import Rheostat from 'rheostat';

import '../../../css/App.css';
import './EventPage.css'
import '../../../javascript/components/Modal/InputForm.css';
import '../../components/Modal/Geosuggest.css'

import LeaderboardTable from "../../components/LeaderboardTable/LeaderboardTable";
import SuggestionsTable from "../../components/SuggestionsTable/SuggestionsTable";
import CollapsableContainer from "../../components/CollapsableContainer/CollapsableContainer";
import API from "../../utils/API";

ThemedStyleSheet.registerInterface(aphroditeInterface);
ThemedStyleSheet.registerTheme(DefaultTheme);


const fakeData = [
    {
        "venue": 'Frozen Yogurt',
        "votes": "10",
        "rating": 4.5,
        "price": 4,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "venue": 'Ice Cream',
        "votes": "8",
        "rating": 4.3,
        "price": 2,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "venue": 'Cake',
        "votes": "6",
        "rating": 3,
        "price": 1,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "venue": 'Frozen Yogurt',
        "votes": "10",
        "rating": 4.5,
        "price": 4,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "venue": 'Ice Cream',
        "votes": "8",
        "rating": 4.3,
        "price": 2,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "venue": 'Cake',
        "votes": "6",
        "rating": 3,
        "price": 1,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "venue": 'Frozen Yogurt',
        "votes": "10",
        "rating": 4.5,
        "price": 4,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "venue": 'Ice Cream',
        "votes": "8",
        "rating": 4.3,
        "price": 2,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert",
        "photo": "photourl"
    },
    {
        "venue": 'Cake',
        "votes": "6",
        "rating": 3,
        "price": 1,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
];

const fakeData2 = [
    {
        "venue": 'Frozen yoghurt',
        "votes": "10",
        "rating": 4.5,
        "price": 4,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert",

    },
    {
        "venue": 'Ice Cream',
        "votes": "8",
        "rating": 4.3,
        "price": 2,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "venue": 'Cake',
        "votes": "6",
        "rating": 3,
        "price": 1,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
];

class EventPage extends React.Component {


    constructor() {
        super();
        this.state = {
            groupName: "Weekend Shenanigans",
            userName: "",
            meetingTime: "",
            meetingDate: "",
            meetingLocation: "",
            priceRange: [false, false, false, false],
            dollarButtonColor: ["goldenrod", "white", "white", "white"],
            searchRadius: 100,
            categories: [],
            popularity: 5,
            suggestionsList: [],
            leaderBoardList: [],
            yourPicksList: [],
        };
    }

    componentDidMount() {
        let eventId = this.props.match.params.id;
        console.log(eventId);
        // get the required data from the database
        API.get(`/event/${eventId}`).then((response) => {
            console.log(response.data);
            let data = response.data;
            this.setState({
                groupName: data.groupName,
                meetingTime: data.meetingTime,
                meetingDate: data.meetingDate,
                leaderBoardList: data.leaderBoardList,
                suggestionsList: data.suggestionsList
            })
        })
            .catch(function (error) {
                console.log(error);
            });
    }

    submitName = (event) => {
        this.setState({userName: event.target.value});
        console.log(event)
    };

    changeDollarButtonState = (event) => {
        let dollarArray = this.state.priceRange;
        dollarArray[event.target.value] = dollarArray[event.target.value] ? false : true;
        this.setState({priceRange: dollarArray});
    };

    render() {
        return (
            <div className={"body"}>
                {/*filters sidebar*/}
                <div className={"filtersContainer"}>
                    {/*Initial inputs*/}
                    <header className="groupName">
                        {this.state.groupName}
                    </header>
                    <div className="InputContainer">
                        <div className={"inputField"}>
                            <div className={"inputTitle"}>Your Name:</div>
                            <input className="nameInput" id={"nameInput"} type={"text"} placeholder=" "
                                   value={this.state.userName} onChange={this.submitName}/>
                        </div>
                        <div className={"inputField"}>
                            <div className={"inputTitle"}>Meeting Time:</div>
                            <input className="nameInput" id={"nameInput"} type={"time"} value={this.state.meetingTime}/>
                        </div>
                        <div className={"inputField"}>
                            <div className={"inputTitle"}>Meeting Date:</div>
                            <input className="nameInput" id={"nameInput"} type={"date"} value={this.state.meetingDate}/>
                        </div>
                        <div className={"inputField"}>
                            <div className={"inputTitle"}> Location:
                            </div>
                            <Geosuggest placeholder={""} id={"geoSuggest"} style={{'input': geoSuggestInputStyle}}/>
                        </div>
                    </div>
                    {/*Filters*/}
                    <div className={"filtersTitle"}>
                        Filters
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Price Range"} filter={
                            <div className={"dollarButtonContainer"}>
                                <button className={"dollarButton"} value={0}
                                        onClick={this.changeDollarButtonState}
                                        style={{
                                            "background-color": this.state.priceRange[0] ? "goldenrod" : "white",
                                            "color": this.state.priceRange[0] ? "white" : "black"
                                        }}
                                >$
                                </button>
                                <button className={"dollarButton"} value={1}
                                        onClick={this.changeDollarButtonState}
                                        style={{
                                            "background-color": this.state.priceRange[1] ? "goldenrod" : "white",
                                            "color": this.state.priceRange[1] ? "white" : "black"
                                        }}
                                >$$
                                </button>
                                <button className={"dollarButton"} value={2}
                                        onClick={this.changeDollarButtonState}
                                        style={{
                                            "background-color": this.state.priceRange[2] ? "goldenrod" : "white",
                                            "color": this.state.priceRange[2] ? "white" : "black"
                                        }}
                                >$$$
                                </button>
                                <button className={"dollarButton"} value={3}
                                        onClick={this.changeDollarButtonState}
                                        style={{
                                            "background-color": this.state.priceRange[3] ? "goldenrod" : "white",
                                            "color": this.state.priceRange[3] ? "white" : "black"
                                        }}
                                >$$$$
                                </button>
                            </div>
                        }/>
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Location Range"} filter={
                            <div className={"locationSliderContainer"}>
                                <Rheostat
                                    min={0.1}
                                    max={10}
                                    values={[1, 50]}
                                />
                            </div>
                        }/>
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Categories"} filter={<div>Something else</div>}/>
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Popularity"} filter={<div>Something else</div>}/>
                    </div>
                </div>
                {/*suggestions list*/}
                <div className={"suggestionsBoardContainer"}>
                    <div className={"tableTitle"}>
                        Some Suggestions
                    </div>
                    <SuggestionsTable showRank={false} data={fakeData}/>
                </div>
                {/*top suggestions list*/}
                <div className={"tablesContainer"}>
                    <div className={"eachTable"}>
                        <div className={"tableTitle"}>
                            LeaderBoard
                        </div>
                        <LeaderboardTable showRank={true} data={fakeData2}/>
                    </div>
                    <div className={"eachTable"}>
                        <div className={"tableTitle"}>
                            Your Picks
                        </div>
                        <LeaderboardTable showRank={true} data={fakeData2}/>
                    </div>
                </div>
            </div>


        )
    }
}

export default EventPage

const geoSuggestInputStyle = {
    "width": "90%",
    "height": "35px",
    "border-width": "0.5px",
    "border-color": "#757575",
    "border-radius": "5px",
    "border-style": "solid",
    "letter-spacing": "1px",
    "font-size": "15px",
    "font-weight": "bold",
    "margin-left": "5%",
    "margin-top": "-5%",
    "text-align": "center"
};
