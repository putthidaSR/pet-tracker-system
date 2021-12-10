
   
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

import HomepageRouter from './HomepageRouter';
import AuthenticationRouter from './AuthenticationRouter';

/**
 * This class represents the main layout of the application.
 * It will navigate to the appropriate screens based on user's authorization.
 *
 * @author Putthida Samrith
 * @date 12/9/2021
 */
export default class InitialRouter extends React.Component {
  
  constructor(props) {
    super(props);
  }

  render() {

    const Stack = createStackNavigator(); 
    
    return (
      <NavigationContainer>
        <Stack.Navigator
          screenOptions={{
            headerShown: false
          }}
          initialRouteName={this.props.isUserSignedIn ? 'Homepage' : 'Authentication'}
        >
          <Stack.Screen name="Authentication" component={AuthenticationRouter} />
          <Stack.Screen name="Homepage" component={HomepageRouter} />

        </Stack.Navigator>
      </NavigationContainer>
    );
  }
}