import React from 'react';
import {useMutation} from '@apollo/react-hooks';
import TextField from "@material-ui/core/TextField";
import {RemoveMutation} from "../../util/mutation";
import {MeQuery, SearchQuery, UserSearchQuery} from "../../util/graphql";


function DeleteButton(post_id) {


    const mutation = RemoveMutation;

    const [deletePostOrMutation, {loading}] = useMutation(mutation, {
            refetchQueries: [{query: UserSearchQuery, MeQuery}],
            variables: {id: String(Object.values(post_id))},
            onCompleted: (data) => {
                window.location.href = '/order';


            }
        }
    )

    return (
        <>

            <form action="#">

                <TextField type='submit'
                           onClick={deletePostOrMutation}
                           disabled={loading}
                           value="↳주문 취소"/>

            </form>

        </>
    );
}


export default DeleteButton;
