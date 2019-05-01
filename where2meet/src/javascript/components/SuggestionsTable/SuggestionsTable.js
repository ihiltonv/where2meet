import React from 'react';
import './SuggestionsTable.css'
import StarRatings from 'react-star-ratings';
import DollarSignComponent from '../DollarSignComponent/DollarSignComponent'

class SuggestionsTable extends React.Component {
    constructor() {
        super();
    }

    render() {
        const {data, showRank} = this.props;
        return (
            <div className={"tableContainer"}>
                <div className={"tableBody"}>
                    {data.map((data, index) => (
                        <div className={"tableRow"} key={index}>
                            {showRank && <div className={"rankNum"} align="center">{index + 1}</div>}
                            <div className={"imgContainer"}>
                                <img className={"img"}
                                     src={data.photo ? data.photo : require('../../../resources/icons/icecream.jpg')}/>
                            </div>
                            <div className={"nameAddressRating"}>

                                <div className={"dollarVotes"}>
                                    <div>
                                        <div className={"category"}>
                                            {data.category}
                                        </div>
                                        <a className={"venueName"} href={data.url}>
                                            <div>{data.venue}</div>
                                        </a>
                                    </div>
                                    <div className={"voteButtonContainer"}>
                                        <button id={data.id} className={"voteButton"}
                                                style={{"background-color": "gold"}} value={5}
                                                onClick={this.props.buttonClicked}>1
                                        </button>
                                        <button id={data.id} className={"voteButton"}
                                                style={{"background-color": "silver"}} value={3}
                                                onClick={this.props.buttonClicked}>2
                                        </button>
                                        <button id={data.id} className={"voteButton"}
                                                style={{"background-color": "#cc6633"}} value={1}
                                                onClick={this.props.buttonClicked}>3
                                        </button>
                                    </div>
                                </div>
                                <div className={"addressContainer"}>
                                    <div className={"locationAddress"}>{data.location}</div>
                                </div>
                                <div className={"dollarVotes"}>
                                    <div className={"voteDollar"}>
                                        <div className={"votes"}>👍 {data.votes} </div>
                                        <DollarSignComponent dollars={data.price}/>
                                    </div>
                                    <StarRatings
                                        rating={data.rating}
                                        starRatedColor="gold"
                                        numberOfStars={5}
                                        name='rating'
                                        starDimension={'20px'}
                                        starSpacing={'1px'}
                                    />
                                </div>


                            </div>

                        </div>
                    ))}
                </div>
            </div>
        );
    }
}

export default SuggestionsTable;