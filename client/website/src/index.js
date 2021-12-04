/**
 * This is the starting point of the website.
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
