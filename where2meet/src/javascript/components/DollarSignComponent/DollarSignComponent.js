import React from 'react';
import './DollarSignComponent.css'

class DollarSignComponent extends React.Component {

    render() {
        let dollarSigns = [];
        const {dollars} = this.props;
        for (let i = 0; i < dollars; i++) {
            dollarSigns.push(<div className={'dollar'} key={i}>$</div>)
        }
        return (
            <div className={"dollar-container"}>
                {dollarSigns}
            </div>
        )
    }
}

export default DollarSignComponent;