import React from 'react';
import './ExplanationModal.css';

class ExplanationModal extends React.Component {
    constructor() {
        super();
        this.state = {
            username: "",
        }
    }

    render() {
        return (
            <div className="modal-wrapper2"
                style={{
                    transform: this.props.show ? 'translateY(0vh)' : 'translateY(-100vh)',
                    opacity: this.props.show ? '1' : '0'
                }}>
                <div className="modal-header2">
                    <h3>Welcome to Where2Meet!</h3>
                    <span className="close-modal-btn" onClick={this.props.close}>×</span>
                </div>
                <div className="modal-body2">
                    <h2 align="left">1) Apply filters to explore our suggestions!</h2>
                    <h2 align="left">2) Use these buttons to vote for your favorite suggestions!</h2>
                    <div className={"voteButtonContainer"}>
                        <button className={"voteButton"}
                            style={{ "backgroundColor": "gold" }} value={5}>
                            1
                        </button>
                        <button className={"voteButton"}
                            style={{ "backgroundColor": "silver" }} value={3}>
                            2
                        </button>
                        <button className={"voteButton"}
                            style={{ "backgroundColor": "#cc6633" }} value={1}>
                            3
                        </button>
                    </div>
                    <h2 align="left">3) Share the link with your friends to decide on Where 2 Meet!</h2>
                    <a href={window.location.href}>
                        {window.location.href}
                    </a>
                </div>
                <div className="modal-footer2">

                </div>

            </div>
        )
            ;
    }
}

export default ExplanationModal;