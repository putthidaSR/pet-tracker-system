import React from 'react';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

import Login from "./components/LoginScreen";
import SignUp from "./components/SignupScreen";
import Homepage from "./components/Homepage";

/**
 * Register router for all supported components in the applications.
 */
function App() {

    return (
        <Router>                
            <div className="App">

                <div className="auth-wrapper">
                    <Switch>
                        <Route path="/login" component={Login} />
                        <Route path="/register" component={SignUp} />
                        <Route exact path='/' component={Homepage} />
                        <Route path="/home" component={Homepage} />
                    </Switch> 
                </div>
            </div>
        </Router>
    );
}

export default App;
