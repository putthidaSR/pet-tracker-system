/* eslint-disable jsx-a11y/alt-text */
import React, { Component } from "react";
import { Alert } from 'antd';
import axios from "axios";

import appLogo from '../assets/app-logo.png';

/**
 * This component represents login screen when user launches the website for the first time
 * or for user who is not authenticated yet.
 * 
 * Date: 12/9/2021
 * Author: Putthida Samrith
 */
export default class Login extends Component {

    constructor(props) {
        super(props);

        this.state = {
            username: '',
            password: '',
            isError: false,
            isLoading: false // flag to track if the page is loading
        };
    }

    /**
     * Handle the action when user presses Login.
     */
    handleLogin = async(e) => {

        e.preventDefault();

        // Check if username or password is empty
        if (this.state.username === '' || this.state.password === '') {
            this.setState({isError: true});
            return;
        }

        // Call the backend API to login to the website with the provided username and password
        await axios({
            method: 'POST',
            url: 'http://localhost:8080/PawTracker/app/login',
            data: {
                loginName: this.state.username,
                loginPassword: this.state.password
            },
            headers: {'user_role': `Admin`},
            mode: 'no-cors'
        })
        .then((response) => {

            console.log('successfully login');

            // Redirect the sign in page to home page if login successful
            this.props.history.push('/home');

        }, (error) => {
            if (error.response) {
                // client received an error response (5xx, 4xx)
                console.log(error.response);
                } else if (error.request) {
                // client never received a response, or request never left
                console.log(error.request);
                } else {
                console.error(error.message);
            }

            alert('Fail to loigin. Please try again!');

        });
    }

    render() {
        return (
            <div className="auth-inner">

                {/** Display app logo */}
                <div class="center-image">
                    <img src={appLogo} />
                </div>
                <br />
                <form onSubmit={this.handleLogin}>
               
                    <h3>Welcome Back, Admin!</h3>

                    {/** Display username field */}
                    <div className="form-group">
                        <label>Username</label> <label style={{color: 'red'}}>*</label>
                        <input type="text" className="form-control" placeholder="Enter username" 
                          value={this.state.username} 
                          onChange={(event) => {this.setState({username: event.target.value});}} />
                    </div>
                    <br />

                    {/** Display password field */}
                    <div className="form-group">
                        <label>Password</label> <label style={{color: 'red'}}>*</label>
                        <input type="password" className="form-control" placeholder="Enter password" 
                        value={this.state.password} 
                        onChange={(event) => {this.setState({password: event.target.value});}} />
                    </div>
                    <br />

                    {/** Display checkbox field */}
                    <div className="form-group">
                        <div className="custom-control custom-checkbox">
                            <input type="checkbox" className="custom-control-input" id="customCheck1" />
                            <span>{' '}</span><label className="custom-control-label" htmlFor="customCheck1">Remember me</label>
                        </div>
                    </div>
                    <br />

                    {/** Handle submit button click */}
                    <button type="submit" className="btn btn-primary btn-block">Login as Admin</button>
                    <br />

                    {/** Display error if username or password field is empty */}
                    {this.state.isError && <Alert message="Please enter username and password." type="error" showIcon />}

                </form>
            </div>
        );
    }
}