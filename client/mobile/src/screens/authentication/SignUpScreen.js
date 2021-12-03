import React, {Component} from 'react';
import { StyleSheet, Text, View, Dimensions, KeyboardAvoidingView, ScrollView, Image } from 'react-native';
import SignUpForm from './../../components/authentication/SignUpForm';

/**
 * This class represents the components to build sign-up screen.
 */
export default class SignUpScreen extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <KeyboardAvoidingView 
        behavior = "padding"
        style={styles.signUpScreenContainer}
      >
        <ScrollView
          contentContainerStyle={styles.signUpScreenContainer}
          keyboardShouldPersistTaps="never"
        >

          {/** Logo container */}
          <View style={styles.logoContainer}>
            <Image
              style={styles.logo}
              source={{uri: 'https://reactnative.dev/img/tiny_logo.png'}}
            />
            <Text style={styles.titleText}>Join us to never lose sight of your pet</Text>
          </View>

          {/** Form container */}
          <View style={styles.formContainer}>
            <SignUpForm navigation = {this.props.navigation} />
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    );
  }
}

const styles = StyleSheet.create({
  signUpScreenContainer: {
    flex: 1,
    backgroundColor: '#3498db',
    justifyContent: 'space-between',
  },
  fullSize: {
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height
  },
  scrollViewContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: '#3498db'
  },
  logoContainer: {
    alignItems: 'center',
    flexGrow: 1,
    justifyContent: 'center',
    marginTop: 100
  },
  logo: {
    width: 100,
    height: 100
  },
  titleText: {
    color: '#FFF',
    marginTop: 15,
    width: 200,
    textAlign: 'center',
    fontSize: 20,
    fontFamily: 'AmericanTypewriter-Bold'
  },
  formContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    flex: 1
  }
});