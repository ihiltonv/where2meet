import React from 'react';
import 'rheostat/initialize';
import StarRatings from 'react-star-ratings'
import ThemedStyleSheet from 'react-with-styles/lib/ThemedStyleSheet';
import Select from 'react-select';
import makeAnimated from 'react-select/lib/animated';
import {DirectLink, Events, scroller, scrollSpy} from "react-scroll";

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
import GoogleMap from '../../components/GoogleMap/GoogleMap'

const MESSAGE_TYPE = {
    CONNECT: 0,
    UPDATE: 1,
    SCORING: 2
};

ThemedStyleSheet.registerInterface(aphroditeInterface);
ThemedStyleSheet.registerTheme(DefaultTheme);

console.log(window.location.host);

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
            latlon: [],
            popularity: 0,
            suggestionsList: [],
            filteredSuggestionList: [],
            leaderBoardList: [{}, {}, {}],
            yourPicksList: [{}, {}, {}],
            prevSelected: null,
            socket: new WebSocket(`ws://localhost:4567/voting`),
            iFrameURL: "",
            wantiFrame: false,
            modalHeight: 0,
        };
    }

    openModalHandler = (url, height) => {
        url ? this.setState({
            modalHeight: height,
            isNameModelShowing: true,
            wantiFrame: true,
            iFrameURL: url,
            opacity: "rgba(255,255,255,0.5)"
        }) : this.setState({
            modalHeight: height,
            isNameModelShowing: true,
            opacity: "rgba(255,255,255,0.5)"
        });
    };

    closeModalHandler = () => {
        this.setState({
            isNameModelShowing: false,
            wantiFrame: false,
            opacity: "rgba(255,255,255,0)"
        });
    };

    socketListener = () => {
        this.state.socket.onmessage = async msg => {
            console.log("message recieved");
            const data = JSON.parse(msg.data);
            switch (data.type) {
                default:
                    console.log("Unkown message type:" + data.type);
                    break;
                case MESSAGE_TYPE.CONNECT:
                    console.log("Connected!");
                    let str = '{"type":' + String(MESSAGE_TYPE.CONNECT) + ',"event_id":' + String(this.props.match.params.id) + '}'
                    this.state.socket.send(JSON.parse(JSON.stringify(str)));
                    break;
                case MESSAGE_TYPE.UPDATE:
                    break;
                case MESSAGE_TYPE.SCORING:
                    console.log("Scoring!");

                    let newList = [JSON.parse(data.s1), JSON.parse(data.s2), JSON.parse(data.s3)];


                    await this.setState({
                        leaderBoardList: newList,
                        //suggestionsList: this.state.suggestionsList,
                    });
                    break;
            }
        };
    };

    componentDidMount() {
        let eventId = this.props.match.params.id;
        this.socketListener();

        // get the required data from the database
        API.get(`/event/${eventId}`).then((response) => {
            let data = response.data;
            console.log(data);
            this.setState({
                latlon: data.location,
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

        Events.scrollEvent.register('begin', function () {
            console.log("begin", arguments);
        });

        Events.scrollEvent.register('end', function () {
            console.log("end", arguments);
        });

        scrollSpy.update();
    }


    scrollTo = (elem) => {
        console.log("scrolling to " + elem)
        scroller.scrollTo(elem, {
            duration: 800,
            delay: 0,
            smooth: "easeInOutQuart",
            containerId: "suggTable",
            offset: -100
        });
        this.state.prevSelected && this.state.prevSelected.removeAttribute("style");
        this.setState({prevSelected: document.getElementById(elem)});
        document.getElementById(elem).setAttribute("style", "border-color: #4da6ff; border-width: 8px")

    };

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

    buttonClicked = async (event) => {
        const val = event.target.value;
        const id = event.target.id;
        console.log(event.target.value);
        console.log(event.target.id);
        if (this.state.userName === "") {
            this.openModalHandler()
        } else {
            console.log(this.state.filteredSuggestionList);
            let suggestion = this.state.suggestionsList.filter(suggestion => suggestion.id === id);
            console.log(suggestion);
            let oldList = this.state.yourPicksList;
            let oldSugg = oldList[3 - ((parseInt(val) + 1) / 2)];
            let update = true;

            if (val === '5') {
                if (oldList[1].id === id || oldList[2].id === id) {
                    alert("Sorry Please Don't Vote for the Same Suggestions Twice");
                    update = false;
                } else {
                    suggestion[0].votes += 5;
                    oldList[0] = suggestion[0];
                }
            } else if (val === '3') {
                if (oldList[0].id === id || oldList[2].id === id) {
                    alert("Sorry Please Don't Vote for the Same Suggestions Twice");
                    update = false;
                } else {
                    suggestion[0].votes += 3;
                    oldList[1] = suggestion[0];
                }
            } else {
                if (oldList[1].id === id || oldList[0].id === id) {
                    alert("Sorry Please Don't Vote for the Same Suggestions Twice");
                    update = false;
                } else {
                    suggestion[0].votes += 1;
                    oldList[2] = suggestion[0];
                }

            }
            await this.setState({yourPicksList: oldList});
            if (update) {
                const msg = '{"type":' + String(MESSAGE_TYPE.UPDATE) + ',"votes":' + String(val) +
                    ',"event":' + String(this.props.match.params.id) + ',"suggestion":' +
                    String(suggestion[0].id) + ',"oldSuggestion":' + String(oldSugg.id) + '}';
                console.log(msg);
                console.log(suggestion[0]);
                this.state.socket.send(JSON.parse(JSON.stringify(msg)));
            }

        }
    };

    filterByCategories = (object) => {
        const {selectedCategories} = this.state;
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
        const {searchRadius} = this.state;
        // filter based on search radius
        if (searchRadius[0] === 0 && searchRadius[1] === 0) {
            return true
        } else if (object.dist >= searchRadius[0] / 10.0 && searchRadius[1] / 10.0 >= object.dist) {
            return true;
        }
        return false
    };

    filterByPopularity = (object) => {
        const {popularity} = this.state;
        // filter based on popularity
        if (popularity === 0) {
            return true;
        } else if (object.rating >= popularity) {
            return true;
        }
        return false;
    };

    filterByPriceRange = (object) => {
        const {priceRange} = this.state;
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
        this.setState({filteredSuggestionList: filteredResult})
    };

    changeDollarButtonState = (event) => {
        let dollarArray = this.state.priceRange;
        dollarArray[event.target.value] = dollarArray[event.target.value] ? false : true;
        this.setState({priceRange: dollarArray});
        this.filterSuggestions();
    };


    render() {
        this.socketListener();
        return (
            <div className={"body"}>
                <UsernameModel
                    submitName={this.submitName}
                    show={this.state.isNameModelShowing}
                    close={this.closeModalHandler}
                    wantiFrame={this.state.wantiFrame}
                    url={this.state.iFrameURL}
                    height={this.state.modalHeight}
                />
                {this.state.isNameModelShowing && <div style={{
                    width: "100vw",
                    height: "100vh",
                    backgroundColor: this.state.opacity,
                    position: "absolute",
                    zIndex: 100
                }}/>}
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
                                        await this.setState({selectedCategories: selectedOption})
                                        this.filterSuggestions();
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
                                    values={this.state.searchRadius}
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
                                {/* <button onClick={this.scrollTo("res6")}>CLICK</button> */}
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
                <div className={"suggestionsBoardContainer"} id={"suggTable"}>
                    <div className={"tableTitle"}>
                        Some Suggestions For You!
                    </div>

                    <SuggestionsTable buttonClicked={this.buttonClicked} showRank={false}
                                      openModalURL={this.openModalHandler}
                                      data={this.state.filteredSuggestionList}/>
                </div>
                {/*top suggestions list*/}
                <div className={"rightSidebar"}>
                    <div className={"tablesContainer"}>
                        <div className={"eachTable"}>
                            <div className={"tableTitle"}>
                                LeaderBoard
                            </div>
                            <LeaderboardTable showRank={true} data={this.state.leaderBoardList}
                                              scrollTo={this.scrollTo}/>
                        </div>
                        <div className={"eachTable"}>
                            <div className={"tableTitle"}>
                                Your Picks
                            </div>
                            <LeaderboardTable showRank={true} data={this.state.yourPicksList} scrollTo={this.scrollTo}/>
                        </div>
                    </div>
                    {this.state.latlon[0] && <div className={"gmap"}>
                        <GoogleMap zoom={12} lat={this.state.latlon[0]} lon={this.state.latlon[1]}
                                   markers={this.state.filteredSuggestionList} scrollTo={this.scrollTo}/>
                    </div>}
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