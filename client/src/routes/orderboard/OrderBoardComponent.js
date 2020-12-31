import React, {useEffect, useState} from 'react';
import {Column, Row} from 'simple-flexbox';
import {createUseStyles} from 'react-jss';
import MiniCardComponent from '../../components/cards/MiniCardComponent';
import OrderBoard from './OrderBoard';
import Task from './Task';
import {useQuery} from "@apollo/react-hooks";
import {AllUserQuery, CountQuery} from "../../graphql/query";

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


function OrderBoardComponent() {
    const classes = useStyles();

    const [contents, setContents] = useState('');
    const [sum, setSum] = useState('');
    const [count, setCount] = useState('');

    const {data: user} = useQuery(AllUserQuery)


    useEffect(() => {
        if (user) {
            setCount(user.allUsers.length);
        }
    }, [user]);

    const {data} = useQuery(CountQuery);
    useEffect(() => {
        if (data) {
            setContents(data.howmany);

        }
    }, [data]);

    return (
        <Column>
            <Row
                className={classes.cardsContainer}
                wrap
                flexGrow={1}
                horizontal='space-between'
                breakpoints={{768: 'column'}}
            >
                <Row
                    className={classes.cardRow}
                    wrap
                    flexGrow={1}
                    horizontal='space-between'
                    breakpoints={{384: 'column'}}
                >
                    <MiniCardComponent
                        className={classes.miniCardContainer}
                        title='주문'
                        value={contents[0]}
                    />
                    <MiniCardComponent
                        className={classes.miniCardContainer}
                        title='주문 취소'
                        value={contents[1]}
                    />
                </Row>
                <Row
                    className={classes.cardRow}
                    wrap
                    flexGrow={1}
                    horizontal='space-between'
                    breakpoints={{384: 'column'}}
                >
                    <MiniCardComponent
                        className={classes.miniCardContainer}
                        title='주문 포기'
                        value={contents[2]}
                    />

                    <MiniCardComponent
                        className={classes.miniCardContainer}
                        title='미주문'
                        value={count - parseInt(contents[0]) - parseInt(contents[1]) - parseInt(contents[2])}
                    />

                </Row>
            </Row>

            <Row
                horizontal='space-between'
                className={classes.lastRow}
                breakpoints={{1024: 'column'}}
            >
                <Task containerStyles={classes.tasks}/>
            </Row>

            <div className={classes.todayTrends}>
                <OrderBoard/>
            </div>

        </Column>
    );
}

export default OrderBoardComponent;
