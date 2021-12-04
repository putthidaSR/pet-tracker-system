import React, { Component } from "react";

export default class Login extends Component {

    constructor(props) {
        super(props);

        this.state = {
            username: ''
        };
    }

    handleLogin = (e) => {

        e.preventDefault();
        console.log('You clicked submit.');

        /* Redirect the sign in page to home page*/
        this.props.history.push('/home');

        // fetch('http://localhost:8080/login', {
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify(this.state.username, this.state.password)
        //     })
        //     .then(data => data.json())
    }

    render() {
        return (
            <div className="auth-inner">

                <form onSubmit={this.handleLogin}>
                    <h3>Welcome...</h3>

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
                    <div className="form-group">
                        <div className="custom-control custom-checkbox">
                            <input type="checkbox" className="custom-control-input" id="customCheck1" />
                            <span>{' '}</span><label className="custom-control-label" htmlFor="customCheck1">Remember me</label>
                        </div>
                    </div>
                    <br />

                    <button type="submit" className="btn btn-primary btn-block">Submit</button>
                    <br />

                    <p className="forgot-password text-right">
                        <a href="/sign-up">Create a new account</a>
                    </p>

                </form>
            </div>
        );
    }
}