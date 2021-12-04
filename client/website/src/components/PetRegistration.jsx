import '../App.css';
import React, { Component } from "react";
import Navbar from "./Navbar";

export default class PetRegistration extends Component {
    
    handleRegistration = (e) => {
        e.preventDefault();
        console.log('You clicked submit.');
    }

    render() {
        return (
            <div className="app">
            <div className="navbar">
                <Navbar />
            </div>

            <div className="main">
                <div className="auth-inner">

                <form onSubmit={this.handleRegistration}>

                    <h3>Register New Pet</h3>

                    <div className="form-group">
                        <label>Pet RFID Tag Number</label>
                        <input type="text" className="form-control" placeholder="First name" />
                    </div>
                    <br />

                    <div className="form-group">
                        <label>Pet Name</label>
                        <input type="text" className="form-control" placeholder="Last name" />
                    </div>
                    <br />
                    
                    <div className="form-group">
                        <label>Email address</label>
                        <input type="email" className="form-control" placeholder="Enter email" />
                    </div>
                    <br />

                    <div className="form-group">
                        <label>Password</label>
                        <input type="password" className="form-control" placeholder="Enter password" />
                    </div>
                    <br />

                    <button type="submit" className="btn btn-primary btn-block">Sign Up</button>

                    <br />
                    <p className="forgot-password text-right">
                        Already registered? <a href="/sign-in">Sign-In</a>
                    </p>
                </form>
            </div>
            </div>
        </div>
        );
    }
}