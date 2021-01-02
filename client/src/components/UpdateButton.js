import React, {useEffect, useState} from 'react';
import gql from 'graphql-tag';
import {useMutation, useQuery} from '@apollo/react-hooks';
import {FETCH_POSTS_QUERY} from '../util/graphql';
import TextField from "@material-ui/core/TextField";
import {UPDATEMUTATION} from "../util/mutation";


function UpdateButton(post_id) {

    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');

    const mutation = UPDATEMUTATION;

    const [update, {loading}] = useMutation(mutation, {
            refetchQueries: [{query: FETCH_POSTS_QUERY, variables: {index: 1}}],
            variables: {
                title: title,
                content: content,
                id: String(Object.values(post_id)),
            }
        }
    )


    return (

        <>

            <tr style={{marginBottom: 20}}>

                <td><input type="text" placeholder="content" onChange={e => setContent(e.target.value)}/></td>
                <td><input type="text" placeholder="title" onChange={e => setTitle(e.target.value)}/></td>
                <td><TextField type='submit'
                               onClick={update}
                               disabled={loading}
                               value="↳Update"/></td>
            </tr>
        </>


    );

}


export default UpdateButton;
