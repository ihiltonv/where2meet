import React from 'react';
import 'rheostat/initialize';
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
import UsernameModel from "../../components/UsernameModel/UsernameModel";
import GMap from '../../components/GMap/GMap'

ThemedStyleSheet.registerInterface(aphroditeInterface);
ThemedStyleSheet.registerTheme(DefaultTheme);


class EventPage extends React.Component {


    constructor() {
        super();
        this.state = {
            opacity: 1,
            groupName: " ",
            userName: "",
            userID: 0,
            isNameModelShowing: false,
            meetingTime: "",
            meetingDate: "",
            meetingLocation: "",
            priceRange: [false, false, false, false],
            dollarButtonColor: ["goldenrod", "white", "white", "white"],
            searchRadius: [0, 0],
            selectedCategories: [],
            categoryOptions: [],
            popularity: 0,
            suggestionsList: [],
            filteredSuggestionList: [],
            leaderBoardList: [{}, {}, {}],
            yourPicksList: [{}, {}, {}],
        };
    }

    openModalHandler = () => {
        this.setState({
            isNameModelShowing: true,
            opacity: "rgba(255,255,255,0.5)"
        });
    };

    closeModalHandler = () => {
        this.setState({
            isNameModelShowing: false,
            opacity: "rgba(255,255,255,0)"
        });
    };

    componentDidMount() {
        let eventId = this.props.match.params.id;
        // get the required data from the database
        API.get(`/event/${eventId}`).then((response) => {
            let data = response.data;
            console.log(data);
            this.setState({
                groupName: data.groupName,
                meetingTime: data.meetingTime,
                meetingDate: data.meetingDate,
                suggestionsList: data.suggestionsList,
                filteredSuggestionList: data.suggestionsList,
                categoryOptions: data.cats
            })
        })
            .catch(function (error) {
                console.log(error);
            });
    }

    submitName = (name) => {
        let eventId = this.props.match.params.id;
        console.log(eventId);
        let body = {
            user: name,
            event: eventId
        };

        API.post(`/newuser`, body).then((response) => {
            let data = response.data;
            console.log(data);
            const error = data.error;
            const existingUser = data.existingUser;
            if (error) {
                const msg = data.errorMsg;
                alert(msg);
            }
            if (existingUser) {
                alert("This user already exists, if it is not you, please enter another name!")
            }
            this.setState({
                userName: name,
                userID: data.userID
            });
            this.closeModalHandler();
        })
            .catch(function (error) {
                console.log(error);
            });

    };

    buttonClicked = (event) => {
        const val = event.target.value;
        const id = event.target.id;
        console.log(event.target.value);
        console.log(event.target.id);
        if (this.state.userName === "") {
            this.openModalHandler()
        } else {
            console.log(this.state.filteredSuggestionList);
            const suggestion = this.state.suggestionsList.filter(suggestion => suggestion.id === id);
            let oldList = this.state.yourPicksList;

            if (val === '5') {
                if (oldList[1].id === id || oldList[2].id === id) {
                    alert("Sorry Please Don't Vote for the Same Suggestions Twice");
                } else {
                    oldList[0] = suggestion[0];
                }
            } else if (val === '3') {
                if (oldList[0].id === id || oldList[2].id === id) {
                    alert("Sorry Please Don't Vote for the Same Suggestions Twice");
                } else {
                    oldList[1] = suggestion[0];
                }
            } else {
                if (oldList[1].id === id || oldList[0].id === id) {
                    alert("Sorry Please Don't Vote for the Same Suggestions Twice");
                } else {
                    oldList[2] = suggestion[0];
                }

            }
            this.setState({ yourPicksList: oldList })

        }
    };

    filterByCategories = (object) => {
        const { selectedCategories } = this.state;
        //filter based on categories
        if (selectedCategories.length === 0) {
            return true;
        } else {
            for (let category in selectedCategories) {
                if (object.category === selectedCategories[category].value) {
                    return true;
                }
            }
        }
        return false;
    };

