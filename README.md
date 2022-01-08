# Project Structure Overview

├── api-doc
├── database
├── client
│   ├── mobile
│   │  ├── src
│   ├── website
│   │  ├── src
├── server
│   ├── pet-tracker


- api-doc/ : Contains API documentations
- database/: Contains schema and sample data for MySQL
- client/  : Contains source code for front-end (mobile and website)
- server/  : Contains back-end code to create APIs, communicate with client, and all logics to communicate/connect with MySQL database

NOTE:
- In client/mobile/, most code are auto-generated with react-native framework. The code we wrote is located in the following directories: /client/mobile/src
- In client/website/, most code are auto-generated with ReactJS framework. The code we wrote is located in the following directories: /client/website/src

==============================================================================================================
## How to run the server locally

### Setup MySQL Server
- Download and install MySQL Workbench
- Setup MySQL server locally on your machine (Tutorial: https://dev.mysql.com/doc/mysql-getting-started/en/, https://www.youtube.com/watch?v=u96rVINbAUI)
- Connect to MySQL server (remember username and password)

### Build and run the webserver
- Import the server code (/code/server/pet-tracker) to Eclipse IDE for Enterprise Java and Web Developers
- Update hibernate configuration file by navigating to `/code/server/pet-tracker`
- Replace username, password, connection_url to match your local environment
- Update the maven project
- Create a new Tomcat v9.0 server with the current project (pet-tracker)
- Start the server

==============================================================================================================

## How to run the mobile application locally

### Software Requirement
- [Android Studio](https://developer.android.com/studio "Android Studio") (at least v3.5)
- [Xcode](https://developer.apple.com/xcode/ "Xcode") (always update to the latest version)
- IDE of your choice. Recommended:
	- [Visual Studio Code 2](https://code.visualstudio.com/ "Visual Studio Code 2")

### Hardware Requirement
- macOS is required to run iOS app

### Dependencies Installation
The dependencies below are required to install on your local machine prior to building and running React-Native app. 
- [Node](https://nodejs.org/en/ "Node") (at least v12)
- [Watchman](https://facebook.github.io/watchman/ "Watchman") (v4.9.0)
- [React Native CLI ](https://www.npmjs.com/package/react-native-cli "React Native CLI ")(v2.0.1)
- [React Native](https://www.npmjs.com/package/react-native "React Native") (v0.62.2)
- Java Development Kit (JDK) 8

> **NOTE**: Please use the same version as listed above (if possible) to ensure the app will run properly.

### Building And Running The App
#### Getting Started
Navigate to the root of the mobile project (/code/client/mobile) and install all the dependencies:
- Run: `npm install`
- Run: `cd ios && pod install && cd ..`
> For third party libraries and dependencies that are used inside the project, please see **package.json** file.

- Start the packager: `npm start`

#### Run the App in client root directory
- Android: `react-native run-android`
- iOS: `react-native run-ios`

#### Debug the App
- Android: `⌘M` (Mac) or `Ctrl+M` (Linux or Windows)
- iOS: `⌘D` (Mac) or `Ctrl+M` (Linux or Windows)

> Resource: https://facebook.github.io/react-native/docs/debugging


==============================================================================================================

## How to run the website locally

### Dependencies Installation
The dependency below is required to install on your local machine prior to building and running the ReactJS web-app. 
- [Node](https://nodejs.org/en/ "Node") (at least v12)

### Building And Running The Website
Navigate to the root of the website project (`/code/client/website`) and install all the dependencies:
- `npm install`

Run the website in the development mode
- `npm start`
Note: Running the command above will open http://localhost:3000 to your default browser. 
The page will reload if you make edits. 
You will also see any lint errors in the console.

### Developer Notes
- You can change port number in **package.json** file.
- When running both server code and website concurrently, port number on the website must be different from the port number of your back-end server


