import React from 'react';
import './CollapsableContainer.css'
import {Collapse} from "react-collapse";

class CollapsableContainer extends React.Component {
    changePriceFilter = () => {
        this.setState({priceFilterIsOpen: this.state.priceFilterIsOpen ? false : true})
    };

    constructor() {
        super();
        this.state = {
            priceFilterIsOpen: true
        };
    }

    render() {
        const {title} = this.props;

        return (
            <div className={"expandContainer"}>
                <div className={"expandHeader"}>
                    <div>{title}</div>
                    <img src={require('../../../resources/icons/chevron-down.svg')} className={"expandButton"}
                         onClick={() =>
                             this.changePriceFilter()
                         }/>

                </div>
                <Collapse isOpened={this.state.priceFilterIsOpen}>
                    <div>{this.props.filter ? this.props.filter : ("ERROR nothing is here")}</div>
                </Collapse>
                <div className={"line"}/>
            </div>
        )
    }
}

export default CollapsableContainer;