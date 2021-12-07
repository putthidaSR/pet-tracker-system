/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, Text, View, ActivityIndicator, Dimensions } from "react-native";
import { Button } from 'react-native-elements';

export default class AccountScreen extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      isLoading: false // flag to indicate whether the screen is still loading
    };
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
      <View style={styles.container}>
        <Button type="solid" title="LOGOUT"
          titleStyle={{fontSize: 15, fontWeight: 'bold'}}
          containerStyle={{width: (Dimensions.get('window').width) - 60, alignSelf: 'center', paddingBottom: 15, paddingTop: 15}}
          buttonStyle={{
            borderWidth: 3,
            borderColor: 'white',
            borderRadius:15,
            height: 55,
            backgroundColor: '#0F2F44'
          }}
          onPress={() => this.props.navigation.navigate('SignInScreen')}
        />
      </View>
    );
  }

}
	
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center"
  }
});

