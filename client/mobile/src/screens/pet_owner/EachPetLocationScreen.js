/* eslint-disable react/no-direct-mutation-state */
/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, Alert, Text, View, ActivityIndicator } from "react-native";
import MapView, { PROVIDER_GOOGLE, Marker, Callout, Polygon } from 'react-native-maps';
import Geolocation from 'react-native-geolocation-service';
import moment from 'moment';

const LATITUDE_DELTA = 0.09;
const LONGITUDE_DELTA = 0.035;
const LATITUDE = 47.244839;
const LONGITUDE = -122.437828;

export default class PetLocationScreen extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      petId: this.props.route.params.petId,
      initialRegion: {
        latitude: 47.244839,
        longitude: -122.437828,
        latitudeDelta: 0.0922,
        longitudeDelta: 0.0421
      }, 
      currentLatitude: 47.244839,
      currentLongitude: -122.437828,
      markers: [],
      coordinates: [
        { address: '1900 Commerce St, Tacoma, WA 98402', latitude: 47.244839, longitude: -122.437828, latestUpdate: '2021-11-29 07:56:09'},
        { address: '15 Cook St, Tacoma, WA 98402, USA', latitude: 47.244821, longitude: -122.437257, latestUpdate: '2021-11-29 07:56:09'},
        { address: '1965 Polk St, Tacoma, WA 94109, USA', latitude: 47.244316, longitude: -122.436741, latestUpdate: '2021-11-29 07:56:09'},
        { address: '110 Shotwell St, Tacoma, WA 98402, USA', latitude: 47.243977, longitude: -122.436660, latestUpdate: '2021-11-29 07:56:09'},
        { address: '3515 Webster St, Tacoma, WA 98402, USA', latitude: 47.244280, longitude: -122.437332, latestUpdate: '2021-11-29 07:56:09'}
      ],
      isLoading: false // flag to indicate whether the screen is still loading
    };

    this.getCurrentDeviceLocation = this.getCurrentDeviceLocation.bind(this);

  }
    
  componentDidMount() {
    //this.getCurrentDeviceLocation();
  }

  componentWillUnmount() {
    this.watchID != null && Geolocation.clearWatch(this.watchID);
  }

  async getCurrentDeviceLocation() {
    // Initial the Map view with device's current location
    this.setState({isLoading: true});

    Geolocation.getCurrentPosition(
      (position) => {
        console.log('Current position: ' + JSON.stringify(position.coords));

        this.setState({ 
          currentLatitude: parseFloat(position.coords.latitude),
          currentLongitude: parseFloat(position.coords.longitude)});

        let region = {
          latitude: parseFloat(position.coords.latitude),
          longitude: parseFloat(position.coords.longitude),
          latitudeDelta: 5,
          longitudeDelta: 5
        };
        this.setState({initialRegion: region});
      },
      error => {
        // eslint-disable-next-line no-console
        console.error('Error getting current geolocation', JSON.stringify(error));
        this.setState({isLoading: false});

        Alert.alert('GPS Error', 'Sorry, there is a GPS error while locating your current location. Please try again later.');
        this.props.navigation.navigate('Home');
        return;
      },
      {enableHighAccuracy: true, timeout: 10 * 1000, maximumAge: 1000}
    );

    this.watchID = Geolocation.watchPosition(position => {
      const lastPosition = JSON.stringify(position);
      this.setState({lastPosition});
    });
  }

  getMapRegion = () => ({
    latitude: LATITUDE,
    longitude: LONGITUDE,
    latitudeDelta: LATITUDE_DELTA,
    longitudeDelta: LONGITUDE_DELTA
  });

  onMarkerPressed = (location) => {
    this._map.animateToRegion({
      latitude: location.latitude,
      longitude: location.longitude,
      latitudeDelta: 0.005,
      longitudeDelta: 0.005
    });

    //this._carousel.snapToItem(index);
  }

  goToInitialLocation() {
    let initialRegion = Object.assign({}, this.state.initialRegion);
    initialRegion["latitudeDelta"] = 0.005;
    initialRegion["longitudeDelta"] = 0.005;
    this._map.animateToRegion(initialRegion, 2000);
  }

  render() {
    
    // Display the spinning wheel to show that the app is still loading
    if (this.state.isLoading) {
      return (
        <View style={{ flex: 1, justifyContent: 'center' }}>
          <ActivityIndicator size="large" color="#0000ff" />
          <Text style={{textAlign: 'center'}}>{'\n'}The app is loading. Please wait...</Text>
        </View>
      );
    }

    return (
      <View style={styles.container}>
        <MapView
          provider={PROVIDER_GOOGLE}
          ref={map => {this._map = map; }}
          style={styles.map}
          loadingEnabled={true}
          //initialRegion={this.getMapRegion()}
          followUserLocation={true}
          showsUserLocation={true}
          zoomEnabled={true}
          initialRegion={this.state.initialRegion}
          onMapReady={this.goToInitialLocation.bind(this)}
        >
          <Polygon
            coordinates={this.state.coordinates}
            fillColor={'yellow'}
            strokeWidth={4}
          />
          <Marker
            draggable
            coordinate={{ latitude: 47.244316, longitude: -122.437031}}
            image={require('./../../assets/images/user.png')}
            title="Your Destination"
          />
          
          {this.state.coordinates.map((marker, index) => {
            //console.log(marker, index);
            return (
              <Marker 
                key={index} 
                ref={ref => {this.state.markers[index] = ref; }}
                onPress={() => this.onMarkerPressed(marker, index)}
                coordinate={{ latitude: marker.latitude, longitude: marker.longitude }}
                image={require('./../../assets/images/pet-location.png')}
              >
                <Callout>
                  <View style={{width: 250, height: 150, padding: 5}}>

                    <Text style={{fontWeight: 'bold'}}>Location Seen:</Text>
                    <Text>{marker.address}{'\n'}</Text>

                    <Text style={{fontWeight: 'bold'}}>Time Seen:</Text>
                    <Text>{moment(marker.latestUpdate).format('MMMM D, YYYY, HH:mm A')}</Text>
                  </View>
                </Callout>

              </Marker>
            );
          })}
        </MapView>
      </View>
    );
  }

}
	
const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject
  },
  map: {
    ...StyleSheet.absoluteFillObject
  }
});

