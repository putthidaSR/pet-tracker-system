import React, {Component} from 'react';
import { StyleSheet, Text, View, Image, KeyboardAvoidingView, ScrollView, Dimensions } from 'react-native';
import SignInForm from './../../components/authentication/SignInForm';

/**
 * This class represents the components to build sign in screen.
 */
export default class SignInScreen extends Component {

  constructor(props) {
    super(props);
  }

  render() {

    return (

      // Create scrollable login form
      <KeyboardAvoidingView behavior="padding" style={styles.fullSize}>
        <ScrollView
          contentContainerStyle={styles.scrollViewContainer}
          keyboardShouldPersistTaps="never"
          scrollEnabled={false}>

          {/** Logo container */}
          <View style={styles.logoContainer}>
            <Image
              style={styles.logo}
              source={require('./../../assets/images/app-logo.png')}
            />
            <Text style={styles.titleText}>Welcome to the largest pet community...</Text>
          </View>

          {/** Form container */}
          <View style={styles.formContainer}>
            <SignInForm navigation={this.props.navigation} />
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    );
  }
}

// style variables to be used in sign-in screen
const styles = StyleSheet.create({
  fullSize: {
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height
  },
  scrollViewContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: '#0F2F44'
  },
  logoContainer: {
    alignItems: 'center',
    flexGrow: 1,
    justifyContent: 'center'
  },
  logo: {
    width: 250,
    height: 250
  },
  titleText: {
    color: '#fff',
    marginTop: 15,
    textAlign: 'center',
    opacity: 1,
    fontSize: 20,
    fontWeight: 'bold',
    fontFamily: 'AmericanTypewriter-Bold'
  },
  formContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    height: Dimensions.get('window').height / 2.5,
    backgroundColor: '#EAF1FF',
    borderRadius: 50
  }
});
