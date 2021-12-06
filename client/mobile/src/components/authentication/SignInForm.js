/* eslint-disable react-native/no-inline-styles */
import React, { Component } from 'react'	;
import { StyleSheet, Text, Alert, TextInput, ActivityIndicator, TouchableOpacity, View, Dimensions } from 'react-native';
import axios from 'axios';
import {SERVER_IP_ADDRESS, USER_KEY_STORAGE} from '../../Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';

/**************************************************************************************
 * This class renders the sign-in form that will display for unauthorized user 
 * or user who launches the app for the first time.
 **************************************************************************************/
export default class SignInForm extends Component {

  constructor(props) {

    super(props);

    this.state = {
      username: '',
      password: '',
      errorMessage: '',
      isLoading: false // flag to indicate whether the screen is still loading
    };
    this.handleSignInUser = this.handleSignInUser.bind(this);
  }

  /**
   * Check if user is successfully logged in to the app.
   */
  async handleSignInUser() {

    if (this.state.username.length === 0 || this.state.password.length === 0) {
      Alert.alert(
        'Failed to Sign-in',
        '\nUsername and Password must not be empty.',
        [{ text: 'OK' }],
        { cancelable: false }
      );
      return;
    }

    this.setState({isLoading: true});
    const URL = SERVER_IP_ADDRESS + '/users/' + this.state.username + '/' + this.state.password;
    console.log('Request URL', URL);

    try {

      const response = await axios.get(URL);
      this.setState({isLoading: false});

      if (response.data.status !== 200) {
        console.log('User not found', response.data.response);
        Alert.alert(
          'Failed to Sign-in',
          response.data.response,
          [{ text: 'OK' }],
          { cancelable: false }
        );

      } else {

        await AsyncStorage.setItem(USER_KEY_STORAGE, this.state.username);

        // At this point, user is authenticated. Navigate to homescreen of the main application.
        this.props.navigation.navigate('Homepage', {username : this.state.username});
      }

    } catch (error) {
      console.log('Error sign-in user', error);
      this.setState({ errorMessage: error.message });

      Alert.alert(
        'Failed to Sign-in',
        error.message,
        [{ text: 'OK' }],
        { cancelable: false }
      );
      this.setState({isLoading: false});

    }

  }

  render() {

    if (this.state.isLoading) {
      //Loading View while data is loading
      return (
        <View style={{ flex: 1, justifyContent: 'center' }}>
          <ActivityIndicator size="large" color="#0000ff" />
          <Text style={{textAlign: 'center', marginTop: 20}}>Hang on!!!</Text>
          <Text style={{textAlign: 'center'}}>Loading...</Text>
        </View>
      );
    }
    
    return (
      <View style={styles.container}>

        <Text style={styles.fieldTitleText}>Username<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style={styles.input}
          placeholder = "Enter your username"
          placeholderTextColor = "rgba(255, 255, 255, 0.7)"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onChangeText = {(username) => this.setState({username})}
          value = {this.state.username}
          onFocus = { () => this.setState({username: ''})}
          underlineColorAndroid = "#fff"
        />

        <Text style={styles.fieldTitleText}>Password<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style={styles.input}
          placeholder = "Enter your password"
          placeholderTextColor = "rgba(255, 255, 255, 0.7)"
          autoCapitalize = "none"
          secureTextEntry = {true}
          onChangeText = {(password) => this.setState({password})}
          value = {this.state.password}
          onFocus = { () => this.setState({password: ''})}
          underlineColorAndroid = "#fff"
        />
        
        <TouchableOpacity
          onPress={this.handleSignInUser}
          style={styles.buttonContainer}
        >
          <Text style={styles.buttonText}>LOGIN</Text>
        </TouchableOpacity>

        <View style={{width: Dimensions.get('window').width - 50, flexDirection: 'row', justifyContent: 'space-around', paddingVertical: 30}}>
          <TouchableOpacity onPress={() => this.props.navigation.navigate('SignUpScreen')}>
            <Text style={{color: '#0F2F44', backgroundColor: 'transparent', textDecorationLine: 'underline'}}>First Time Here? Create Account</Text>
          </TouchableOpacity>
        </View>
        
      </View>

    );
  }
}
	
const styles = StyleSheet.create({
  container: {
    padding: 20,
    marginTop: 50
  },
  fieldTitleText: {
    color: '#0F2F44', 
    fontWeight: 'bold', 
    paddingLeft: 10,
    paddingBottom: 5
  },
  input: {
    width: Dimensions.get('window').width - 50,
    height: 40,
    backgroundColor: 'rgba(0, 0, 0, 0.2)',
    marginBottom: 20,
    color: '#0F2F44',
    paddingHorizontal: 20,
    borderRadius: 20
  },
  buttonContainer: {
    backgroundColor: '#F5C945',
    paddingVertical: 15,
    borderRadius: 20
  },
  buttonText: {
    textAlign: 'center',
    color: '#FFFFFF',
    fontWeight: '700'
  }
});
