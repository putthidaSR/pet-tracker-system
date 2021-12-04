import React from 'react';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";

import Login from "./components/LoginScreen";
import SignUp from "./components/SignupScreen";
import Homepage from "./components/Homepage";
import PetRegistration from "./components/PetRegistration";
import ViewAllPets from "./components/ViewAllPets";

function App() {

    return (
        <Router>
            <div className="App">
                <nav className="navbar navbar-expand-lg navbar-light fixed-top">
                    <div className="container">
                    <Link className="navbar-brand" to={"/sign-in"}>Pet Tracker</Link>
                    <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
                        <ul className="navbar-nav ml-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to={"/sign-in"}>Log Out</Link>
                        </li>
                        </ul>
                    </div>
                    </div>
                </nav>

                <div className="auth-wrapper">
                    <Switch>
                        <Route path="/sign-in" component={Login} />
                        <Route path="/sign-up" component={SignUp} />
                        <Route exact path='/' component={Homepage} />
                        <Route path="/home" component={Homepage} />
                        <Route path="/register" component={PetRegistration} />
                        <Route path="/view" component={ViewAllPets} />
                    </Switch> 
                </div>
            </div>
        </Router>
    );
}

export default App;
