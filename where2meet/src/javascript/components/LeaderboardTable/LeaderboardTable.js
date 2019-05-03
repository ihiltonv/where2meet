import React from 'react';
import './LeaderboardTable.css'
import StarRatings from 'react-star-ratings';
import DollarSignComponent from '../DollarSignComponent/DollarSignComponent'

class LeaderboardTable extends React.Component {
    constructor() {
        super();
    }

    render() {
        const { data } = this.props;
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
                                    {data.venue && <div className={"votes"}>👍 {data.votes} </div>}

                                    <div>
                                        {/* <div className={"category"}>
                                            {data.category}
                                        </div> */}
                                        {data.venue && <a className={"venueName2"} href={data.url}>
                                            <div>{(data.venue.split("").length > 18) ? data.venue.split("").splice(0, 18).join("") + "..." : data.venue}</div>
                                            {console.log(data.venue.split("").length)}
                                        </a>}

                                    </div>
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