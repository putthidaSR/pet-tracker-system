/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { SafeAreaView, Dimensions, StyleSheet, Text, TextInput, View, ActivityIndicator, Alert } from "react-native";
import { Button, ButtonGroup } from 'react-native-elements';
import axios from 'axios';
import {REQUEST_URLS} from '../../Configuration';
import DropDownPicker from 'react-native-dropdown-picker';

/**
 * This class represents the form register a new pet.
 * This component is only for authenticated user.
 *
 * @author Putthida Samrith
 * @date 12/9/2021
 */
export default class PetRegistrationScreen extends Component {
  
  constructor(props) {
    super(props);
      
    this.state = {
      petName: '',
      age: '',
      breed: '',
      color: '',
      species: 0, // Cat(0), Dog(1), Other(2)
      gender: 0, // Female(0), Male(1), Neutered(2)
      petRfidNumber: '',
      userId: 0,

      // For dropdown user list
      allPetOwnersList: [],
      items: [],
      open: false,
      value: null,
      isLoading: false // flag to indicate whether the screen is still loading
    };
  }
  
  /**
   * Get initial data
   */
  componentDidMount() {
    this.getAllPetOwner();
  }

  getAllPetOwner = async () => {

    this.setState({isLoading: true});

    await axios({
      url: REQUEST_URLS.GET_ALL_PET_OWNERS,
      method: 'GET'
    })
      .then((response) => {
        this.setState({allPetOwnersList: response.data.result});

        // Convert returned results into list of objects to be used in the dropdown
        var userListDropdown = [];
        for(var i = 0; i < this.state.allPetOwnersList.length; i++) {
          const objectToAdd = {
            label: this.state.allPetOwnersList[i].username,
            value: this.state.allPetOwnersList[i].user_id
          };
          userListDropdown.push(objectToAdd);
        }

        this.setState({items: userListDropdown, isLoading: false});
        console.log(this.state.items);
      })
      .catch((error) => {
        this.setState({isLoading: false});
        Alert.alert('Error', error);
        this.props.navigation.navigate('Homepage');
      });
  }

  /*****************************************************
   * Trigger the endpoint to call the backend server to
   * add a pet to mobile device
  *****************************************************/
  registerPet = async () => {

    this.setState({isLoading: true});

    const data = {
      name: this.state.petName,
      age: this.state.age,
      breed: this.state.breed,
      color: this.state.color,
      species: this.state.species == 0 ? 'Cat' : this.state.species == 1 ? 'Dog' : 'Other',
      gender: this.state.gender == 0 ? 'Female' : this.state.gender == 1 ? 'Male' : 'Neutered'
    };

    const header = {
      rfid_number: this.state.petRfidNumber,
      user_id: this.state.userId
    };

    await axios({
      url: REQUEST_URLS.REGISTER_PET,
      method: 'POST',
      data: data,
      headers: header
    })
      .then(() => {
        this.setState({isLoading: false});

        Alert.alert('Success', this.state.petName + ' has successfully added to the app.');
        this.props.navigation.goBack();
      })
      .catch((error) => {
        this.setState({isLoading: false});
        Alert.alert('Error', error);
        this.props.navigation.navigate('Homepage');
      });
  }

  /*****************************************************
   * Render the form to fill in
  *****************************************************/
  renderInputForm() {
    return (
      <View style={styles.formContainer}>

        <View style={{padding: 15}} />
        <Text style={styles.fieldTitleText}>Pet Name<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(petName) => this.setState({petName})}
          placeholder = "Enter Your Pet Name"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({petName: ''})}
          underlineColorAndroid = "#fff"
        />

