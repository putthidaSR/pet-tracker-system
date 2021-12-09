import React, { useState } from 'react';
import { useTable, useFilters } from "react-table";
import styled from 'styled-components'

/**
 * This component will render the table to view all veterinarians.
 * 
 * Reference: https://blog.logrocket.com/complete-guide-building-smart-data-table-react/
 */
export default function Table({ columns, data }) {

    // Create a state
    const [filterInput, setFilterInput] = useState("");

    // Update the state when input changes
    const handleFilterChange = e => {
        console.log(e.target.value);
        const value = e.target.value || undefined;
        setFilter("badgeNumber", value); // Update the show.name filter. Now our table will filter and show only the rows which have a matching value
        setFilterInput(value);
      };

    // Use the useTable Hook to send the columns and data to build the table
    const {
      getTableProps, // table props from react-table
      getTableBodyProps, // table body props from react-table
      headerGroups, // headerGroups, if your table has groupings
      rows, // rows for the table based on the data passed
      prepareRow, // Prepare the row (this function needs to be called for each row before getting the row props)
      setFilter // The useFilter Hook provides a way to set the filter
    } = useTable(
        {
            columns,
            data
        },
        useFilters // Adding the useFilters Hook to the table
    );
  
    /* 
      Render the UI for your table
    */
    return (
        <div className="container" style={{justifyContent: 'center', alignContent: 'center'}}>

            <input
                style={{ width: '100%', height: '50px' }}
                value={filterInput}
                onChange={handleFilterChange}
                placeholder={"Search by badge number"}/>
        
            <Styles>
            
                <table {...getTableProps()}>

                    <thead>
                    {headerGroups.map(headerGroup => (
                        <tr {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map(column => (
                            <th {...column.getHeaderProps()}>{column.render("Header")}</th>
                        ))}
                        </tr>
                    ))}
                    </thead>
        
                    <tbody {...getTableBodyProps()}>

                        {rows.map((row, i) => {
                            
                            // prepare the rows and get the row props from react-table dynamically
                            prepareRow(row);
                            return (
                            <tr {...row.getRowProps()}>
                                {row.cells.map(cell => {
                                return <td {...cell.getCellProps()}>{cell.render("Cell")}</td>;
                                })}
                            </tr>
                            );
                        })}
                    </tbody>
                </table>
            </Styles>
        </div>
    );
}

// Styling for the table
const Styles = styled.div`
  padding: 10rem;

  display: block;
  max-width: 100%;

  /* This will make the table scrollable when it gets too small */
  .tableWrap {
        display: block;
        max-width: 100%;
        overflow-x: scroll;
        overflow-y: hidden;
        border-bottom: 1px solid black;
  }

  table {

    /* Make sure the inner table is always as wide as needed */
    width: 100%;
    border-spacing: 0;
    border: 2px solid black;
    background-color: white;

    tr {
        :last-child {
            td {
                border-bottom: 0;
            }
        }
    }

    th {
        background-color: #F5C945;
        border: 1px solid black;
        padding: 1rem;
        font-size: 18px;
    },
    td {
        margin: 0;
        padding: 0.5rem;
        border-bottom: 1px solid black;
        border-right: 1px solid black;

        :last-child {
            border-right: 0;
        }
    }
  }
`
