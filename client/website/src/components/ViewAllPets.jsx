import '../App.css';
import React, { Component } from "react";
import {
    Table,
    Header,
    HeaderRow,
    HeaderCell, Body,
    Row,
    Cell,
  } from '@table-library/react-table-library/table';

import Navbar from "./Navbar";

const list = [
    {
      id: '1',
      name: 'Chance',
      deadline: new Date(2020, 1, 17),
      type: 'Pitbull',
      isComplete: true,
    },
    {
      id: '2',
      name: 'Fluffy',
      deadline: new Date(2020, 2, 28),
      type: 'Husky',
      isComplete: true,
    },
    {
      id: '3',
      name: 'Bella',
      deadline: new Date(2020, 3, 8),
      type: 'Golden Retriever',
      isComplete: false,
    }
  ];

export default class ViewAllPets extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            search: ''
        }

    }

    handleSearch = (event) => {
        this.setState({search: event.target.value});
    }

    render() {
        const data = {
            nodes: list.filter((item) =>
                item.name.toLowerCase().includes(this.state.search.toLowerCase())
            ),
        };

        return (
            <div className="app">
            <div className="navbar">
                <Navbar />
            </div>

            <div className="centered">
                <label htmlFor="search">
                    Search by Name:
                    <input id="search" type="text" onChange={this.handleSearch} />
                </label>


                <Table data={data}>
                {(tableList) => (
                    <>
                        <Header>
                            <HeaderRow>
                                <HeaderCell>Name</HeaderCell>
                                <HeaderCell>Date Of Birth</HeaderCell>
                                <HeaderCell>Breed</HeaderCell>
                                <HeaderCell>Registered to App</HeaderCell>
                            </HeaderRow>
                        </Header>

                        <Body>
                            {tableList.map((item) => (
                            <Row key={item.id} item={item}>
                                <Cell>{item.name}</Cell>
                                <Cell>
                                {item.deadline.toLocaleDateString(
                                    'en-US',
                                    {
                                    year: 'numeric',
                                    month: '2-digit',
                                    day: '2-digit',
                                    }
                                )}
                                </Cell>
                                <Cell>{item.type}</Cell>
                                <Cell>{item.isComplete.toString()}</Cell>
                            </Row>
                            ))}
                        </Body>
                    </>
                )}
                </Table>
            </div>
        </div>
        );
    }
}