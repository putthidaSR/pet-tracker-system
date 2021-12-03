import React, {Component} from 'react';
import { StyleSheet, Text, View, KeyboardAvoidingView, ScrollView, Image } from 'react-native';
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
              source={require('./../../assets/images/app-logo.png')}
            />
            <Text style={styles.titleText}>Join us to never lose sight of your pet...</Text>
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
    backgroundColor: '#0F2F44',
    justifyContent: 'space-between'
  },
  logoContainer: {
    alignItems: 'center',
    flexGrow: 1,
    justifyContent: 'center'
  },
  logo: {
    width: 200,
    height: 200
  },
  titleText: {
    color: '#FFF',
    marginTop: 15,
    width: 300,
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