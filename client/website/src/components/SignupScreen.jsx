import React, { Component } from "react";
import axios from "axios";
import Navbar from "./Navbar";
import { Alert } from 'antd';

/**
 * This class renders the components to register the veterinarian and call the API to insert new record to the database.
 * 
 * 
 * @author Putthida Samrith
 * @date 12/9/2021
 */
export default class SignUp extends Component {

    constructor(props) {
        super(props);

        this.state = {
            fullName: '',
            badgeNumber: '',
            email: '',
            phoneNumber: '',
            address: '',
            isAdded: false
        }
    }

    /**
     * Handle the action when user presses register.
     */
    handleSignUp = async(event) => {

        event.preventDefault();

        // Construct request option (method, request body)
        const options = {
            method: 'POST',
            url: 'http://localhost:8080/PawTracker/users/vet',
            data: {
                loginName: this.state.fullName,
                loginPassword: '123456!',
                badgeNumber: this.state.badgeNumber,
                phoneNumber: this.state.phoneNumber,
                email: this.state.email,
                address: this.state.address
            },
            mode: 'no-cors'
        };

        await axios(options)
        .then((response) => {
            console.log("success");
            this.setState({isAdded: true});

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
        });
    }

    render() {
        return (
            <div className="auth-inner">

                {/** Display navigation menu bar */}
                <Navbar />

                {/** Display alert message when user is successfully added */}
                {
                    this.state.isAdded && <div style={{height: 100}}>
                        <Alert
                            message="Success"
                            description="New veterinarian has been added to the system."
                            type="success"
                            showIcon/>
                    </div>
                }

                {/** Display the input form */}
                <form onSubmit={this.handleSignUp}>
                    <br />
                    <h3>Register New Veterinarian</h3>
                    <br />
                    <div className="form-group">
                        <label>Full Name</label>
                        <input type="text" className="form-control" placeholder="Enter full name" 
                          value={this.state.fullName} 
                          onChange={(event) => {this.setState({fullName: event.target.value});}} />
                    </div>
                    <br />

                    <div className="form-group">
                        <label>Badge Number</label>
                        <input type="text" className="form-control" placeholder="Enter badge number" 
                            value={this.state.badgeNumber} 
                            onChange={(event) => {this.setState({badgeNumber: event.target.value});}} />
                    </div>
                    <br />
                    
                    <div className="form-group">
                        <label>Email Address</label>
                        <input type="email" className="form-control" placeholder="Enter email address" 
                            value={this.state.email} 
                            onChange={(event) => {this.setState({email: event.target.value});}} />
                    </div>
                    <br />

                    <div className="form-group">
                        <label>Phone Number</label>
                        <input type="text" className="form-control" placeholder="Enter phone number" 
                            value={this.state.phoneNumber} 
                            onChange={(event) => {this.setState({phoneNumber: event.target.value});}} />
                    </div>
                    <br />

                    <div className="form-group">
                        <label>Address</label>
                        <input type="text" className="form-control" placeholder="Enter address" 
                            value={this.state.address} 
                            onChange={(event) => {this.setState({address: event.target.value});}} />
                    </div>
                    <br />

                    <button type="submit" className="btn btn-primary btn-block">Register</button>

                    <br />
                </form>
            </div>
        );
    }
}