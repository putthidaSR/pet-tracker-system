/**
 * This is the starting point of the website where we register the root element to DOM and initialize bootstrap.
 * 
 * @author Putthida Samrith
 * @date 12/9/2021
 */
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import "./index.css";
import "./App.css";

import App from './App';

import 'antd/dist/antd.css';

ReactDOM.render(
    <React.StrictMode>
        <Router>
            <App />
      </Router>
    </React.StrictMode>,
    document.getElementById('root')
);