    filterBySearchRadius = (object) => {
        const { searchRadius } = this.state;
        // filter based on search radius
        if (searchRadius[0] === 0 && searchRadius[1] === 0) {
            return true
        } else if (object.dist >= searchRadius[0] / 10.0 && searchRadius[1] / 10.0 >= object.dist) {
            return true;
        }
        return false
    };

    filterByPopularity = (object) => {
        const { popularity } = this.state;
        // filter based on popularity
        if (popularity === 0) {
            return true;
        } else if (object.rating >= popularity) {
            return true;
        }
        return false;
    };

    filterByPriceRange = (object) => {
        const { priceRange } = this.state;
        // filter based on price
        if (priceRange[0] === false && priceRange[1] === false
            && priceRange[2] === false && priceRange[3] === false) {
            return true;
        } else {
            for (let price in priceRange) {
                let numPrice = parseInt(price, 10) + 1;
                if (priceRange[price] === true) {
                    if (object.price === numPrice) {
                        return true;
                    }
                }
            }
        }

        return false;
    };
    isFilterObjectValid = (object) => {
        return this.filterByCategories(object)
            && this.filterByPopularity(object)
            && this.filterByPriceRange(object)
            && this.filterBySearchRadius(object);
    };
    /*methods for filtering suggestions*/
    filterSuggestions = () => {
        let filteredResult = this.state.suggestionsList.filter(this.isFilterObjectValid);
        this.setState({ filteredSuggestionList: filteredResult })
    };

    changeDollarButtonState = (event) => {
        let dollarArray = this.state.priceRange;
        dollarArray[event.target.value] = dollarArray[event.target.value] ? false : true;
        this.setState({ priceRange: dollarArray });
        this.filterSuggestions();
    };


    render() {
        return (
            <div className={"body"}>
                {this.state.isNameModelShowing && <div style={{
                    width: "100vw",
                    height: "100vh",
                    backgroundColor: this.state.opacity,
                    position: "absolute",
                    zIndex: 100
                }} />}
                {/*filters sidebar*/}
                <div className={"filtersContainer"}>
                    {/*Initial inputs*/}
                    <header className="groupName">
                        {this.state.groupName}
                    </header>
                    {this.state.userName !== "" && <h2>
                        Welcome! {this.state.userName}
                    </h2>}
                    <div>
                        Meeting Time: {this.state.meetingTime}
                    </div>
                    <div>
                        Meeting Date: {this.state.meetingDate}
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Categories"} filter={
                            <div className={"categoryOptionsContainer"}>
                                <Select
                                    options={this.state.categoryOptions}
                                    onChange={async (selectedOption) => {
                                        await this.setState({ selectedCategories: selectedOption })
                                        this.filterSuggestions();
                                    }}
                                    closeMenuOnSelect={false}
                                    components={makeAnimated()}
                                    isMulti
                                />
                            </div>
                        } />
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Search Radius"} filter={
                            <div className={"locationSliderContainer"}>
                                <Rheostat
                                    min={0}
                                    max={100}
                                    values={this.state.searchRadius}
                                    onValuesUpdated={async (event) => {
                                        await this.setState({ searchRadius: event.values });
                                        this.filterSuggestions();
                                    }}
                                />
                                <div className={"searchRadiusLabelContainer"}>
                                    <h1>{this.state.searchRadius[0] / 10.0} miles</h1>
                                    <h1>{this.state.searchRadius[1] / 10.0} miles</h1>
                                </div>
                            </div>
                        } />
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Price Range"} filter={
                            <div className={"dollarButtonContainer"}>
                                <button className={"dollarButton"} value={0}
                                    onClick={this.changeDollarButtonState}
                                    style={{
                                        "backgroundColor": this.state.priceRange[0] ? "goldenrod" : "white",
                                        "color": this.state.priceRange[0] ? "white" : "black"
                                    }}
                                >$
                                </button>
                                <button className={"dollarButton"} value={1}
                                    onClick={this.changeDollarButtonState}
                                    style={{
                                        "backgroundColor": this.state.priceRange[1] ? "goldenrod" : "white",
                                        "color": this.state.priceRange[1] ? "white" : "black"
                                    }}
                                >$$
                                </button>
                                <button className={"dollarButton"} value={2}
                                    onClick={this.changeDollarButtonState}
                                    style={{
                                        "backgroundColor": this.state.priceRange[2] ? "goldenrod" : "white",
                                        "color": this.state.priceRange[2] ? "white" : "black"
                                    }}
                                >$$$
                                </button>
                                <button className={"dollarButton"} value={3}
                                    onClick={this.changeDollarButtonState}
                                    style={{
                                        "backgroundColor": this.state.priceRange[3] ? "goldenrod" : "white",
                                        "color": this.state.priceRange[3] ? "white" : "black"
                                    }}
                                >$$$$
                                </button>
                            </div>
                        } />
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
                                    changeRating={async (rating) => {
                                        await this.setState({ popularity: rating });
                                        this.filterSuggestions();
                                    }}
                                    rating={this.state.popularity}
                                />
                            </div>
                        } />
                    </div>

                </div>
                {/*suggestions list*/}
                <div className={"suggestionsBoardContainer"}>
                    <div className={"tableTitle"}>
                        Some Suggestions
                    </div>
                    <UsernameModel
                        submitName={this.submitName}
                        show={this.state.isNameModelShowing}
                        close={this.closeModalHandler}
                    />
                    <SuggestionsTable buttonClicked={this.buttonClicked} showRank={false}
                        data={this.state.filteredSuggestionList} />
                </div>
                {/*top suggestions list*/}
                <div className={"rightSidebar"}>
                    <div className={"tablesContainer"}>
                        <div className={"eachTable"}>
                            <div className={"tableTitle"}>
                                LeaderBoard
                        </div>
                            <LeaderboardTable showRank={true} data={this.state.leaderBoardList} />
                        </div>
                        <div className={"eachTable"}>
                            <div className={"tableTitle"}>
                                Your Picks
                        </div>
                            <LeaderboardTable showRank={true} data={this.state.yourPicksList} />
                        </div>
                    </div>
                    <div className={"gmap"}>
                        <GMap />
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

