import React, {useEffect, useState} from 'react';
import {Column, Row} from 'simple-flexbox';
import {createUseStyles} from 'react-jss';
import MiniCardComponent from '../../components/cards/MiniCardComponent';
import TodayTrendsComponent from './PaymentBoard';
import TasksComponent from './TasksComponent';
import {useUserQuery} from '../../auth/useUserQuery';

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

function PaymentboardComponent() {
    const classes = useStyles();
    const [contents, setContents] = useState([]);
    const {data, loading} = useUserQuery();


    useEffect(() => {
        if (data) {
            setContents(data.contents);
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
                        title='Open'
                        value='40'
                    />
                    <MiniCardComponent
                        className={classes.miniCardContainer}
                        title='미주문자'
                        value='16'
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
                        title='주문자'
                        value='43'
                    />
                    <MiniCardComponent
                        className={classes.miniCardContainer}
                        title='취소자'
                        value='64'
                    />
                </Row>
            </Row>
            <Row
                horizontal='space-between'
                className={classes.lastRow}
                breakpoints={{1024: 'column'}}
            >
                <TasksComponent containerStyles={classes.tasks}/>
            </Row>

            <div className={classes.todayTrends}>
                <TodayTrendsComponent/>
            </div>

        </Column>
    );
}

export default PaymentboardComponent;
