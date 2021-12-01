/* eslint-disable react-native/no-inline-styles */
import React from 'react';
import { Image } from 'react-native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { createStackNavigator } from '@react-navigation/stack';

// For bottom tab
import HomepageScreen from '../screens/HomepageScreen';
import ViewMyPetsScreen from '../screens/ViewMyPetsScreen';
import AccountScreen from '../screens/AccountScreen';

// For homepage stack

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
      tabBarOptions= {{
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
          tabBarLabel: 'Home',
          tabBarIcon: () => (
            <Image
              style={{ width: 25, height: 25, resizeMode: 'contain'}}
              source={{
                uri: 'https://reactnative.dev/img/tiny_logo.png'
              }}
            />          
          )
        }}
      />

      <MainBottomTab.Screen 
        name="ViewMyPetsScreen" 
        component={ViewMyPetsScreen}
        options={{
          tabBarLabel: 'View My Pets',
          tabBarIcon: () => (
            <Image
              style={{ width: 25, height: 25, resizeMode: 'contain'}}
              source={{
                uri: 'https://reactnative.dev/img/tiny_logo.png'
              }}
            />          
          )
        }}
      />

      <MainBottomTab.Screen 
        name="AccountScreen" 
        component={AccountScreen}
        options={{
          tabBarLabel: 'Account',
          tabBarIcon: () => (
            <Image
              style={{ width: 25, height: 25, resizeMode: 'contain'}}
              source={{
                uri: 'https://reactnative.dev/img/tiny_logo.png'
              }}
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
      <Stack.Screen name="HomepageScreen" component={HomepageScreen} />
    </Stack.Navigator>
  );
}