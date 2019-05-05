import React from "react";
import ReactDOM from "react-dom";
import { compose, withProps } from "recompose";
import {
    withScriptjs,
    withGoogleMap,
    GoogleMap,
    Marker,
    Circle
} from "react-google-maps";

const MapComponent = compose(
    withProps({
        googleMapURL:
            "https://maps.googleapis.com/maps/api/js?key=AIzaSyCj4HG2u7yGcDfeRgnjUvExu09hDGEMhgU&v=3.exp&libraries=geometry,drawing,places",
        loadingElement: <div style={{ height: `100%` }} />,
        containerElement: <div style={{ height: `100%` }} />,
        mapElement: <div style={{ height: `100%` }} />
    }),
    withScriptjs,
    withGoogleMap
)(props => (
    < GoogleMap defaultZoom={props.zoom} defaultCenter={{ lat: props.lat, lng: props.lon }}>
        {props.markers.map(m => (<Marker key={m.id}
            position={{ lat: m.lat, lng: m.lon }}
            title={m.venue}
            onClick={e => props.scrollTo(m.venue + ":" + m.id)} />))}
        <Circle
            center={{ lat: props.lat, lng: props.lon }}
            visible={true}
            radius={10}
        />
    </GoogleMap >
));

export default MapComponent;
