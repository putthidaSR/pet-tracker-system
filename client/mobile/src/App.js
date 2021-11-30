import React from 'react';
import { View, Text } from 'react-native';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import InitialRouter from './routers/InitialRouter';

import AsyncStorage from '@react-native-community/async-storage';
import {USER_KEY_STORAGE} from './serverConfig';

/**
 * The first component that the application will render when the app is loaded.
 */
export default class App extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      isSignedIn: false // a flag to track if user is already logged in to the app
    };
  }

  /**
   * Get initial data when component is first mounted.
   */
   componentDidMount() {
    this.getUsername();
  }

  getUsername = async () => {
    try {
      const value = await AsyncStorage.getItem(USER_KEY_STORAGE);
      if (value !== null) {
        console.log('User is already logged in', value);
        this.setState({isSignedIn: true});
      }    
    } catch (error) {
      console.log('Error getting username', error);
    }
  }

  render() {

    return (
      <SafeAreaProvider>
        <InitialRouter isUserSignedIn = {this.state.isSignedIn} />
      </SafeAreaProvider>
      
    );
  
  }

}