const fakeData = [
    {
        "id": "1",
        "venue": 'Frozen Yogurt',
        "votes": "10",
        "rating": 4.5,
        "price": 4,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "id": "2",
        "venue": 'Ice Cream',
        "votes": "8",
        "rating": 4.3,
        "price": 2,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "id": "3",
        "venue": 'Cake',
        "votes": "6",
        "rating": 3,
        "price": 1,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "id": "4",
        "venue": 'Frozen Yogurt',
        "votes": "10",
        "rating": 4.5,
        "price": 4,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "id": "5",
        "venue": 'Ice Cream',
        "votes": "8",
        "rating": 4.3,
        "price": 2,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "id": "6",
        "venue": 'Cake',
        "votes": "6",
        "rating": 3,
        "price": 1,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "id": "7",
        "venue": 'Frozen Yogurt',
        "votes": "10",
        "rating": 4.5,
        "price": 4,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "id": "8",
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
        "id": "9",
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
        "id": "1",
        "venue": 'Frozen yoghurt',
        "votes": "10",
        "rating": 4.5,
        "price": 4,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert",

    },
    {
        "id": "2",
        "venue": 'Ice Cream',
        "votes": "8",
        "rating": 4.3,
        "price": 2,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
    {
        "id": "3",
        "venue": 'Cake',
        "votes": "6",
        "rating": 3,
        "price": 1,
        "location": 'Thayer Street, Providence, RI, 02912',
        "url": 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        "category": "dessert"
    },
];

//setup_live_scores();

const MESSAGE_TYPE = {
    CONNECT: 0,
    UPDATE: 1,
    SCORING: 2
}

let conn;

const setup_live_scores = () => {
    console.log("Connecting");
    conn = new WebSocket(`ws://${window.location.host}/voting`);

    conn.onerror = err => {
        console.log('Connection error:', err);
    };

    conn.onmessage = msg => {
        const data = JSON.parse(msg.data);

        switch (data.type) {
            case MESSAGE_TYPE.SCORING:
            //TODO: send suggestions to leaderboard
        };
    }
}

setup_live_scores();


//const socket = io(`ws://${window.location.host}/voting`);
