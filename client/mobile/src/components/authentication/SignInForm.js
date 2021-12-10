/* eslint-disable react-native/no-inline-styles */
import React, { Component } from 'react'	;
import { StyleSheet, SafeAreaView, Text, Alert, TextInput, ActivityIndicator, TouchableOpacity, View, Dimensions } from 'react-native';
import axios from 'axios';
import { ButtonGroup } from 'react-native-elements';
import {REQUEST_URLS, USER_ID_KEY_STORAGE, USER_BADGE_NUMBER_STORAGE, USER_NICKNAME_KEY_STORAGE, USER_ROLE_KEY_STORAGE, USER_ROLE} from '../../Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';

/**************************************************************************************
 * This class renders the sign-in form that will display for unauthorized user 
 * or user who launches the app for the first time.
 * 
 * @author Putthida Samrith
 * @date 12/9/2021
 **************************************************************************************/
export default class SignInForm extends Component {

  constructor(props) {

    super(props);

    this.state = {
      username: '',
      password: '',
      userRole: 0, // PetOwner (0), Vet (1)
      isLoading: false // flag to indicate whether the screen is still loading
    };
    this.handleSignInUser = this.handleSignInUser.bind(this);
  }

  /**
   * Check if user is successfully logged in to the app.
   */
  async handleSignInUser() {
    console.log('signin');
    if (this.state.username.length === 0 || this.state.password.length === 0) {
      Alert.alert(
        'Fail to sign-in',
        '\nUsername and Password must not be empty.',
        [{ text: 'OK' }],
        { cancelable: false }
      );
      return;
    }
    
    this.setState({isLoading: true});
    var headerVal = this.state.userRole === 0 ? USER_ROLE.PET_OWNER : USER_ROLE.VETERINARIAN;
    console.log(headerVal);

    try {

      const response = await axios({
        method: 'POST',
        url: REQUEST_URLS.LOGIN,
        data: {
          loginName: this.state.username,
          loginPassword: this.state.password
        },
        headers: {
          user_role: `${headerVal}`
        }
      });
      this.setState({isLoading: false});

      console.log('Successfully login!', response.data);

      // Save data to app cache
      try {
        await AsyncStorage.setItem(USER_NICKNAME_KEY_STORAGE, this.state.username);
        await AsyncStorage.setItem(USER_ID_KEY_STORAGE, response.data.id.toString());
        await AsyncStorage.setItem(USER_ROLE_KEY_STORAGE, this.state.userRole === 0 ? USER_ROLE.PET_OWNER : USER_ROLE.VETERINARIAN);

        if (this.state.userRole === 1) {
          await AsyncStorage.setItem(USER_BADGE_NUMBER_STORAGE, response.data.badgeNumber);
        }

      } catch (error) {
        //console.log('error saving data to app cache');
      }
      
      // At this point, user is authenticated. Navigate to homescreen of the main application based on user role.
      this.props.navigation.navigate('Homepage', {username : this.state.username, id: response.data.id});

    } catch (error) {
      console.log('Error sign-in user', error.request);
      this.setState({isLoading: false});

      Alert.alert(
        'Fail to Login',
        error.message,
        [{ text: 'OK' }],
        { cancelable: false }
      );
      return;
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
      <SafeAreaView style={styles.container}>

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


        <View style={{marginLeft: -10, paddingBottom:20}}>
          <Text style={{...styles.fieldTitleText, marginLeft: 10}}>Are You?<Text style={{color: 'red'}}> *</Text></Text>
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
        
      </SafeAreaView>

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
