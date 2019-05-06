import React from 'react';
import './UsernameModel.css';
import Iframe from 'react-iframe'

class UsernameModel extends React.Component {
    constructor() {
        super();
        this.state = {
            username: "",
        }
    }

    render() {
        let {wantiFrame, url, height} = this.props;
        console.log(height);
        return (
            <div className="modal-wrapper2"
                 style={{
                     marginTop: height,
                     transform: this.props.show ? 'translateY(0vh)' : 'translateY(-100vh)',
                     opacity: this.props.show ? '1' : '0'
                 }}>
                <div className="modal-header2">
                    <h3>{!wantiFrame && "Please Give Us Your Name!"}</h3>
                    <span className="close-modal-btn" onClick={this.props.close}>×</span>
                </div>
                <div className="modal-body2">
                    {!wantiFrame && <div className="floating-label">
                        <input className="floating-input" type="text" placeholder=" " value={this.state.groupName}
                               onChange={(event) => {
                                   this.setState({username: event.target.value});
                               }}/>
                        <span className="highlight"></span>
                        <label>Your Name</label>
                    </div>}
                    {wantiFrame && <Iframe url={url}
                                           width="1100vw"
                                           height="500vh"
                                           id="myId"
                                           className="myClassname"
                                           position="relative"
                                           allowFullScreen={true}
                                           allow={"fullscreen"}
                    />

                    }
                </div>
                <div className="modal-footer2">
                    <button className="btn-cancel" onClick={this.props.close}>CLOSE</button>
                    <button className="btn-continue"
                            onClick={() => this.props.submitName(this.state.username)}>CONTINUE
                    </button>
                </div>

            </div>
        );
    }
}

export default UsernameModel;