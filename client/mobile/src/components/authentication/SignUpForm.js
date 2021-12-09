/* eslint-disable react-native/no-inline-styles */
import React, { Component } from 'react';
import { StyleSheet, Alert, Text, TextInput, TouchableOpacity, View, Dimensions, ActivityIndicator } from 'react-native';
import { ButtonGroup } from 'react-native-elements';

import axios from 'axios';
import {REQUEST_URLS} from '../../Configuration';

/**************************************************************************************
 * This class renders the sign-up form that will display for unauthorized user 
 * or user who launches the app for the first time.
 * 
 * @author Putthida Samrith
 * @date 12/9/2021
 **************************************************************************************/
export default class SignUpForm extends Component {

  constructor(props) {
    
    super(props);

    this.state = {
      password: '',
      email: '',
      username: '',
      confirmationCode: '',
      badgeNumber: '',
      userRole: 0, // PetOwner (0), Vet (1)
      errorMessage: '',
      isLoading: false // flag to indicate whether the screen is still loading
    };

    this.handleSignUpUser = this.handleSignUpUser.bind(this);
  }
  
  async handleSignUpUser() {

    // Check for form validation
    const { username, password, confirmationCode, badgeNumber, userRole } = this.state;

    if (username.length === 0 || password.length === 0) {
      Alert.alert(
        'Fail to Sign-up',
        '\nUsername and Password must not be empty.',
        [{ text: 'OK' }],
        { cancelable: false }
      );
      return;
    }

    if (password.length < 6) {
      Alert.alert(
        'Fail to Sign-up',
        '\nPassword must be at least 6 characters long.',
        [{ text: 'OK' }],
        { cancelable: false }
      );
      return;
    }

    if (userRole === 0 && confirmationCode == '' ) {
      Alert.alert(
        'Fail to sign-up',
        '\nPlease provide a confirmation code number',
        [{ text: 'OK' }],
        { cancelable: false }
      );
      return;
    }

    if (userRole === 1 && badgeNumber == '' ) {
      Alert.alert(
        'Fail to sign-up',
        '\nPlease provide your badge number',
        [{ text: 'OK' }],
        { cancelable: false }
      );
      return;
    }

    this.setState({isLoading: true});

    try {

      // Set header parameter
      let header = {};
      if (this.state.userRole == 0) {
        header = {confirmation_code: `${this.state.confirmationCode}`};
      } else if (this.state.userRole == 1) {
        header = {badge_number: `${this.state.badgeNumber}`};
      }

      await axios({
        method: 'put',
        url: REQUEST_URLS.REGISTER,
        data: {
          loginName: this.state.username,
          loginPassword: this.state.password
        },
        headers: header
      });

      this.setState({isLoading: false});

      // Navigate to sign-in screen so that user can sign up with the newly created credentials
      console.log('Successfully sign up!');
      Alert.alert('Success', 'Please login with the account you just created');
      this.props.navigation.goBack();

    } catch (error) {

      console.log('Error signing up: ', error.response.status, error.response.data);
      this.setState({isLoading: false});

      if (error.response.status === 404) {
        Alert.alert(
          'Fail to sign-up',
          'No record found. Please check your confirmation code or badge number again.',
          [{ text: 'OK' }],
          { cancelable: false }
        );
        return;
      }

      Alert.alert(
        'Failed to Sign Up',
        error.response.data,
        [{ text: 'OK' }],
        { cancelable: false }
      );
    }
  }

  renderSignUpForm() {
    return (
      <View>
        <TextInput
          style = {styles.input}
          onChangeText = {(username) => this.setState({username})}
          placeholder = "Username*"
          placeholderTextColor = "rgba(255, 255, 255, 0.7)"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({username: ''})}
          underlineColorAndroid = "#fff"
        />
        <TextInput
          style = {styles.input}
          onChangeText = {(password) => this.setState({password})}
          placeholder = "Password*"
          placeholderTextColor = "rgba(255, 255, 255, 0.7)"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => {
            this.setState({password: ''});
            this.setState({errorMessage: 'Passwords must contain uppercase and at least 6 characters length'});
          }}
          secureTextEntry = { true }
          underlineColorAndroid = "#fff"
        />

        <Text style ={{color: 'white', marginLeft: 10}}>Are You? </Text>

        <View style={{marginLeft: -10, paddingBottom: 20}}>
          <ButtonGroup
            textStyle={{fontSize: 12}}
            onPress={(selectedIndex) => {
              this.setState({userRole: selectedIndex});
            }}
            selectedIndex={this.state.userRole}
            buttons={['Pet Owner', 'Veterinarian']}
            containerStyle={{height: 40, borderRadius: 40, width: Dimensions.get('window').width - 50}}
          />

        </View>
        {
          this.state.userRole === 0 &&
             <TextInput
               style = {styles.input}
               onChangeText = {(confirmationCode) => this.setState({confirmationCode})}
               placeholder = "Confirmation Code*"
               placeholderTextColor = "rgba(255, 255, 255, 0.7)"
               autoCapitalize = "none"
               autoCorrect = {false}
               returnKeyType = "next"
               onFocus = { () => this.setState({email: ''})}
               underlineColorAndroid = "#fff"
             />
        }

        {
          this.state.userRole === 1 &&
             <TextInput
               style = {styles.input}
               onChangeText = {(badgeNumber) => this.setState({badgeNumber})}
               placeholder = "Badge Number*"
               placeholderTextColor = "rgba(255, 255, 255, 0.7)"
               autoCapitalize = "none"
               autoCorrect = {false}
               returnKeyType = "next"
               onFocus = { () => this.setState({email: ''})}
               underlineColorAndroid = "#fff"
             />
        }
        
        <TouchableOpacity
          onPress = {this.handleSignUpUser}
          style = {styles.buttonContainer}
        >
          <Text style = {styles.buttonText}>SIGNUP</Text>
        </TouchableOpacity>
        
        <View style={{padding: 15, alignItems: 'center'}}>
          <TouchableOpacity onPress={() => this.props.navigation.goBack()}>
            <Text style={styles.textLink}>Already has an account?</Text>
          </TouchableOpacity>
        </View>

      </View>
    );
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

        <View style={styles.signUpSectionContainer}>

          {this.renderSignUpForm()}
        </View>

      </View>

    );
  }
}
	
const styles = StyleSheet.create({
  container: {
    padding: 20
  },
  input: {
    width: Dimensions.get('window').width - 50,
    height: 40,
    backgroundColor: 'rgba(255, 255, 255, 0.2)',
    marginBottom: 20,
    color: '#FFF',
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
  },
  textLink: {
    color: 'white',
    backgroundColor: 'transparent',
    textDecorationLine: 'underline'
  },
  signUpSectionContainer: {
    width: Dimensions.get('window').width - 50,
    flexDirection: 'row',
    justifyContent: 'space-around',
    paddingVertical: 30,
    marginBottom: 100
  }
});