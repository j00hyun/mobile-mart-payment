import React, {useState} from 'react';
import {useMutation, useQuery} from '@apollo/react-hooks';
import {FETCH_POSTS_QUERY} from '../util/graphql';
import TextField from "@material-ui/core/TextField";
import {CREATEMUTATION} from "../util/mutation";


function CreateButton() {

    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const createmutation = CREATEMUTATION;


    const [create, {loading}] = useMutation(createmutation, {
            refetchQueries: [{query: FETCH_POSTS_QUERY, variables: {index: 1}}],
            variables: {
                title: title,
                content: content
            },
        }
    )

    return (
        <table className="employees-table">
            <thead className="employees-table-head">

            <tr style={{marginBottom: 20, marginLeft: 40}}>
                <th>Content</th>
                <th>Title</th>
                <th>Create</th>


            </tr>
            </thead>
            <tbody className="employees-table-body">

            <tr style={{marginBottom: 20}}>

                <td><input type="text" placeholder="content" onChange={e => setContent(e.target.value)}/></td>
                <td><input type="text" placeholder="title" onChange={e => setTitle(e.target.value)}/></td>
                <td><TextField type='submit'
                               onClick={create}
                               disabled={loading}
                               value="↳Create"/></td>

            </tr>
            </tbody>
        </table>


    );

}


export default CreateButton;
