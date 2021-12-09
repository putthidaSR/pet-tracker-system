/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, SafeAreaView, Text, ScrollView, Image, View, Alert, Dimensions, ActivityIndicator } from "react-native";
import axios from 'axios';
import {USER_ID_KEY_STORAGE, REQUEST_URLS} from '../../Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Card, Button } from 'react-native-elements';

/**
 * This component renders the screen to display the list of all pets that the logged-in user owns.
 * User will have abilities to view medical record, vaccination record, pet's RFID tag detail and 
 * view the last 10 locations of the specified pet.
 * 
 * @author Putthida Samrith
 * @date 12/9/2021
 */
export default class ViewMyPetsScreen extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      userId: 0,
      username: '',
      petDataList: [],
      vaccinationRecordsList: [],
      medicalRecordsList: [],
      isLoading: false // flag to indicate whether the screen is still loading
    };
  }
  
  /**
   * Get initial data
   */
  async componentDidMount() {
    await this.getUserData();
    await this.getAllPetsList();
  }

  /***************************************************************
   * Get the user ID of the current logged-in user
  ****************************************************************/
  getUserData = async () => {
    try {
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
    } catch (error) {
      console.log('Error getting username', error);
    }
  }

  /***************************************************************
   * Get the initial summary data of each pet that belong to the user.
   * This is the first action to be rendered when the component is mounted.
  ****************************************************************/
  getAllPetsList = async() => {

    console.log('Attempt to send request to get list of pets belong to a user');
    const URL = REQUEST_URLS.VIEW_PETS_BY_USER + '/' + this.state.userId;
    console.log('Request URL', URL);

    this.setState({isLoading: true});
  
    await axios({
      url: URL,
      method: 'GET'
    })
      .then((response) => {
        //console.log(response.data);

        let newTableData = [];
        var serverData = response.data.results;

        // loop through server response data to construct array of objects
        serverData.forEach((element, index) => {
          console.log(index, element);

          // objects to store (will be used later in the app)
          const objectToAdd = {
            petName: element.petName,
            petId: element.petId,
            rfidNumber: element.rfidNumber,
            rfidStatus: element.rfidStatus
          };

          // add object to array
          newTableData.push(objectToAdd);
        });

        this.setState({
          petDataList: newTableData,
          isLoading: false
        });

      })
      .catch((error) => {
        this.setState({isLoading: false});
        Alert.alert('Error', error.message);
        this.props.navigation.navigate('Homepage');
      });
  }

  /***************************************************************
   * Render the card list of all pets in summary
  ****************************************************************/
  renderAllPetsList() {

    return (
      <View>
        {
          this.state.petDataList.map((data, index) => (
            <Card
              title={data.petName}
              key={index}
              containerStyle={{width: Dimensions.get('window').width - 40, borderRadius: 30}}
            >
      
              {/* display pet name */}
              <View style={{flexDirection: 'row', width: Dimensions.get('window').width - 100}}>
                <Image style={{width: 50, height: 50}} source={require('./../../assets/images/paw.gif')} />
                <Text style={{fontSize: 18, textAlign: 'center', fontWeight: 'bold', marginTop: 10}}>{data.petName}</Text>
              </View>

              <View style={{paddingVertical: 3}}>
                <Text style={{fontWeight: 'bold'}}>RFID Number: <Text>{data.rfidNumber}</Text></Text>
                <Text style={{fontWeight: 'bold'}}>RFID Status: {data.rfidStatus ? 'Active' : 'Inactive'}</Text>
              </View>
              <View style={{padding: 3}}/>


              {/* display latest locations button only if RFID is active */}
              {
                data.rfidStatus &&
                  <Button
                    containerStyle={{padding: 5}}
                    titleStyle={{fontSize: 14, fontWeight: 'bold'}}
                    buttonStyle={{backgroundColor: '#0F2F44', borderRadius: 20, marginLeft: 0, marginRight: 0, marginBottom: 0}}
                    title="View Location History"
                    onPress={() => this.props.navigation.navigate('EachPetLocation', {
                      petId: data.petId
                    })}
                  />
              }
            </Card>
              
          ))
        }
      </View>
    );
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
      <SafeAreaView style={styles.container}>

        <View style={styles.listViewContainer}>
          <ScrollView style={styles.scrollViewContainer} contentContainerStyle={{height: Dimensions.get('window').height * 2}}>
            {this.renderAllPetsList()}
          </ScrollView>
        </View>


      </SafeAreaView>
    );
  }

}
	
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#F5C945"
  },
  scrollViewContainer: {
    alignSelf: 'center',
    marginTop: '15%',
    marginBottom: '15%'
  },
  listViewContainer: {
    justifyContent: 'center',
    height: Dimensions.get('window').height,
    width: Dimensions.get('window').width,
    alignSelf: 'center'
  }
});

