import '../App.css';
import React, { Component } from "react";
import Navbar from "./Navbar";

export default class PetRegistration extends Component {
    
    render() {
        return (
            <div className="app">
            <div className="navbar">
                <Navbar />
            </div>

            <div className="main">
                <h1>Pet registration</h1>
            </div>
        </div>
        );
    }
}