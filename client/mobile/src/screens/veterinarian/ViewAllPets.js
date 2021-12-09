/* eslint-disable react-native/no-inline-styles */
import React, { Component } from "react";
import { StyleSheet, ActivityIndicator, ScrollView, Alert, Switch, TextInput, SafeAreaView, Dimensions, Text, View } from "react-native";
import {REQUEST_URLS, USER_BADGE_NUMBER_STORAGE} from '../../Configuration';
import { Button, ButtonGroup, Icon } from 'react-native-elements';
import axios from 'axios';
import { Table, Row } from 'react-native-table-component';
import AsyncStorage from '@react-native-async-storage/async-storage';
import DropDownPicker from 'react-native-dropdown-picker';

export default class ViewAllPets extends Component {
  
  constructor(props) {
    super(props);
    
    this.state = {
      username: '',
      userId: 0,
      email: '',
      phoneNumber: '',
      address: '',
      badgeNumber: '',
      rfidNumber: '',
      searchByOption: 0,
      tableHeader: ['User', 'Pet Name', 'RFID #', 'Active', 'Delete'],
      tableData: [],

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
        Alert.alert('Error', error.message);
        this.props.navigation.navigate('Homepage');
      });
  }

  /**
   * Update RFID status of the specified pet.
   */
  handleUpdateRfidStatus = async (id, status) => {

    console.log('Attempt to update pet with ID ' + id + ' to status: ' + !status);
    this.setState({isLoading: true});
    
    await axios({
      url: REQUEST_URLS.UPDATE_PET_DETAILS + '/' + id,
      method: 'PUT',
      data: {
        active: !status
      }
    })
      .then(() => {
        this.setState({isLoading: false});
        console.log(id, 'successfully updated!');
  
        this.getAllPetsWithDetails();
        Alert.alert('RFID status has successfully updated.');
      })
      .catch((error) => {
        this.setState({isLoading: false});
        Alert.alert('Error', error.message);
        this.props.navigation.navigate('Homepage');
      });
  }
  
  /**
   * Delete the specified pet ID from the database.
   */
  handleDeletePet = async (id) => {

    console.log('Attempt to delete pet', id);
    this.setState({isLoading: true});
    
    await axios({
      url: REQUEST_URLS.DELETE_PET_BY_ID + '/' + id,
      method: 'DELETE'
    })
      .then(() => {
        this.setState({isLoading: false});
        console.log(id, 'successfully deleted!');

        this.getAllPetsWithDetails();
        Alert.alert('Pet is successfully deleted!');
      })
      .catch((error) => {
        this.setState({isLoading: false});
        Alert.alert('Error', error.message);
        this.props.navigation.navigate('Homepage');
      });
  }

  /**
   * Retrieve all pets with details from database.
   */
  getAllPetsWithDetails = async () => {

    this.setState({isLoading: true});

    try {
      // Retrieve username from app cache if exists
      const badgeNumberFromCache = await AsyncStorage.getItem(USER_BADGE_NUMBER_STORAGE);
      if (badgeNumberFromCache !== null) {
        this.setState({badgeNumber: badgeNumberFromCache});
      }
    } catch (error) {
      // ignore error here (won't have any effect)
    }

    await axios({
      url: REQUEST_URLS.VIEW_ALL_PETS_DETAILS,
      method: 'GET',
      headers: {
        badge_number: `${this.state.badgeNumber}`
      }
    })
      .then((response) => {
        this.setState({isLoading: false});
        console.log(response.data);

        let newTableData = [];
        var serverData = response.data.results;
        serverData.forEach((element, index) => {
          console.log(index, element);
          var arrayElement = [element.username, element.petName, element.rfidNumber, this.elementButton(element.petId, element.rfidStatus), this.deleteButton(element.petId)];
          newTableData.push(arrayElement);
        });

        this.setState({tableData: newTableData});

      })
      .catch((error) => {
        this.setState({isLoading: false});
        Alert.alert('Error', error.message);
        this.props.navigation.navigate('Homepage');
      });
  }

  /**
   * 
   * @returns 
   */
  handleSearchPets = async () => {

    if (this.state.userId === 0 && this.state.rfidNumber === '') {
      Alert.alert(
        '',
        '\nPlease enter RFID Tag Number or select a user.',
        [{ text: 'OK' }],
        { cancelable: false }
      );
      return;
    }

    this.setState({isLoading: true});

    var url = this.state.rfidNumber !== '' ? REQUEST_URLS.VIEW_PET_BY_RFID + '/' + this.state.rfidNumber : REQUEST_URLS.VIEW_PETS_BY_USER + "/" + this.state.userId + '/pets';
    console.log(url);

    await axios.get(url)
      .then((response) => {

        let newTableData = [];
        var serverData = response.data.results;
        serverData.forEach((element, index) => {
          console.log(index, element);
          var arrayElement = [element.username, element.petName, element.rfidNumber, this.elementButton(element.petId, element.rfidStatus), this.deleteButton(element.petId)];
          newTableData.push(arrayElement);
        });

        this.setState({tableData: newTableData, isLoading: false, userId: 0});
      })
      .catch((error) => {
        this.setState({isLoading: false});
        console.log(error.message);

        if (error.response.status === 404) {
          Alert.alert(
            'No record found',
            'Please check the RFID tag number again.',
            [{ text: 'OK' }],
            { cancelable: false }
          );
          return;
        }

        Alert.alert(
          'Error',
          error.message,
          [{ text: 'OK' }],
          { cancelable: false }
        );
        return;
      });

  }

  /**
   * Render how the switch button in table will display
   */
  elementButton = (id, status) => (
    <View style={{alignItems: 'center'}}>
      <Switch
        trackColor={{ false: '#767577', true: '#green' }}
        ios_backgroundColor="#3e3e3e"
        value={status}
        onValueChange={() => {
          this.handleUpdateRfidStatus(id, status);
        }}
      />
    </View>

  );

  /**
   * Render how the delete button in the table will display
   */
  deleteButton = (id) => (
    <View style={{alignItems: 'center'}}>
      <Icon
        raised
        name='trash'
        type='font-awesome'
        color='red'
        size={15}
        onPress={() => this.handleDeletePet(id)} />
    </View>
    
  );

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


  /*****************************************************
   * Render search form with buttons
  *****************************************************/
  renderSearchForm = () => {
    return (
      <View style={{alignSelf: 'center', backgroundColor: '#F5C945', position: 'absolute', top: 0, left: 0, 
        width: Dimensions.get('window').width, height: 200}}>


        <Text style={styles.fieldTitleText}>Search By<Text style={{color: 'red'}}> *</Text></Text>
        <ButtonGroup
          textStyle={{fontSize: 12}}
          onPress={(selectedIndex) => {
            this.setState({searchByOption: selectedIndex});
          }}
          selectedIndex={this.state.searchByOption}
          buttons={["RFID Tag Number", "Pet Owner's Name"]}
          containerStyle={{height: 35, borderRadius: 10, borderWidth: 3, width: Dimensions.get('window').width - 80, alignSelf: 'center'}}
          selectedButtonStyle={{backgroundColor:'#0F2F44'}}
        />

        {
          this.state.searchByOption === 0 &&
          <TextInput
            style = {styles.input}
            onChangeText = {(rfidNumber) => this.setState({rfidNumber})}
            placeholder = "Enter RFID Tag Number"
            placeholderTextColor = "gray"
            autoCapitalize = "none"
            autoCorrect = {false}
            returnKeyType = "next"
            onFocus = { () => this.setState({rfidNumber: ''})}
            underlineColorAndroid = "#fff"
          />
        }

        {
          this.state.searchByOption === 1 &&
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
            placeholder="Select a user"
            placeholderStyle={{color: "grey"}}
            containerStyle={{
              width: Dimensions.get('window').width - 80,
              alignSelf: 'center',
              paddingBottom: 15,
              borderColor: '#EAF1FF'
            }}
            textStyle={{
              fontSize: 13,
              color: 'black'
            }}
            style={{
              height: 35,
              borderWidth: 3,
              borderColor: '#EAF1FF'
            }}
          />
        }

        <View style={{flexDirection: 'row', justifyContent: 'center'}}>
          <View style={{}}>
            <Button type="solid" title=" Search "
              titleStyle={{fontSize: 13, fontWeight: 'bold'}}
              containerStyle={{width: (Dimensions.get('window').width - 80) / 2, alignSelf: 'center'}}
              buttonStyle={{borderWidth: 2,borderColor: '#fff',borderRadius:10,backgroundColor: '#0F2F44',height: 40}}
              onPress={this.handleSearchPets} 
            />
          </View>
          <View style={{}}>
            <Button type="solid" title=" View All Pets "
              titleStyle={{fontSize: 13, fontWeight: 'bold'}}
              containerStyle={{width: (Dimensions.get('window').width - 80) / 2, alignSelf: 'center'}}
              buttonStyle={{borderWidth: 2,borderColor: '#fff',borderRadius:10,backgroundColor: '#0F2F44',height: 40}}
              onPress={this.getAllPetsWithDetails} 
            />
          </View>
        </View>

      </View>


    );
  }

  render() {

    // Display the spinning wheel to show that the app is still loading
    if (this.state.isLoading) {
      return (
        <View style={{ flex: 1, justifyContent: 'center' }}>
          <ActivityIndicator size="large" color="#0000ff" />
          <Text style={{textAlign: 'center'}}>{'\n'}Please wait...</Text>
        </View>
      );
    }

    return (
      <SafeAreaView style={styles.container}>

        <View>
          {this.renderSearchForm()}
        </View>

        {
          this.state.tableData.length > 0 &&

          <View style={styles.tableContainer}>
            <ScrollView horizontal={true}>
              <View>
                <Table borderStyle={{borderWidth: 1, borderColor: '#C1C0B9'}}>
                  <Row data={this.state.tableHeader} widthArr={[80, 80, 80, 80, 80]} style={styles.head} textStyle={styles.text}/>
                </Table>

                <ScrollView style={styles.dataWrapper}>
                  <Table borderStyle={{borderWidth: 1}}>

                    {
                      this.state.tableData.map((rowData, index) => (
                        <Row
                          key={index}
                          data={rowData}
                          widthArr={[80, 80, 80, 80, 80]}
                          style={[styles.row, index%2 && {backgroundColor: '#fff'}]}
                          textStyle={{...styles.text, fontSize: 12, color: 'black'}}
                        />
                      ))
                    }
                  </Table>
                </ScrollView>
              </View>
            </ScrollView>
          
            
          </View>
        
        }
      </SafeAreaView>
    );
  }

}
	
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff"
  },
  tableContainer: {
    marginTop: 200
  }, 
  fieldTitleText: {
    color: '#0F2F44', 
    fontWeight: 'bold', 
    paddingLeft: 50,
    paddingBottom: 5,
    textAlign: 'left',
    fontSize: 15
  },
  input: {
    alignSelf: 'center',
    width: Dimensions.get('window').width - 80,
    height: 35,
    backgroundColor: '#fff',
    marginBottom: 20,
    color: 'black',
    borderWidth: 3,
    borderColor: '#EAF1FF',
    paddingHorizontal: 20,
    borderRadius: 10,
    fontSize: 13
  },

  head: { height: 40, backgroundColor: '#0F2F44' },
  text: { margin: 6, color: 'white', fontWeight: 'bold'},
  row: { flexDirection: 'row', backgroundColor: '#FFF1C1', height: 40 },
  dataWrapper: { marginTop: -1 }

});

