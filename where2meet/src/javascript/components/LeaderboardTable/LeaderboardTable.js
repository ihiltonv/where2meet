import React from 'react';
import './LeaderboardTable.css'
import StarRatings from 'react-star-ratings';
import DollarSignComponent from '../DollarSignComponent/DollarSignComponent'

class LeaderboardTable extends React.Component {
    constructor() {
        super();
    }

    render() {
        const {data} = this.props;
        return (
            <div className={"tableContainer"}>
                <div className={"tableBody"}>
                    {data.map((data, index) => (
                        <div className={"tableRow1"} key={index}>
                            <div className={"rankNum"} align="center" key={index}>
                                <div className={"rankNumText"}>
                                    {index + 1}
                                </div>
                            </div>
                            <div className={"nameAddressRating1"}>
                                <div className={"dollarVotes"}>
                                    <div>
                                        <div className={"category"}>
                                            {data.category}
                                        </div>
                                        <a className={"venueName"} href={data.url}>
                                            <div>{data.venue}</div>
                                        </a>
                                    </div>
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
                                        starDimension={'10px'}
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

export default LeaderboardTable;