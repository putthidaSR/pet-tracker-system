import '../App.css';
import React, { Component } from "react";
import Navbar from "./Navbar";

export default class ViewAllPets extends Component {
    
    render() {
        return (
            <div className="app">
            <div className="navbar">
                <Navbar />
            </div>

            <div className="main">
                <h1>View All Pets</h1>
            </div>
        </div>
        );
    }
}