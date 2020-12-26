import React, { useEffect, useState } from 'react';
import { Column, Row } from 'simple-flexbox';
import { createUseStyles } from 'react-jss';
import UserBoard from './UserBoard';
import { useAuthToken } from '../../auth/authToken';
import { useUserQuery } from '../../auth/useUserQuery';

const useStyles = createUseStyles({
    cardsContainer: {
        marginRight: -30,
        marginTop: -30
    },
    cardRow: {
        marginTop: 30,
        '@media (max-width: 768px)': {
            marginTop: 0
        }
    },
    miniCardContainer: {
        flexGrow: 1,
        marginRight: 30,
        '@media (max-width: 768px)': {
            marginTop: 30,
            maxWidth: 'none'
        }
    },
    todayTrends: {
        marginTop: 30
    },
    lastRow: {
        marginTop: 30
    },
    unresolvedTickets: {
        marginRight: 30,
        '@media (max-width: 1024px)': {
            marginRight: 0
        }
    },
    tasks: {
        marginTop: 0,
        '@media (max-width: 1024px)': {
            marginTop: 30
        }
    }
});

function UserboardComponent() {
    const classes = useStyles();
    const [contents, setContents] = useState([]);
    const { data, loading } = useUserQuery();

    console.log(data);


    useEffect(() => {
        if (data) {
            setContents(data.contents);
        }
    }, [data]);

    console.log(contents);

    console.log(contents &&
        contents.map((content) => (
            content._id

        ))
    );

    return (
        <Column>
            <div className={classes.todayTrends}>
                <UserBoard />
            </div>

        </Column>
    );
}

export default UserboardComponent;
