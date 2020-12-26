import * as React from 'react';
import { useMediaQuery } from '@material-ui/core';
import { SimpleList, List, Datagrid, EmailField, TextField } from 'react-admin';

export const UserList = props => {
    const isSmall = useMediaQuery(theme => theme.breakpoints.down('sm'));

    return (
	    /*
        <List title="All users" {...props}>
            {isSmall ? (
                <SimpleList
                    primaryText={record => record.name}
                    secondaryText={record => record.username}
                    tertiaryText={record => record.email}
                />
            ) : (
                <Datagrid>
                    <TextField source="id" />
                    <TextField source="name" />
                    <TextField source="username" />
                    <EmailField source="email" />
                </Datagrid>
            )}
<img src={require("images/jsoo.png")} />  
	    */
	    <>
	    <img src={process.env.PUBLIC_URL + '/jsoo.PNG'} width="150" height="120"/>

	    <h2> Category - </h2>
	    <h2> 남은수량 - </h2>
	    <h2> Category1 - </h2>
	    <h2> Category2 - </h2>
	    <h2> Category3 - </h2>
	    <h2> Category4 - </h2>
	    <h2> Category5 - </h2>
	    </>
       // </List>
    );
};
