import React from 'react';
import './SuggestionsTable.css'
import StarRatings from 'react-star-ratings';
import DollarSignComponent from '../DollarSignComponent/DollarSignComponent'
import {DirectLink, Element} from 'react-scroll'

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
                        <Element className={"tableRow"} key={index} name={data.venue + ":" + data.id}
                                 id={data.venue + ":" + data.id}>
                            {showRank && <div className={"rankNum"} align="center">{index + 1}</div>}
                            <div className={"imgContainer"} style={{"background-image": `url(${data.photo})`}}/>


                            <div className={"nameAddressRating"}>

                                <div className={"dollarVotes"}>
                                    <div>
                                        <div className={"category"}>
                                            {data.category}
                                        </div>
                                        <div className={"venueName"}>
                                            {/*<a className={"venueName"} target="_blank" href={data.url}>{data.venue}</a>*/}
                                            <div className={"venueName"}
                                                 onClick={() => {
                                                     this.props.openModalURL(data.url, document.getElementById(data.venue + ":" + data.id).scrollTop)
                                                 }}>
                                                {data.venue}
                                            </div>
                                        </div>
                                    </div>
                                    <div className={"voteButtonContainer"}>
                                        <button id={data.id} className={"voteButton"}
                                                style={{"backgroundColor": "gold"}} value={5}
                                                onClick={this.props.buttonClicked}>1
                                        </button>
                                        <button id={data.id} className={"voteButton"}
                                                style={{"backgroundColor": "silver"}} value={3}
                                                onClick={this.props.buttonClicked}>2
                                        </button>
                                        <button id={data.id} className={"voteButton"}
                                                style={{"backgroundColor": "#cc6633"}} value={1}
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
                                        <div className={"dist"}>  {data.dist.toFixed(2)} mi</div>
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

                        </Element>
                    ))}
                </div>
            </div>
        );
    }
}

export default SuggestionsTable;