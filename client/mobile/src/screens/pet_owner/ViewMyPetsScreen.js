/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, SafeAreaView, Text, ScrollView, Image, View, Alert, Dimensions, ActivityIndicator } from "react-native";
import axios from 'axios';
import {USER_KEY_STORAGE} from '../../Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Card, Button } from 'react-native-elements';
import moment from 'moment';

export default class ViewMyPetsScreen extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      username: '',
      totalPetsSaved: 0,
      petDataList: [
        {
          id: 1,
          petName:'Chance',
          latestUpdate: '2021-11-29 07:56:09'
        },
        {
          id: 2,
          petName:'Fluffy',
          latestUpdate: '2021-11-29 07:56:09'
        }
      ],
      isLoading: false // flag to indicate whether the screen is still loading
    };
  }
  
  componentDidMount() {
    this.getUsername();
  }

  /***************************************************************
   * Get the username of the current logged-in user
  ****************************************************************/
  getUsername = async () => {
    try {
      const value = await AsyncStorage.getItem(USER_KEY_STORAGE);
      if (value !== null) {
        this.setState({username: value});
        this.getAllPetsList();
      } else {
        console.log('No user found');
      } 
    } catch (error) {
      console.log('Error getting username', error);
    }
  }

  /***************************************************************
   * Get the initial summary data of each pet. 
   * This is the first action to be rendered when the component is mounted.
  ****************************************************************/
  getAllPetsList = async() => {

    console.log('Attempt to send request to get list of pets belong to a user');
    const URL = '' + this.state.username;
    console.log('Request URL', URL);
    this.setState({isLoading: true});
  
    try {
      const response = await axios.get(URL);
      //console.log(response.data);
  
      this.setState({isLoading: false});
  
      if (response.data.status === 200) {
        this.setState({
          petDataList: response.data.response,
          totalPetsSaved: response.data.response.length
        });
      }
    } catch (error) {
      console.log('Error getting list of pets owned', error);
      Alert.alert('Error getting list of pets', error);
      this.props.navigation.navigate('HomepageScreen');
    }
  }

  /***************************************************************
   * Render the card list of all pets in summary (no detail)
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
              
              <View style={{flexDirection: 'row', width: Dimensions.get('window').width - 100}}>
                
                {/* display vaccination record button */}
                <Button
                  containerStyle={{padding: 5, width: 160}}
                  titleStyle={{fontSize: 13, fontWeight: 'bold'}}
                  buttonStyle={{backgroundColor: 'green', borderRadius: 20, marginLeft: 0, marginRight: 0, marginBottom: 0}}
                  title="Medical Record" 
                  onPress={() => {
                  }}
                />
  
                {/* display medical record button */}
                <Button
                  containerStyle={{padding: 5, width: 160}}
                  titleStyle={{fontSize: 13, fontWeight: 'bold'}}
                  buttonStyle={{backgroundColor: 'red', borderRadius: 20, marginRight: 0, marginBottom: 0}}
                  title="Vaccination Record"
                  onPress={() => {
                  }}
                />
              </View>

              {/* display latest locations button */}
              <Button
                containerStyle={{padding: 5}}
                titleStyle={{fontSize: 14, fontWeight: 'bold'}}
                buttonStyle={{backgroundColor: '#0F2F44', borderRadius: 20, marginLeft: 0, marginRight: 0, marginBottom: 0}}
                title="View Location History"
                onPress={() => this.props.navigation.navigate('EachPetLocation', {
                  petId: data.id
                })}
              />
  
              <Text style={{fontSize: 12, textAlign: 'right', padding: 5, marginTop: 5}}>Last Updated: {moment(data.latestUpdate).format('MMMM D, YYYY, HH:mm A')}</Text>
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

