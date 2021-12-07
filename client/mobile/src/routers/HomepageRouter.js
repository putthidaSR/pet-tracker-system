/* eslint-disable react-native/no-inline-styles */
import React from 'react';
import { TouchableOpacity, Image } from 'react-native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { createStackNavigator } from '@react-navigation/stack';
import { Icon } from 'react-native-elements';

// For bottom tab
import HomepageScreen from '../screens/HomepageScreen';
import AccountScreen from '../screens/AccountScreen';

// For homepage stack
import WeatherInfoScreen from '../screens/pet_owner/WeatherInfoScreen';
import PetLocationScreen from '../screens/pet_owner/AllPetsLocationScreen';
import ViewMyPetsScreen from '../screens/pet_owner/ViewMyPetsScreen';
import EachPetLocationScreen from '../screens/pet_owner/EachPetLocationScreen';

// Features for veterinarians
import UserRegistration from '../screens/veterinarian/UserRegistration';
import PetRegistrationScreen from '../screens/veterinarian/PetRegistrationScreen';
import ViewAllPets from '../screens/veterinarian/ViewAllPets';

/**
 * This function component will render the HomePage of the main application after user is successfully logged in.
 */
export default function HomePageNavigator() {
  return (
    <HomepageBottomTab />
  );
}

/**
 * Bottom navigator tab for Homepage
 */
function HomepageBottomTab() {

  const MainBottomTab = createBottomTabNavigator();

  return (
    <MainBottomTab.Navigator
      initialRouteName="Home"
      screenOptions= {{
        activeTintColor: '#2980b9',
        backgroundColor: 'blue',
        inactiveTintColor: 'rgba(0,0,0,0.3)',
        labelStyle:{fontWeight: 'bold'},
        labelPosition: 'below-icon',
        style:{padding: 5, backgroundColor: 'white'}
      }}
    >
      <MainBottomTab.Screen 
        name="Home" 
        component={HomepageStack}
        options={{
          headerShown: false,
          tabBarLabel: 'Home',
          tabBarIcon: () => (
            <Image
              style={{ width: 25, height: 25, resizeMode: 'contain'}}
              source={require('./../assets/images/home.png')}
            />          
          )
        }}
      />

      <MainBottomTab.Screen 
        name="ViewMyPetsScreen" 
        component={ViewMyPetsScreen}
        options={{
          headerShown: false,
          tabBarLabel: 'View My Pets',
          tabBarIcon: () => (
            <Image
              style={{ width: 25, height: 25, resizeMode: 'contain'}}
              source={require('./../assets/images/paw.png')}
            />          
          )
        }}
      />

      <MainBottomTab.Screen 
        name="AccountScreen" 
        component={AccountScreen}
        options={{
          headerShown: false,
          tabBarLabel: 'Account',
          tabBarIcon: () => (
            <Image
              style={{ width: 25, height: 25, resizeMode: 'contain'}}
              source={require('./../assets/images/account.png')}
            />          
          )
        }}
      />

    </MainBottomTab.Navigator>
  );
}

function HomepageStack() {

  const Stack = createStackNavigator();

  return (

    <Stack.Navigator screenOptions={{ headerShown: false }} >
      <Stack.Screen name="HomepageScreen" component={HomepageScreen} options={{
        headerShown: false
      }} />

      <Stack.Screen name="ViewMyPets" component={ViewMyPetsScreen} options={({ navigation }) => ({
        headerShown: true,
        headerTitle: 'My Pets',
        headerTitleStyle: {fontWeight: 'bold', color: '#0F2F44'},
        headerStyle: {backgroundColor: '#F5C945'},
        headerLeft: () => (
          <TouchableOpacity style={{marginLeft: 15}}><Icon name="arrow-back" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        ),
        headerRight: () => (
          <TouchableOpacity style={{marginRight: 15}}><Icon name="home" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        )
      })} />

      <Stack.Screen name="EachPetLocation" component={EachPetLocationScreen} options={({ navigation }) => ({
        headerShown: true,
        headerTitle: 'Track Last Seen Locations',
        headerTitleStyle: {fontWeight: 'bold', color: '#0F2F44'},
        headerStyle: {backgroundColor: '#F5C945'},
        headerLeft: () => (
          <TouchableOpacity style={{marginLeft: 15}}><Icon name="arrow-back" color="white" onPress={() => navigation.navigate('ViewMyPets')} /></TouchableOpacity>
        ),
        headerRight: () => (
          <TouchableOpacity style={{marginRight: 15}}><Icon name="home" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        )
      })} />

      <Stack.Screen name="PetRegistration" component={PetRegistrationScreen} options={({ navigation }) => ({
        headerShown: true,
        headerTitle: 'Register New Pet',
        headerTitleStyle: {fontWeight: 'bold', color: '#0F2F44'},
        headerStyle: {backgroundColor: '#F5C945'},
        headerLeft: () => (
          <TouchableOpacity style={{marginLeft: 15}}><Icon name="arrow-back" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        ),
        headerRight: () => (
          <TouchableOpacity style={{marginRight: 15}}><Icon name="home" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        )
      })} />


      <Stack.Screen name="PetLocationScreen" component={PetLocationScreen} options={({ navigation }) => ({
        headerShown: true,
        headerTitle: 'Track All My Pets',
        headerTitleStyle: {fontWeight: 'bold', color: '#0F2F44'},
        headerStyle: {backgroundColor: '#F5C945'},
        headerLeft: () => (
          <TouchableOpacity style={{marginLeft: 15}}><Icon name="arrow-back" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        ),
        headerRight: () => (
          <TouchableOpacity style={{marginRight: 15}}><Icon name="home" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        )
      })} />

      <Stack.Screen name="WeatherInfoScreen" component={WeatherInfoScreen} options={({ navigation }) => ({
        headerShown: true,
        headerTitle: 'Should I go out?',
        headerTitleStyle: {fontWeight: 'bold', color: '#0F2F44'},
        headerStyle: {backgroundColor: '#F5C945'},
        headerLeft: () => (
          <TouchableOpacity style={{marginLeft: 15}}><Icon name="arrow-back" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        ),
        headerRight: () => (
          <TouchableOpacity style={{marginRight: 15}}><Icon name="home" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        )
      })} />

      <Stack.Screen name="UserRegistration" component={UserRegistration} options={({ navigation }) => ({
        headerShown: true,
        headerTitle: 'Register New User',
        headerTitleStyle: {fontWeight: 'bold', color: '#0F2F44'},
        headerStyle: {backgroundColor: '#F5C945'},
        headerLeft: () => (
          <TouchableOpacity style={{marginLeft: 15}}><Icon name="arrow-back" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        ),
        headerRight: () => (
          <TouchableOpacity style={{marginRight: 15}}><Icon name="home" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        )
      })} />

      <Stack.Screen name="ViewAllPets" component={ViewAllPets} options={({ navigation }) => ({
        headerShown: false,
        headerTitle: 'View All Pets',
        headerTitleStyle: {fontWeight: 'bold', color: '#0F2F44'},
        headerStyle: {backgroundColor: '#F5C945'},
        headerLeft: () => (
          <TouchableOpacity style={{marginLeft: 15}}><Icon name="arrow-back" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        ),
        headerRight: () => (
          <TouchableOpacity style={{marginRight: 15}}><Icon name="home" color="white" onPress={() => navigation.navigate('HomepageScreen')} /></TouchableOpacity>
        )
      })} />

        
    </Stack.Navigator>
  );
}