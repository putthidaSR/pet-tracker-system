/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, ActivityIndicator, Alert, TextInput, SafeAreaView, Dimensions, Text, View } from "react-native";
import {REQUEST_URLS} from '../../Configuration';
import { Button } from 'react-native-elements';
import axios from 'axios';

/**
 * This class renders the component to register user with pet's owner role.
 * Pet's owner will then receive text message with confirmation code once the 
 * veterinarian has successfully register them to the system.
 *
 * @author Putthida Samrith
 * @date 12/9/2021
 */
export default class UserRegistration extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      username: '',
      email: '',
      phoneNumber: '',
      address: '',
      isLoading: false // flag to indicate whether the screen is still loading
    };
  }
  
  /**
   * Add pet's owner to the database.
   */
  handleUserRegistration = async () => {

    this.setState({isLoading: true});

    await axios({
      url: REQUEST_URLS.ADD_PET_OWNER,
      method: 'POST',
      data: {
        loginName: this.state.username,
        loginPassword: '123456!', // random value (password will be overriden by user later)
        email: this.state.email,
        address: this.state.address,
        phoneNumber: this.state.phoneNumber
      }
    })
      .then(() => {
        this.setState({isLoading: false});

        Alert.alert('Success', this.state.username + ' has been registered with the Paw Tracker system. User will receive a text message with instructions to setup an account.');
        this.props.navigation.goBack();
      })
      .catch((error) => {
        this.setState({isLoading: false});
        Alert.alert('Error', error);
        this.props.navigation.navigate('Homepage');
      });
  }

  /*****************************************************
   * Render the form to fill in
  *****************************************************/
  renderInputForm() {
    return (
      <View style={styles.formContainer}>
        
        <View style={{padding: 20}} />
        <Text style={styles.fieldTitleText}>Name<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(username) => this.setState({username})}
          placeholder = "Enter username"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({username: ''})}
          underlineColorAndroid = "#fff"
        />

        <View style={{padding: 5}} />
        <Text style={styles.fieldTitleText}>Email<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(email) => this.setState({email})}
          placeholder = "Enter email"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({email: ''})}
          underlineColorAndroid = "#fff"
        />

        <View style={{padding: 5}} />
        <Text style={styles.fieldTitleText}>Phone Number<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(phoneNumber) => this.setState({phoneNumber})}
          placeholder = "Enter phone number"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({phoneNumber: ''})}
          underlineColorAndroid = "#fff"
        />

        <View style={{padding: 5}} />
        <Text style={styles.fieldTitleText}>Address<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(address) => this.setState({address})}
          placeholder = "Enter address"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({addres: ''})}
          underlineColorAndroid = "#fff"
        />

      </View>
    );
  }

  /*****************************************************
   * Render button that will add the pet to mobile phone
  *****************************************************/
  renderRegisterButton() {
    return (
      <View style={{}}>
        <Button type="solid" title=" Register "
          titleStyle={{fontSize: 15, fontWeight: 'bold'}}
          containerStyle={{width: (Dimensions.get('window').width) - 50, alignSelf: 'center', marginTop: 20}}
          buttonStyle={{
            borderWidth: 5,
            borderColor: '#F5C945',
            borderRadius:20,
            backgroundColor: '#0F2F44',
            height: 50
          }}
          onPress={this.handleUserRegistration} 
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
          <Text style={{textAlign: 'center'}}>{'\n'}Please wait...</Text>
        </View>
      );
    }

    return (
      <SafeAreaView style={styles.container}>
        <View style={{alignSelf: 'center', backgroundColor: '#F5C945', position: 'absolute', top: 0, left: 0, 
          width: Dimensions.get('window').width, height: 50}} />

        {this.renderInputForm()}

        {this.renderRegisterButton()}

      </SafeAreaView>
    );
  }

}
	
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center"
  },
  formContainer: {
    //backgroundColor: '#EAF1FF',
    alignSelf: 'center',
    width: Dimensions.get('window').width - 40,
    borderRadius: 30,
    borderColor: '#EAF1FF', 
    borderWidth: 5
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
  }
});

