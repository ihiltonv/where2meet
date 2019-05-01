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

ThemedStyleSheet.registerInterface(aphroditeInterface);
ThemedStyleSheet.registerTheme(DefaultTheme);


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
            userName: "name",
            meetingTime: "10:30",
            meetingDate: "2019-10-5",
            meetingLocation: "",
            priceRange: [false, false, false, false],
            dollarButtonColor: ["goldenrod", "white", "white", "white"],
            searchRadius: [0, 100],
            selectedCategories: [],
            categoryOptions: options,
            popularity: 0,
            suggestionsList: [],
            filteredSuggestionList: [],
            leaderBoardList: [{}, {}, {}],
            yourPicksList: [{}, {}, {}],
        };
    }

    getDataFromServer(username) {
        this.setState({username});
        API.post("")
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
                suggestionsList: data.suggestionsList,
                filteredSuggestionList: data.suggestionsList
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

    buttonClicked = (event) => {
        const val = event.target.value;
        const id = event.target.id;
        console.log(event.target.value);
        console.log(event.target.id);
        if (this.state.userName === "") {
            alert("hello")
        } else {
            console.log(this.state.filteredSuggestionList);
            const suggestion = this.state.suggestionsList.filter(suggestion => suggestion.id === id);
            let oldList = this.state.yourPicksList;
            if (val === '5') {
                oldList[0] = suggestion[0];
            } else if (val === '3') {
                oldList[1] = suggestion[0];

            } else {
                oldList[2] = suggestion[0];

            }
            console.log(oldList);
            this.setState({yourPicksList: oldList})

        }
    };

    isFilterObjectValid = (object) => {
        let {priceRange, searchRadius, popularity} = this.state;
        console.log(object.dist);
        // filter based on search radius
        if (object.dist >= searchRadius[0] && searchRadius[1] >= object.dist) {
            return true;
        }
        // filter based on price
        for (let price in priceRange) {
            let numPrice = parseInt(price, 10) + 1;
            if (priceRange[price] === true) {
                if (object.price === numPrice) {
                    return true;
                }
            }
        }

        // filter based on popularity
        if (object.rating >= popularity) {
            console.log(popularity);
            console.log(object.rating);
            return true;
        }

        return false;

    };
    /*methods for filtering suggestions*/
    filterSuggestions = () => {
        let filteredResult = this.state.suggestionsList.filter(this.isFilterObjectValid);
        this.setState({filteredSuggestionList: filteredResult})
    };

    changeDollarButtonState = (event) => {
        let dollarArray = this.state.priceRange;
        dollarArray[event.target.value] = dollarArray[event.target.value] ? false : true;
        this.setState({priceRange: dollarArray});
        this.filterSuggestions();
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
                    <div>
                        Meeting Time: {this.state.meetingTime}
                    </div>
                    <div>
                        Meeting Date: {this.state.meetingDate}
                    </div>
                    {/*<div className="InputContainer">*/}
                    {/*<div className={"inputField"}>*/}
                    {/*<div className={"inputTitle"}> Your Name</div>*/}
                    {/*<input className="nameInput" id={"nameInput"} type={"text"} placeholder=" "*/}
                    {/*value={this.state.userName} onChange={this.submitName}/>*/}
                    {/*</div>*/}
                    {/*<div className={"inputField"}>*/}
                    {/*<div className={"inputTitle"}>Meeting Time:</div>*/}
                    {/*<input className="nameInput" id={"nameInput"} type={"time"} value={this.state.meetingTime}/>*/}
                    {/*</div>*/}
                    {/*<div className={"inputField"}>*/}
                    {/*<div className={"inputTitle"}>Meeting Date:</div>*/}
                    {/*<input className="nameInput" id={"nameInput"} type={"date"} value={this.state.meetingDate}/>*/}
                    {/*</div>*/}
                    {/*</div>*/}
                    {/*Filters*/}
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
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Search Radius"} filter={
                            <div className={"locationSliderContainer"}>
                                <Rheostat
                                    min={0}
                                    max={100}
                                    values={[0, 100]}
                                    onValuesUpdated={async (event) => {
                                        await this.setState({searchRadius: event.values});
                                        this.filterSuggestions();
                                    }}
                                />
                                <div className={"searchRadiusLabelContainer"}>
                                    <h1>{this.state.searchRadius[0] / 10.0} miles</h1>
                                    <h1>{this.state.searchRadius[1] / 10.0} miles</h1>
                                </div>
                            </div>
                        }/>
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
                                    changeRating={async (rating) => {
                                        await this.setState({popularity: rating});
                                        this.filterSuggestions();
                                    }}
                                    rating={this.state.popularity}
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
                    <SuggestionsTable buttonClicked={this.buttonClicked} showRank={false}
                                      data={this.state.filteredSuggestionList}/>
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
