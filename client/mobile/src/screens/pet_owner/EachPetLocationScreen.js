/* eslint-disable react/no-direct-mutation-state */
/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, Alert, Text, View, ActivityIndicator } from "react-native";
import MapView, { PROVIDER_GOOGLE, Marker, Callout, Polygon } from 'react-native-maps';
import Geolocation from 'react-native-geolocation-service';
import moment from 'moment';
import {REQUEST_URLS} from '../../Configuration';
import axios from 'axios';

const LATITUDE_DELTA = 0.09;
const LONGITUDE_DELTA = 0.035;
const LATITUDE = 47.244839;
const LONGITUDE = -122.437828;

/**
 * This component renders the screen to view the last 10 locations of the specified pet.
 * Google map is implemented to display the markers that represent pet's location.
 * 
 * @author Putthida Samrith
 * @date 12/9/2021
 */
export default class PetLocationScreen extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      petId: this.props.route.params.petId,

      // hard-code initial location values (for testing purpose)
      initialRegion: {
        latitude: 47.244839,
        longitude: -122.437828,
        latitudeDelta: 0.0922,
        longitudeDelta: 0.0421
      }, 
      currentLatitude: 47.244839,
      currentLongitude: -122.437828,
      markers: [],

      // default values (for testing purpose)
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

  /**
   * Get initial data
   */
  async componentDidMount() {
    await this.getCurrentDeviceLocation();
  }

  /**
   * Called immediately before a component is destroyed. This method is to perform any necessary cleanup.
   */
  componentWillUnmount() {
    this.watchID != null && Geolocation.clearWatch(this.watchID);
  }

  /**
   * Get current device's location (latitude, longitude)
   */
  async getCurrentDeviceLocation() {
    // Initial the Map view with device's current location
    this.setState({isLoading: true});

    Geolocation.getCurrentPosition(
      async (position) => {
        console.log('Current position: ' + JSON.stringify(position.coords));

        this.setState({ 
          currentLatitude: parseFloat(position.coords.latitude),
          currentLongitude: parseFloat(position.coords.longitude)});

        this.setState({isLoading: false});

        // After getting current geolocation, attempt to call server to get locations data
        await this.getPetLocationData();
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

  /**
   * Fetch the last 10 locations of the pets with the specified pet ID.
   */
  getPetLocationData = async() => {

    this.setState({isLoading: true});

    // Get current locations of all pets that belong to the specified user
    await axios.get(REQUEST_URLS.GET_ALL_LOCATIONS_FOR_PET_ID + '/' + this.state.petId, {
      headers: {
        'limit': 10
      }
    })
      .then(async(response) => {
        const data = response.data.results;
        console.log(data);
        // Convert returned results into list of objects to be used to display as marker on map
        var newLocationList = [];
        for(var i = 0; i < data.length; i++) {

          // Call nominatim library to convert geolocation to address
          const responseAddress = await axios.get('https://nominatim.openstreetmap.org/reverse?', {
            params: { 
              lat: data[i].latitude, lon: data[i].longitude, format: 'json'
            }});
          var fullAddress = responseAddress.data.display_name;
          
          // Get current locations of all pets that belong to the specified user
          const objectToAdd = {
            name: data[i].petName,
            latitude: data[i].latitude,
            longitude: data[i].longitude,
            address: fullAddress,
            latestUpdate: data[i].lastSeenDate
          };
          newLocationList.push(objectToAdd);
        }
        this.setState({isLoading: false, coordinates: newLocationList});

        this.setState({initialRegion: {
          latitude: newLocationList[0].latitude,
          longitude: newLocationList[0].longitude,
          latitudeDelta: 0.0922,
          longitudeDelta: 0.0421
        }});
      })
      .catch((error) => {
        this.setState({isLoading: false});
        Alert.alert('Error', error.message);
        this.props.navigation.goBack();
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

  }

  /**
   * Helper method to initialize default location.
   */
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
          <Text style={{textAlign: 'center'}}>{'\n'}Getting location... {'\n'}Please wait...</Text>
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
          
          {/** Display marker on the map to represent pet's location (based on latitude and longitude) */}
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

                {/** Display the card when user presses on marker on the map */}
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

