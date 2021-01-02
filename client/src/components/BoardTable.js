import React, {useEffect, useState} from 'react';
import {useQuery} from "@apollo/react-hooks";
import {FETCH_POSTS_QUERY} from "../util/graphql"

function BoardTable() {




    return (


        <table className="employees-table">
            <thead className="employees-table-head">
            <tr>
                <th>ID</th>
                <th>Content</th>
                <th>CreatedAt</th>
                <th>Title</th>
                <th></th>
            </tr>
            </thead>
            <tbody className="employees-table-body">


            </tbody>


        </table>


    )
}

export default BoardTable;
