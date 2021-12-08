/* eslint-disable react/no-direct-mutation-state */
/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, Alert, Text, View, ActivityIndicator, Image, TouchableHighlight, Dimensions } from "react-native";
import MapView, { PROVIDER_GOOGLE, Marker, Callout } from 'react-native-maps';
import Geolocation from 'react-native-geolocation-service';
import NavigateBetweenTwoRoutes from '../../components/NavigateBetweenTwoRoutes';
import moment from 'moment';
import Carousel, { Pagination } from 'react-native-snap-carousel';
import {REQUEST_URLS, USER_ID_KEY_STORAGE} from '../../Configuration';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const LATITUDE_DELTA = 0.09;
const LONGITUDE_DELTA = 0.035;
const LATITUDE = 47.244839;
const LONGITUDE = -122.437828;

export default class PetLocationScreen extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      userId: 0,
      initialRegion: {
        latitude: 47.244839,
        longitude: -122.437828,
        latitudeDelta: 0.0922,
        longitudeDelta: 0.0421
      }, 
      currentLatitude: 47.244839,
      currentLongitude: -122.437828,
      markers: [],
      // coordinates: [
      //   { name: 'Fluffy', address: '1900 Commerce St, Tacoma, WA 98402', latitude: 47.244839, longitude: -122.437828, latestUpdate: '2021-11-29 07:56:09'},
      //   { name: 'Chance', address: '15 Cook St, Tacoma, WA 98402, USA', latitude: 47.244821, longitude: -122.437257, latestUpdate: '2021-11-29 07:56:09'},
      //   { name: 'Bella', address: '1965 Polk St, Tacoma, WA 94109, USA', latitude: 47.244316, longitude: -122.436741, latestUpdate: '2021-11-29 07:56:09'},
      //   { name: 'Bear', address: '110 Shotwell St, Tacoma, WA 98402, USA', latitude: 47.243977, longitude: -122.436660, latestUpdate: '2021-11-29 07:56:09'},
      //   { name: 'Violet', address: '3515 Webster St, Tacoma, WA 98402, USA', latitude: 47.244280, longitude: -122.437332, latestUpdate: '2021-11-29 07:56:09'}
      // ],
      coordinates: [],
      selectedIndex: 0,
      activeSlide: 0,
      isLoading: false // flag to indicate whether the screen is still loading
    };

    this.getCurrentDeviceLocation = this.getCurrentDeviceLocation.bind(this);

  }
    
  async componentDidMount() {
    await this.getCurrentDeviceLocation();
  }

  componentWillUnmount() {
    this.watchID != null && Geolocation.clearWatch(this.watchID);
  }

  /***************************************************************
   * Get the info of the current logged-in user from app cache
   ****************************************************************/
  getCurrentUser = async () => {
    try {
  
      this.setState({isLoading: true});
  
      // Retrieve user ID from app cache if exists
      const userIdFromCache = await AsyncStorage.getItem(USER_ID_KEY_STORAGE);
      if (userIdFromCache !== null) {
        this.setState({userId: userIdFromCache});
      } else if (typeof this.props.route.params !== "undefined") {
        if (this.props.route.params.id !== null) {
          this.setState({userId: this.props.route.params.id});
        }
      } else {
        // Prompt user to login again
        this.props.navigation.navigate('SignInScreen');
        return;
      }
  
      this.setState({isLoading: false});
  
    } catch (error) {
      this.setState({isLoading: false});
    }
  }

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

  getPetLocationData = async() => {

    // Get user id from app cache
    await this.getCurrentUser();
    console.log(this.state.userId);

    this.setState({isLoading: true});

    // Get current locations of all pets that belong to the specified user
    await axios.get(REQUEST_URLS.GET_CURRENT_LOCATIONS + '/' + this.state.userId)
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

          const objectToAdd = {
            name: data[i].petName,
            latitude: data[i].latitude,
            longitude: data[i].longitude,
            address: fullAddress,
            latestUpdate: data[i].lastSeenDate,
            weather: data[i].weather,
            iconLink: data[i].iconLink
          };
          newLocationList.push(objectToAdd);
        }
        this.setState({isLoading: false, coordinates: newLocationList});
        console.log(this.state.coordinates);
      })
      .catch((error) => {
        this.setState({isLoading: false});
        console.log(error.response);
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

  onMarkerPressed = (location, index) => {
    this._map.animateToRegion({
      latitude: location.latitude,
      longitude: location.longitude,
      latitudeDelta: 0.005,
      longitudeDelta: 0.005
    });

    this._carousel.snapToItem(index);
  }

  onCarouselItemChange = (index) => {
    let location = this.state.coordinates[index];
    this.setState({activeSlide: index});

    this._map.animateToRegion({
      latitude: location.latitude,
      longitude: location.longitude,
      latitudeDelta: 0.005,
      longitudeDelta: 0.005
    });

    this.state.markers[index].showCallout();
  }

  goToInitialLocation() {
    let initialRegion = Object.assign({}, this.state.initialRegion);
    initialRegion["latitudeDelta"] = 0.005;
    initialRegion["longitudeDelta"] = 0.005;
    this._map.animateToRegion(initialRegion, 2000);
  }

  renderCarouselItem = ({ item }) => {
    //console.log('valueeee', item, this.state.currentLatitude);
    return (
      <View style={styles.cardContainer}>

        <View style={{justifyContent: 'center', alignItems: 'center'}}>

          <Text style={{textAlign: 'center', fontWeight: 'bold', fontSize: 18, paddingVertical: 10}}>{item.name}</Text>

          <Image style={{width: 40, height: 40}} source={require('./../../assets/images/paw.png')} />
        </View>

        <View style={{alignItems: 'center', paddingTop: 5}}>
          <TouchableHighlight
            onPress={() => NavigateBetweenTwoRoutes.handleGetDirections(
              this.state.currentLatitude, 
              this.state.currentLongitude, 
              item.latitude, 
              item.longitude, 
              'driving')}
          >

            <View style={styles.directionButton}>
              <Text style={{textAlign: 'center', color: 'white'}}>Get Direction</Text>
            </View>
          </TouchableHighlight></View>    
      </View>
    );
  }

  /**
   * Render pagination below carousel container.
   */
  get pagination () {
    return (
      <Pagination
        dotsLength={this.state.coordinates.length}
        activeDotIndex={this.state.activeSlide}
        containerStyle={{ backgroundColor: 'rgba(255, 255, 255, 0.6)', paddingVertical: 10, marginTop: 10, paddingHorizontal: 10, borderRadius: 15, alignItems: 'center' }}
        dotColor={'rgba(0, 0, 0, 0.92)'}
        dotStyle={{
          width: 15,
          height: 15,
          borderRadius: 10,
          marginHorizontal: 8,
          backgroundColor: 'rgba(0, 0, 0, 0.92)'
        }}
        inactiveDotOpacity={0.4}
        inactiveDotScale={0.6}
        inactiveDotColor={'black'}
      />
    );
  }

  updateIndex = (selectedIndex) => {
    this.setState({selectedIndex});
  }

  render() {
    
    // Display the spinning wheel to show that the app is still loading
    if (this.state.isLoading) {
      return (
        <View style={{ flex: 1, justifyContent: 'center' }}>
          <ActivityIndicator size="large" color="#0000ff" />
          <Text style={{textAlign: 'center'}}>{'\n'}Getting location...{'\n'} Please wait...</Text>
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
          <Marker
            draggable
            coordinate={{ latitude: 47.244316, longitude: -122.437031}}
            title="You are here!"
          />
          
          {this.state.coordinates.map((marker, index) => {
            //console.log(marker, index);
            return (
              <Marker 
                key={index} 
                ref={ref => {this.state.markers[index] = ref; }}
                onPress={() => this.onMarkerPressed(marker, index)}
                coordinate={{ latitude: marker.latitude, longitude: marker.longitude }}
                image={require('./../../assets/images/paw.png')}
              >
                <Callout>
                  <View style={{width: 300, height: 300}}>

                    <Text style={{textAlign: 'center', fontWeight: 'bold', fontSize: 18, paddingVertical: 10}}>{marker.name}</Text>

                    <Text style={{fontWeight: 'bold'}}>Latest Location Seen:</Text>
                    <Text>{marker.address}{'\n'}</Text>

                    <Text style={{fontWeight: 'bold'}}>Time Seen:</Text>
                    <Text>{moment(marker.latestUpdate).format('MMMM D, YYYY, HH:mm A')}{'\n'}</Text>

                    <Text style={{fontWeight: 'bold'}}>Weather Condition:</Text>
                    <Text>{marker.weather}</Text>
                    
                    <View style={{justifyContent: 'center'}}>
                      <Image
                        style={{width: 100, height: 100}}
                        source={{uri: 'http:' + marker.iconLink}}
                      />
                    </View>
                  </View>
                </Callout>

              </Marker>
            );
          })}
        </MapView>

        <Carousel
          ref={(c) => { this._carousel = c; }}
          layout={'default'}
          layoutCardOffset={'5'}
          data={this.state.coordinates}
          containerCustomStyle={styles.carousel}
          renderItem={this.renderCarouselItem}
          sliderWidth={Dimensions.get('window').width}
          itemWidth={180}
          removeClippedSubviews={false}
          onSnapToItem={(index) => this.onCarouselItemChange(index)}
        />

        <View style={{alignSelf: 'center', bottom: 0, position: 'absolute', marginBottom: 10, marginTop: 20, width: Dimensions.get('window').width - 100}}>
          {this.pagination}
        </View>
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
  },
  directionButton: {
    marginTop: 15,
    width: 110,
    height: 30,
    alignItems: 'center',
    backgroundColor: '#0F2F44',
    justifyContent: 'center',
    borderRadius: 5
  },
  carousel: {
    position: 'absolute',
    bottom: 0,
    marginBottom: 45
  },
  cardContainer: {
    backgroundColor: '#fff',
    height: 220,
    width: 180,
    padding: 5,
    borderRadius: 24,
    borderWidth: 1
  }
});

