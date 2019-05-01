import React from 'react';
import './UsernameModel.css';

class UsernameModel extends React.Component {
    constructor() {
        super();
        this.state = {
            username: "",
        }
    }

    render() {
        return (
            <div>
                <div className="modal-wrapper"
                     style={{
                         transform: this.props.show ? 'translateY(0vh)' : 'translateY(-100vh)',
                         opacity: this.props.show ? '1' : '0'
                     }}>
                    <div className="modal-header">
                        <h3>Create an Event!</h3>
                        <span className="close-modal-btn" onClick={this.props.close}>×</span>
                    </div>
                    <div className="modal-body">
                        <div className="floating-label">
                            <input className="floating-input" type="text" placeholder=" " value={this.state.groupName}
                                   onChange={(event) => {
                                       this.setState({username: event.target.value});
                                   }}/>
                            <span className="highlight"></span>
                            <label>Your Name</label>
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button className="btn-cancel" onClick={this.props.close}>CLOSE</button>
                        <button className="btn-continue"
                                onClick={() => this.props.getDataFromServer(this.state.username)}>CONTINUE
                        </button>
                    </div>

                </div>
            </div>
        );
    }
}

export default UsernameModel;