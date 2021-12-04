/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { SafeAreaView, Dimensions, StyleSheet, Text, TextInput, View, ActivityIndicator, Alert } from "react-native";
import { Button } from 'react-native-elements';
import axios from 'axios';
import FormData from 'form-data';

/**
 * This class represents the form register a new pet.
 * This component is only for authenticated user.
 */
export default class PetRegistrationScreen extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      petId: '',
      petName: '',
      petConfirmationNumber: '',
      isLoading: false // flag to indicate whether the screen is still loading
    };

    this.registerPet = this.registerPet.bind(this);
  }
  
  /*****************************************************
   * Trigger the endpoint to call the backend server to
   * add a pet to mobile device
  *****************************************************/
  async registerPet() {

    const data = new FormData();
    data.append('petId', this.state.petId);
    data.append('petName', this.state.petName);
    data.append('petConfirmationNumber', this.state.petConfirmationNumber);   

    await axios({
      url: '',
      method: 'POST',
      data: data
    })
      .then(() => {
        this.setState({isLoading: false});

        Alert.alert('Success', this.state.petName + ' has successfully added to the app.');
        this.props.navigation.navigate('Home');
      })
      .catch((error) => {
        this.setState({isLoading: false});
        Alert.alert('Error', error);
        this.props.navigation.navigate('Home');
      });
  }

  /*****************************************************
   * Render the form to fill in
  *****************************************************/
  renderInputForm() {
    return (
      <View style={styles.formContainer}>

        <View style={{padding: 20}} />
        <Text style={styles.fieldTitleText}>Pet RFID Tag Number<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(petId) => this.setState({petId})}
          placeholder = "Enter Pet RFID Tag Number"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({username: ''})}
          underlineColorAndroid = "#fff"
        />

        <View style={{padding: 3}} />
        <Text style={styles.fieldTitleText}>Pet Confirmation Number<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(petId) => this.setState({petId})}
          placeholder = "Enter the Confirmation Number"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({username: ''})}
          underlineColorAndroid = "#fff"
        />

        <View style={{padding: 3}} />
        <Text style={styles.fieldTitleText}>Pet Name<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(petId) => this.setState({petId})}
          placeholder = "Enter Your Pet Name"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({username: ''})}
          underlineColorAndroid = "#fff"
        />
      </View>
    );
  }

  /*****************************************************
   * Render button that will add the pet to mobile phone
  *****************************************************/
  renderBottomView() {
    return (
      <View style={{}}>
        <Button type="solid" title=" Register My Pet "
          titleStyle={{fontSize: 15, fontWeight: 'bold'}}
          containerStyle={{width: (Dimensions.get('window').width) - 50, alignSelf: 'center', marginTop: 20}}
          buttonStyle={{
            borderWidth: 5,
            borderColor: '#F5C945',
            borderRadius:20,
            backgroundColor: '#0F2F44',
            height: 50
          }}
          onPress={this.registerPet} 
        />
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

        <View style={{alignSelf: 'center', backgroundColor: '#F5C945', position: 'absolute', top: 0, left: 0, 
          width: Dimensions.get('window').width, height: 300}} />

        {this.renderInputForm()}

        {this.renderBottomView()}

      </SafeAreaView>
    );
  }

}
	
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff"
  },
  fieldTitleText: {
    color: '#0F2F44', 
    fontWeight: 'bold', 
    paddingLeft: 20,
    paddingBottom: 5,
    textAlign: 'left'
  },
  input: {
    alignSelf: 'center',
    width: Dimensions.get('window').width - 80,
    height: 40,
    backgroundColor: '#fff',
    marginBottom: 20,
    color: '#0F2F44',
    borderWidth: 2,
    borderColor: 'gray',
    paddingHorizontal: 20,
    borderRadius: 10
  },
  formContainer: {
    backgroundColor: 'white',
    alignSelf: 'center',
    width: Dimensions.get('window').width - 40,
    height: Dimensions.get('window').height / 2.2,
    borderRadius: 35,
    borderColor: '#fff', 
    borderWidth: 5,
    shadowOffset: {
      width: 5,
      height: 10
    },
    shadowColor: 'rgba(0,0,0,1)',
    shadowOpacity: 0.73,
    shadowRadius: 15,
    elevation: 35,
    marginTop: 60
  }
});

