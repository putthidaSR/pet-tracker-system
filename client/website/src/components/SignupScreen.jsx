import React, { Component } from "react";

export default class SignUp extends Component {

    constructor(props) {
        super(props);

        this.state = {
            fullName: '',
            badgeNumber: '',
            email: '',
            phoneNumber: '',
            address: ''
        }
    }

    handleSignUp = (event) => {
        event.preventDefault();
        console.log('You clicked submit.', event);

        let formData = new FormData();    //formdata object
        formData.append('loginName', this.state.fullName);   //append the values with key, value pair
        formData.append('loginPassword', '123456!');
        formData.append('badgeNumber', this.state.badgeNumber);

        // POST request using fetch with error handling
        const requestOptions = {
            method: 'POST',
            headers: { 'Access-Control-Allow-Origin': '*', 'Accept': 'application/json',
            'Content-Type': 'application/json'},
          //  body: formData,
            body: JSON.stringify({ 
                loginName: this.state.fullName,
                loginPassword: '123456!',
                badgeNumber: this.state.badgeNumber,
                phoneNumber: this.state.phoneNumber,
                email: this.state.email,
                address: this.state.address
            }),
            mode: 'no-cors'
        };



        console.log(requestOptions);

        fetch('https://cors-anywhere.herokuapp.com/http://localhost:8080/PawTracker/users/vet', requestOptions)
            .then(async response => {

                const isJson = response.headers.get('content-type')?.includes('application/json');
                const data = isJson && await response.json();

                // check for error response
                if (!response.ok) {
                    // get error message from body or default to response status
                    const error = (data && data.message) || response.status;
                    return Promise.reject(error);
                }

                console.log('success')
                alert("Success")

            })
            .catch(error => {
                //this.setState({ errorMessage: error.toString() });
                console.error('There was an error!', error);
            });

    }

    render() {
        return (
            <div className="auth-inner">

                <form onSubmit={this.handleSignUp}>

                    <h3>Register New Veterinarian</h3>

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
                    <p className="forgot-password text-right">
                        Already registered? <a href="/sign-in">Sign-In</a>
                    </p>
                </form>
            </div>
        );
    }
}