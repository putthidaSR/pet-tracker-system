import { Component } from 'react';
import getDirections from 'react-native-google-maps-directions';

// Travel mode that Google Maps support
export const TRAVEL_MODE = {WALKING: 'walking', BICYCLING: 'bicycling', TRANSIT: 'transit'};

/**
 * This class renders the component to open default map app on the device 
 * and navigate from one location to another based on the specified travel mode.
 */
export default class NavigateBackToLocation extends Component {
  
  static handleGetDirections(sourceLat, sourceLong, destinationLat, destinationLong, travelMode) {
    //console.log('handleGetDirections', sourceLat, sourceLong, destinationLat, destinationLong, travelMode);
    const data = {
      source: {
        latitude: sourceLat,
        longitude: sourceLong
      },
      destination: {
        latitude: destinationLat,
        longitude: destinationLong
      },
      params: [
        {
          key: 'travelmode',
          value: travelMode // may be "walking", "bicycling" or "transit" as well
        }
      ]
    };

    getDirections(data);
  }

  

}
