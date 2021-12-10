/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, TouchableOpacity, SafeAreaView, Text, View, ActivityIndicator, Dimensions, Image } from "react-native";
import {USER_ID_KEY_STORAGE, USER_NICKNAME_KEY_STORAGE, USER_ROLE_KEY_STORAGE, USER_ROLE} from '../Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';

/**
 * This class renders the components to display different menu options on homescreen.
 * Different user role (pet owner or veterinarian) will have different menu options.
 * This screen will the first screen authenticated users see after they successfully login
 * to the app.
 *
 * @author Putthida Samrith
 * @date 12/9/2021
 */
export default class HomepageScreen extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      username: '',
      userId: 0,
      userRole: '',
      isLoading: false // flag to indicate whether the screen is still loading
    };
  }
  
  /**
   * Get initial data.
   */
  componentDidMount() {
    this.getCurrentUser();
  }

  /***************************************************************
   * Get the info of the current logged-in user from app cache
   ****************************************************************/
  getCurrentUser = async () => {
    try {

      this.setState({isLoading: true});

      // Retrieve username from app cache if exists
      const userNameFromCache = await AsyncStorage.getItem(USER_NICKNAME_KEY_STORAGE);
      if (userNameFromCache !== null) {
        this.setState({username: userNameFromCache});
      } else if (typeof this.props.route.params !== "undefined") {
        if (this.props.route.params.username !== null) {
          this.setState({username: this.props.route.params.username});
        }
      }

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

      // Retrieve user role from app cache if exists
      const userRoleFromCache = await AsyncStorage.getItem(USER_ROLE_KEY_STORAGE);
      if (userRoleFromCache !== null) {
        this.setState({userRole: userRoleFromCache});
      }

      this.setState({isLoading: false});

    } catch (error) {
      this.setState({isLoading: false});
    }
  }

  renderTitleView() {
    return (
      <View style={{marginTop: 30, height: TITLE_HEIGHT_VIEW - 80, width: Dimensions.get('window').width - 50, justifyContent: 'center', borderRadius: 30, alignSelf: 'center', 
        backgroundColor: '#0F2F44'}}>

        {/** Logo container */}
        <View style={styles.logoContainer}>
          <Image
            style={{width: 100, height: 100, resizeMode: 'contain'}}
            source={require('./../assets/images/app-logo.png')}
          />
          <Text style={{...styles.titleText, paddingTop: 10, fontSize: 30, color: '#fff'}}>
            {this.state.userRole == USER_ROLE.PET_OWNER ? 'Welcome to the largest paw community!' : 'Welcome to PawTracker, veterinarian!!!'}</Text>
        </View>
      </View>
    );
  }

  renderMenuOption = (title, navigatorRouteName, imagePath) => {
    return (
      <View style={styles.menuButtonContainer}>
        <TouchableOpacity style={styles.navBarLeftButton} onPress={() => this.props.navigation.navigate(navigatorRouteName) }>
          <Image
            style={styles.button}
            source={imagePath}
          />
          <Text style={styles.bottonOptionText}>
            {title}
          </Text>
        </TouchableOpacity>
        
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
          width: Dimensions.get('window').width, height: 200}} />

        {this.renderTitleView()}

        <Image
          style={{width: 100, height: 100, resizeMode: 'contain'}}
          source={require('./../assets/images/paw.gif')}
        />
        
        {
          this.state.userRole == USER_ROLE.PET_OWNER &&
            <View style={styles.buttonContainer}>
              {this.renderMenuOption("View All My Pets", 'ViewMyPets', require('./../assets/images/view-pets.png'))}
              {this.renderMenuOption("Where Are My Pets?", 'PetLocationScreen', require('./../assets/images/pet-location.png'))}
            </View>
        }

        {
          this.state.userRole == USER_ROLE.VETERINARIAN &&
            <View style={styles.buttonContainer}>
              {this.renderMenuOption("Register New User", 'UserRegistration', require('./../assets/images/user-add.png'))}
              {this.renderMenuOption("Register New Pet", 'PetRegistration', require('./../assets/images/pet-registration.png'))}
              {this.renderMenuOption("View All Pets", 'ViewAllPets', require('./../assets/images/view-pets.png'))}
            </View>
        }

      </SafeAreaView>
    );
  }

}
	
const WHOLE_VIEW = Dimensions.get('window').height;
const TITLE_HEIGHT_VIEW = (WHOLE_VIEW / 2) - 150;
const OPTIONS_HEIGHT_VIEW = WHOLE_VIEW - TITLE_HEIGHT_VIEW;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff'
  },
  logoContainer: {
    alignItems: 'center',
    flexGrow: 1,
    marginTop: 30
  },
  menuButtonContainer: {
    backgroundColor: '#EAF1FF', 
    alignSelf: 'center',
    justifyContent: 'center',
    height: 80,
    width: Dimensions.get('window').width - 50,
    borderColor: '#F5C945',
    borderBottomWidth: 4,
    borderRadius: 30,
    shadowOffset: {
      width: 5,
      height: 10
    },
    shadowColor: 'rgba(0,0,0,1)',
    shadowOpacity: 0.73,
    shadowRadius: 10,
    elevation: 15,
    zIndex: 5,
    marginBottom: 20
  },
  button: {
    width: 50,
    height: 50,
    resizeMode: 'contain',
    alignSelf: 'center',
    marginLeft: 15
  },
  titleText: {
    color: '#EAF1FF',
    fontFamily: 'SavoyeLetPlain',
    fontWeight: 'bold',
    fontSize: 70,
    alignSelf: 'center',
    paddingTop: 10,
    paddingBottom: 10,
    position: 'absolute',
    top: 100,
    zIndex: 4
  },
  buttonContainer: {
    alignSelf: 'center',
    height: OPTIONS_HEIGHT_VIEW
  },
  bottonOptionText: {
    textAlign: 'center', 
    fontSize: 16, 
    fontWeight: 'bold',
    color: '#0F2F44',
    paddingLeft: 20
  },
  navBarLeftButton: {
    paddingLeft: 20,
    width: 240,
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-start'
  }
});
