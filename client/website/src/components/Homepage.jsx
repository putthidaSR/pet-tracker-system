import '../App.css';
import React, { Component } from "react";
import { Layout } from 'antd';

import Navbar from "./Navbar";

export default class Homepage extends Component {
    
    render() {
        return (
            <div className="app">
                <div className="navbar">
                    <Navbar />
                </div>

                <div className="main">
                    <Layout>
                        <div className="routes">
                            <h1>Homepage</h1>
                        </div>

                    </Layout>
                </div>
            </div>
        );
    }
}