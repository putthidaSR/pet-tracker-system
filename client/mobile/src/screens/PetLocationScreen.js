/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, Text, View, ActivityIndicator } from "react-native";
	
export default class PetLocationScreen extends Component {
  
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
        <Text>Where is my pet</Text>
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

