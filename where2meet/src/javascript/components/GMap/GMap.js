import React from "react";
import ReactDOM from "react-dom";
import { compose, withProps } from "recompose";
import {
    withScriptjs,
    withGoogleMap,
    GoogleMap,
    Marker
} from "react-google-maps";

const MapComponent = compose(
    withProps({
        googleMapURL:
            "https://maps.googleapis.com/maps/api/js?key=AIzaSyCj4HG2u7yGcDfeRgnjUvExu09hDGEMhgU&v=3.exp&libraries=geometry,drawing,places",
        loadingElement: <div style={{ height: `100%` }} />,
        containerElement: <div style={{ height: `400px` }} />,
        mapElement: <div style={{ height: `100%` }} />
    }),
    withScriptjs,
    withGoogleMap
)(props => (
    <GoogleMap defaultZoom={8} defaultCenter={{ lat: -34.397, lng: 150.644 }}>
        {props.isMarkerShown && (
            <Marker position={{ lat: -34.397, lng: 150.644 }} />
        )}
    </GoogleMap>
));

//ReactDOM.render(<MyMapComponent isMarkerShown />, document.getElementById("root"));
export default MapComponent;







// import React, { Component } from 'react';
// import GoogleMapReact from 'google-map-react';

// const AnyReactComponent = ({ text }) => <div>{text}</div>;

// class GMap extends Component {
//     static defaultProps = {
//         center: {
//             lat: 41.825684,
//             lng: -71.402438
//         },
//         zoom: 11
//     };

//     constructor() {
//         super();

//     }

//     render() {
//         return (
//             // Important! Always set the container height explicitly
//             <div style={{ height: '100%', width: '100%' }}>
//                 <GoogleMapReact
//                     //bootstrapURLKeys={{ key: /* YOUR KEY HERE */ }}
//                     defaultCenter={this.props.center}
//                     defaultZoom={this.props.zoom}
//                 >
//                     <AnyReactComponent
//                         lat={59.955413}
//                         lng={30.337844}
//                         text="My Marker"
//                     />
//                 </GoogleMapReact>
//             </div>
//         );
//     }
// }

// export default GMap;
