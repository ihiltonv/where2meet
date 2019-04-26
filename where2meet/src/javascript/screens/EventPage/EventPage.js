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
            GroupName: "Weekend Shenanigans",
        };
    }

    render() {
        return (
            <div className={"body"}>
                {/*filters sidebar*/}
                <div className={"filtersContainer"}>
                    <header className="groupName">
                        {this.state.GroupName}
                    </header>
                    <div className="InputContainer">
                        <div className={"inputField"}>
                            <div className={"inputTitle"}>Your Name:</div>
                            <input className="nameInput" id={"nameInput"} type={"text"} placeholder=" "/>
                        </div>
                        <div className={"inputField"}>
                            <div className={"inputTitle"}>Meeting Time:</div>
                            <input className="nameInput" id={"nameInput"} type={"time"} value="10:20"/>
                        </div>
                        <div className={"inputField"}>
                            <div className={"inputTitle"}>Meeting Date:</div>
                            <input className="nameInput" id={"nameInput"} type={"date"} value="2019-05-12"/>
                        </div>
                        <div className={"inputField"}>
                            <div className={"inputTitle"}> Location:
                            </div>
                            <Geosuggest placeholder={""} id={"geoSuggest"} style={{'input': geoSuggestInputStyle}}/>
                        </div>
                    </div>
                    <div className={"filtersTitle"}>
                        Filters
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Price Range"} filter={
                            <div className={"dollarButtonContainer"}>
                                <button className={"dollarButton"}>$</button>
                                <button className={"dollarButton"}>$$</button>
                                <button className={"dollarButton"}>$$$</button>
                                <button className={"dollarButton"}>$$$$</button>
                            </div>
                        }/>
                    </div>
                    <div className={"filtersRow"}>
                        <CollapsableContainer title={"Location Range"} filter={
                            <div className={"locationSliderContainer"}>
                                <Rheostat
                                    min={1}
                                    max={50}
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
