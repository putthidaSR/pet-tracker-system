import React from 'react';
import { View, Text } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { SafeAreaProvider } from 'react-native-safe-area-context';

/**
 * The first component that the application will render when the app is loaded.
 */
export default class App extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {

    return (
      <SafeAreaProvider>
        <NavigationContainer>
          <View style={{flex: 1, justifyContent: 'center'}}>
            <Text>Main Page</Text>
          </View>
        </NavigationContainer>
      </SafeAreaProvider>
      
    );
  
  }

}