        <Text style={styles.fieldTitleText}>Pet RFID Tag Number<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(petRfidNumber) => this.setState({petRfidNumber})}
          placeholder = "Enter Pet RFID Tag Number"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({petRfidNumber: ''})}
          underlineColorAndroid = "#fff"
        />

        <Text style={styles.fieldTitleText}>Pet Owner<Text style={{color: 'red'}}> *</Text></Text>
        <DropDownPicker
          open={this.state.open}
          value={this.state.value}
          items={this.state.items}
          setOpen={this.setOpen}
          setValue={this.setValue}
          setItems={this.setItems}
          onChangeValue={(value) => {
            this.setState({userId: value});
          }}
          containerStyle={{
            width: Dimensions.get('window').width - 80,
            alignSelf: 'center',
            paddingBottom: 15
          }}
          textStyle={{
            fontSize: 13,
            color: 'gray'
          }}
          style={{
            height: 28
          }}
        />

        <Text style={styles.fieldTitleText}>Estimate Age<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(age) => this.setState({age})}
          placeholder = "Enter age (ie: 6 months, 1 year)"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({age: ''})}
          underlineColorAndroid = "#fff"
        />

        <View style={{marginLeft: 10, paddingBottom:10}}>
          <Text style={{...styles.fieldTitleText, marginLeft: -10}}>Spcies<Text style={{color: 'red'}}> *</Text></Text>
          <ButtonGroup
            textStyle={{fontSize: 12}}
            onPress={(selectedIndex) => {
              this.setState({species: selectedIndex});
            }}
            selectedIndex={this.state.species}
            buttons={['Cat', 'Dog', 'Other']}
            containerStyle={{height: 30, borderRadius: 40, width: Dimensions.get('window').width - 80}}
          />
        </View>

        <View style={{marginLeft: 10, paddingBottom:10}}>
          <Text style={{...styles.fieldTitleText, marginLeft: -10}}>Gender<Text style={{color: 'red'}}> *</Text></Text>
          <ButtonGroup
            textStyle={{fontSize: 12}}
            onPress={(selectedIndex) => {
              this.setState({gender: selectedIndex});
            }}
            selectedIndex={this.state.gender}
            buttons={['Female', 'Male', 'Neutered']}
            containerStyle={{height: 30, borderRadius: 40, width: Dimensions.get('window').width - 80}}
          />
        </View>

        <Text style={styles.fieldTitleText}>Breed<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(breed) => this.setState({breed})}
          placeholder = "Enter Breed (ie: husky, pitbull)"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({breed: ''})}
          underlineColorAndroid = "#fff"
        />

        <Text style={styles.fieldTitleText}>Color<Text style={{color: 'red'}}> *</Text></Text>
        <TextInput
          style = {styles.input}
          onChangeText = {(color) => this.setState({color})}
          placeholder = "Enter Color (ie: gray, black/white)"
          placeholderTextColor = "gray"
          autoCapitalize = "none"
          autoCorrect = {false}
          returnKeyType = "next"
          onFocus = { () => this.setState({color: ''})}
          underlineColorAndroid = "#fff"
        />
      </View>
    );
  }

  /*****************************************************
   * Render button that will add the pet to mobile phone
  *****************************************************/
  renderBottomView() {
    return (
      <View style={{}}>
        <Button type="solid" title=" Register "
          titleStyle={{fontSize: 15, fontWeight: 'bold'}}
          containerStyle={{width: (Dimensions.get('window').width) - 50, alignSelf: 'center', marginTop: 20}}
          buttonStyle={{
            borderWidth: 5,
            borderColor: '#F5C945',
            borderRadius:20,
            backgroundColor: '#0F2F44',
            height: 50
          }}
          onPress={this.registerPet} 
        />
      </View>
    );
  }
    
  setOpen = (open) => {
    this.setState({
      open
    });
  }

  setValue = (callback) => {
    this.setState(state => ({
      value: callback(state.value)
    }));
  }

  setItems = (callback) => {
    this.setState(state => ({
      items: callback(state.items)
    }));
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
          width: Dimensions.get('window').width, height: 300}} />

        {this.renderInputForm()}

        {this.renderBottomView()}

      </SafeAreaView>
    );
  }

}
	
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff"
  },
  fieldTitleText: {
    color: '#0F2F44', 
    fontWeight: 'bold', 
    paddingLeft: 20,
    paddingBottom: 5,
    textAlign: 'left',
    fontSize: 13
  },
  input: {
    alignSelf: 'center',
    width: Dimensions.get('window').width - 80,
    height: 28,
    backgroundColor: '#fff',
    marginBottom: 15,
    color: '#0F2F44',
    borderWidth: 2,
    borderColor: 'gray',
    paddingHorizontal: 20,
    borderRadius: 10,
    fontSize: 13
  },
  formContainer: {
    backgroundColor: 'white',
    alignSelf: 'center',
    width: Dimensions.get('window').width - 40,
    height: Dimensions.get('window').height - 270,
    borderRadius: 35,
    borderColor: '#fff', 
    borderWidth: 5,
    shadowOffset: {
      width: 5,
      height: 10
    },
    shadowColor: 'rgba(0,0,0,1)',
    shadowOpacity: 0.73,
    shadowRadius: 15,
    elevation: 35,
    marginTop: 10
  }
});

