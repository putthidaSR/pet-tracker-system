/* eslint-disable jsx-a11y/alt-text */
import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import appLogo from '../assets/app-logo.png';

/**
 * Component to display the navigation bar at the top of the page.
 */
export default class Navbar extends Component {

    render() {
        return (

            <div>
                <nav className="navbar navbar-expand-lg navbar-light fixed-top">
                    <div className="container"> 
                        {/** Display app logo */}
                        <div class="center-image" style={{width: 100, height: 100}}>
                            <img src={appLogo} />
                        </div>
                        <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
                            <ul className="navbar-nav ml-auto">
                                <li className="nav-item">
                                    <Link className="nav-link" to={"/"}><label style={{marginLeft: '10rem', fontSize: 20}}>View All Veterinarians    |   </label></Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to={"/register"}><label style={{fontSize: 20}}>Register Veterinarian    |   </label></Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to={"/login"}><label style={{fontSize: 20}}>Log Out</label></Link>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
            </div>
        );
    }
};

