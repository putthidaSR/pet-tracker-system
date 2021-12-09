import '../App.css';
import React, { useState, useEffect } from 'react';
import Table from "./Table";
import axios from "axios";
import Navbar from "./Navbar";

/**
 * Render the component to display all veterinarians in tabular format.
 */
function Homepage() {

    // data state; initial value is an empty array
    const [data, setData] = useState([]);

    // Call the API once the component is mounted and set the data to be used locally
    useEffect(() => {
        (
            async () => {

                // Call the backend API to get all of all vets
                await axios({
                    method: 'GET',
                    url: 'http://localhost:8080/PawTracker/users/vet',
                    mode: 'no-cors'
                })
                .then((response) => {

                    console.log('success');
                    console.log(response.data.result);
                    setData(response.data.result);
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
        )();
    }, []);

    /**
     * Call the API to delete the specified user based on ID
     */
    const handleDeleteUser = async (event) => {

        // Call the backend API to get all of all vets
        await axios({
            method: 'DELETE',
            url: 'http://localhost:8080/PawTracker/users/' + event.target.value,
            mode: 'no-cors'
        })
        .then((response) => {

            console.log('success');
            window.location.reload(false);

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

    /**
     * List of columns in the table and its unique accessor (key) to access data
     */
    const columns = React.useMemo(
        () => [
            {
                Header: "ID",
                accessor: "user_id"
            },
            {
                Header: "Username",
                accessor: "username"
            },
            {
                Header: "Badge Number ",
                accessor: "badgeNumber"
            },
            {
                Header: "Email",
                accessor: "email"
            },
            {
                Header: "Phone Number",
                accessor: "phoneNumber"
            },
            {
                Header: "Address",
                accessor: "address"
            },
            {
                width: 300,
                Header: "Delete User",
                Cell: ({ cell }) => (
                <button style={{width: 100, backgroundColor: 'red', color: 'white'}} value={cell.row.values.user_id} onClick={handleDeleteUser}>
                    Delete
                </button>
                )
              }
        ],
        []
    );

    return (
        <div>
            {/** Display navigation menu bar */}
            <Navbar />

            <div><h3 style={{marginTop: 50, marginBottom: 20, color: '#F5C945'}}>List of All Veterinarians in the Paw Tracker System</h3></div>
            {/** Display table */}
            <Table columns={columns} data={data} />
        </div>
    );
}

export default Homepage;