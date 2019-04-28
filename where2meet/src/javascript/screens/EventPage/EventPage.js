import React from 'react';
import 'rheostat/initialize';
import Geosuggest from 'react-geosuggest';
import StarRatings from 'react-star-ratings'
import ThemedStyleSheet from 'react-with-styles/lib/ThemedStyleSheet';
import Select from 'react-select';
import makeAnimated from 'react-select/lib/animated';

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

const options = [
    {value: 'chocolate', label: 'Chocolate'},
    {value: 'strawberry', label: 'Strawberry'},
    {value: 'sdaf', label: 'Vanilla'},
    {value: 'asdfa', label: 'yas'},
    {value: 'asdfas', label: 'fsdsf'},
    {value: 'asdfasda', label: 'fsd'},
    {value: 'asdfa', label: 'Chocosfsslate'},
    {value: 'sdfaf', label: 'wtesdvasd'},
    {value: 'fsdf', label: 'sfafdas '}
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
            searchRadius: [1, 100],
            selectedCategories: [],
            categoryOptions: options,
            popularity: 0,
            suggestionsList: [],
            leaderBoardList: [],
            yourPicksList: fakeData2,
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
                leaderBoardList: fakeData2,//data.leaderBoardList,
                suggestionsList: fakeData//data.suggestionsList
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
                        <CollapsableContainer title={"Search Radius"} filter={
                            <div className={"locationSliderContainer"}>
                                <Rheostat
                                    min={1}
                                    max={100}
                                    values={[1, 100]}
                                    onValuesUpdated={(event) => this.setState({searchRadius: event.values})}
                                />
                                <div className={"searchRadiusLabelContainer"}>
                                    <h1>{this.state.searchRadius[0] / 10.0} miles</h1>
                                    <h1>{this.state.searchRadius[1] / 10.0} miles</h1>
                                </div>
                            </div>
                        }/>
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Popularity"} filter={
                            <div className={"popularitySelectorContainer"}>
                                <StarRatings
                                    isSelectable={true}
                                    starHoverColor={'gold'}
                                    starRatedColor={"gold"}
                                    numberOfStars={5}
                                    name='rating'
                                    starDimension={'20px'}
                                    starSpacing={'5px'}
                                    changeRating={(rating) => this.setState({popularity: rating})}
                                    rating={this.state.popularity}
                                />
                            </div>
                        }/>
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Categories"} filter={
                            <div className={"categoryOptionsContainer"}>
                                <Select
                                    options={this.state.categoryOptions}
                                    onChange={(selectedOption) => {
                                        console.log(selectedOption);
                                        this.setState({selectedCategories: selectedOption})
                                    }}
                                    closeMenuOnSelect={false}
                                    components={makeAnimated()}
                                    isMulti
                                />
                            </div>
                        }/>
                    </div>
                </div>
                {/*suggestions list*/}
                <div className={"suggestionsBoardContainer"}>
                    <div className={"tableTitle"}>
                        Some Suggestions
                    </div>
                    <SuggestionsTable showRank={false} data={this.state.suggestionsList}/>
                </div>
                {/*top suggestions list*/}
                <div className={"tablesContainer"}>
                    <div className={"eachTable"}>
                        <div className={"tableTitle"}>
                            LeaderBoard
                        </div>
                        <LeaderboardTable showRank={true} data={this.state.leaderBoardList}/>
                    </div>
                    <div className={"eachTable"}>
                        <div className={"tableTitle"}>
                            Your Picks
                        </div>
                        <LeaderboardTable showRank={true} data={this.state.yourPicksList}/>
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
