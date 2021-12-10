/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, SafeAreaView, Text, ScrollView, Image, View, Alert, Dimensions, ActivityIndicator } from "react-native";
import axios from 'axios';
import {USER_ID_KEY_STORAGE, REQUEST_URLS} from '../../Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Card, Button, Overlay } from 'react-native-elements';

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
      showVaccinationModal: false,
      showMedicalModal: false,
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

  /**
   * Get the list of vaccination records of the specified pet.
   */
  getVaccinationRecord = async (id) => {

    console.log('Attempt to get vaccination record of pet ID: ' + id);
    this.setState({isLoading: true});
      
    await axios({
      url: REQUEST_URLS.VIEW_VACCINATION_RECORDS + '/' + id + '/vaccinations',
      method: 'GET',
      headers: {
        currentPage: `1`,
        pageSize: `10`
      }
    })
      .then((response) => {
        console.log(id, 'success!');        
        this.setState({vaccinationRecordsList: response.data});
        this.setState({showVaccinationModal: true, isLoading: false});
      })
      .catch((error) => {
        console.log(error.message);
        this.setState({isLoading: false});
        Alert.alert('', 'No vaccination record is found.');
      });
  }

  /**
   * Get the list of medical records of the specified pet.
   */
  getMedicalRecord = async (id) => {

    console.log('Attempt to get medical record of pet ID: ' + id);
    this.setState({isLoading: true});
        
    await axios({
      url: REQUEST_URLS.VIEW_MEDICAL_RECORDS + '/' + id + '/medicals',
      method: 'GET',
      headers: {
        currentPage: `1`,
        pageSize: `10`
      }
    })
      .then((response) => {
        console.log(id, 'success!');        
        this.setState({medicalRecordsList: response.data});
        this.setState({showMedicalModal: true, isLoading: false});
      })
      .catch((error) => {
        this.setState({isLoading: false});
        console.log(error.message);
        Alert.alert('', 'No medical record is found.');
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
                <Text><Text style={{fontWeight: "bold"}}>RFID Number: </Text>{data.rfidNumber}</Text>
                <Text><Text style={{fontWeight: "bold"}}>RFID Status: </Text>{data.rfidStatus ? 'Active' : 'Inactive'}</Text>
              </View>
              <View style={{padding: 3}}/>

              <View style={{flexDirection: 'row'}}>
                <Button
                  containerStyle={{padding: 5, width: 172}}
                  titleStyle={{fontSize: 15, fontWeight: 'bold'}}
                  buttonStyle={{backgroundColor: 'green', borderRadius: 20, marginLeft: 0, marginRight: 0, marginBottom: 0}}
                  title="View Vaccination Records" 
                  onPress={() => {
                    this.getVaccinationRecord(data.petId);
                  }}
                />

                <Button
                  containerStyle={{padding: 5, width: 172}}
                  titleStyle={{fontSize: 15, fontWeight: 'bold'}}
                  buttonStyle={{backgroundColor: 'red', borderRadius: 20, marginRight: 0, marginBottom: 0}}
                  title="View Medical Records" 
                  onPress={() => {
                    this.getMedicalRecord(data.petId);
                  }}
                />
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
  
  /***************************************************************
   * Render the overlay screen when View Vaccination record is clicked
  ****************************************************************/
  renderViewVaccinationModal() {
    return (
      <Overlay 
        isVisible={this.state.showVaccinationModal}
        overlayStyle={{
          width: Dimensions.get('window').width - 50,
          height: Dimensions.get('window').height - 350
        }}
        onBackdropPress={() => {this.setState({showVaccinationModal: true});}}
      >
        <View>
          <View style={{padding: 10, marginBottom: 20}}>
            <ScrollView style={{paddingBottom: 10}} contentContainerStyle={{height: Dimensions.get('window').height * 2}}>
              {
                this.state.vaccinationRecordsList.map((value, index) => {
                  return (
                    <View key={index} style={{paddingVertical: 3, marginBottom: 10, borderWidth: 1, backgroundColor: '#FFDBAC', borderRadius: 10}}>
                      <Text><Text style={{fontWeight: "bold"}}>Immunization Date: </Text>{value.immunizationDate}</Text>
                      <Text><Text style={{fontWeight: "bold"}}>Vaccination Name: </Text>{value.vaccinationName}</Text>
                      <Text><Text style={{fontWeight: "bold"}}>Veterinarian Name: </Text>{value.veterinarianName}</Text>
                      <Text><Text style={{fontWeight: "bold"}}>Veterinarian Contact: </Text>{value.veterinarianContact}</Text>
                    </View>
                  );
                })
              }
              
            </ScrollView>
          </View>

          <Button
            containerStyle={{padding: 5, width: 300, alignSelf: 'center', position: 'absolute', bottom: 0, marginBottom: 10}}
            titleStyle={{fontSize: 17, fontWeight: 'bold'}}
            buttonStyle={{borderRadius: 20, marginRight: 0, marginBottom: 0}}
            title="Close" 
            onPress={() => this.setState({showVaccinationModal: false})}                
          />

        </View>
      </Overlay>
    );
  }

  /***************************************************************
   * Render the overlay screen when View Medical record is clicked
  ****************************************************************/
  renderViewMedicalModal() {
    return (
      <Overlay 
        isVisible={this.state.showMedicalModal}
        overlayStyle={{
          width: Dimensions.get('window').width - 50,
          height: 500
        }}
        onBackdropPress={() => {this.setState({showMedicalModal: true});}}
      >
        <View>
          <View style={{padding: 10, marginBottom: 20}}>
            <ScrollView style={{paddingBottom: 10}} contentContainerStyle={{height: Dimensions.get('window').height * 2}}>
              {
                this.state.medicalRecordsList.map((value, index) => {
                  console.log(value, index);
                  return (
                    <View key={index} style={{paddingHorizontal: 5, paddingVertical: 10, marginBottom: 10, borderWidth: 1, backgroundColor: '#FFDBAC', borderRadius: 10}}>
                      <Text><Text style={{fontWeight: "bold"}}>Date Visited: </Text>{value.medicalAssignDate}</Text>
                      <Text><Text style={{fontWeight: "bold"}}>Condition: </Text>{value.medical}</Text>     
                    </View>
                  );
                })
              }
              
            </ScrollView>
          </View>

          <Button
            containerStyle={{padding: 5, width: 300, alignSelf: 'center', position: 'absolute', bottom: 0, marginBottom: 10}}
            titleStyle={{fontSize: 17, fontWeight: 'bold'}}
            buttonStyle={{borderRadius: 20, marginRight: 0, marginBottom: 0}}
            title="Close" 
            onPress={() => this.setState({showMedicalModal: false})}                
          />

        </View>
      </Overlay>
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

        {this.state.showVaccinationModal && this.renderViewVaccinationModal()}
        {this.state.showMedicalModal && this.renderViewMedicalModal()}

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

