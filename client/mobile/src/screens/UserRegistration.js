import React, { Component } from "react";
import { StyleSheet, Text, View } from "react-native";
	
export default class UserRegistration extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      
      isLoading: false // flag to indicate whether the screen is still loading
    };
  }
    
  render() {
    return (
      <View style={styles.container}>
        <Text>Home</Text>
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

