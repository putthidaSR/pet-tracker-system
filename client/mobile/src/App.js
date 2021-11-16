import React from 'react';
import { View, Text } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';

/**
 * The first component that the application will render when the app is loaded.
 */
export default class App extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {

    return (
      <NavigationContainer>
        <View style={{flex: 1}}>
          <Text>Main Page</Text>
        </View>
      </NavigationContainer>
    );
  
  }

}
