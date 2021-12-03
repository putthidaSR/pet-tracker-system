import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import SignInScreen from '../screens/authentication/SignInScreen';
import SignUpScreen from '../screens/authentication/SignUpScreen';
import HomepageRouter from './HomepageRouter';

/**
 * This function component represents router for user authentication screen. 
 */
export default function AuthenticationRouter() {

  const Stack = createStackNavigator();

  return (
    <Stack.Navigator screenOptions={{ headerShown: false }} >
      <Stack.Screen name="SignInScreen" component={SignInScreen} />
      <Stack.Screen name="SignUpScreen" component={SignUpScreen} />
      <Stack.Screen name="Homepage" component={HomepageRouter} />
    </Stack.Navigator>
  );
}