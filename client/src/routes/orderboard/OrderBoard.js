import React, {useEffect, useState} from 'react';
import {Column, Row} from 'simple-flexbox';
import {createUseStyles, useTheme} from 'react-jss';
import BoardTable from '../../components/table/BoardTable';
import {CreateMutation, OrderResetMutation} from "../../graphql/mutation";
import {MeQuery, SearchQuery} from "../../graphql/query";
import {useQuery, useMutation} from "@apollo/react-hooks";
import {TextField} from "@material-ui/core";


const useStyles = createUseStyles((theme) => ({
    container: {
        backgroundColor: '#FFFFFF',
        border: `1px solid ${theme.color.lightGrayishBlue2}`,
        borderRadius: 4,
        cursor: 'pointer'
    },
    graphContainer: {
        marginTop: 24,
        marginLeft: 0,
        marginRight: 0,
        width: '100%'
    },
    graphSection: {
        padding: 24
    },
    graphSubtitle: {
        ...theme.typography.smallSubtitle,
        color: theme.color.grayishBlue2,
        marginTop: 4,
        marginRight: 8
    },
    graphTitle: {
        ...theme.typography.cardTitle,
        color: theme.color.veryDarkGrayishBlue
    },
    legendTitle: {
        ...theme.typography.smallSubtitle,
        fontWeight: '600',
        color: theme.color.grayishBlue2,
        marginLeft: 8
    },
    separator: {
        backgroundColor: theme.color.lightGrayishBlue2,
        width: 1,
        minWidth: 1
    },
    statContainer: {
        borderBottom: `1px solid ${theme.color.lightGrayishBlue2}`,
        padding: '48px 64px 48px 64px',
        height: 'calc(114px - 48px)',
        '&:last-child': {
            border: 'none'
        }
    },
    stats: {
        borderTop: `1px solid ${theme.color.lightGrayishBlue2}`,
        width: '100%'
    },
    statTitle: {
        fontWeight: '600',
        fontSize: 16,
        lineHeight: '22px',
        letterSpacing: '0.3px',
        textAlign: 'center',
        color: theme.color.grayishBlue2,
        whiteSpace: 'nowrap',
        marginBottom: 6
    },
    statValue: {
        ...theme.typography.title,
        textAlign: 'left',
        color: theme.color.veryDarkGrayishBlue
    },
    statValue2: {
        ...theme.typography.title,
        textAlign: 'right',
        color: theme.color.veryDarkGrayishBlue
    }
}));

function OrderBoard() {
    const theme = useTheme();
    const classes = useStyles({theme});
    const [id, setId] = useState();
    const [menu, setMenu] = useState();
    const [hi, setHi] = useState();
    const [status, setStatus] = useState();
    const [username, setName] = useState();

    const {data} = useQuery(MeQuery);


    useEffect(() => {
        if (data) {
            setName(data.me.username);
            setId(data.me.idNum);
            setStatus(data.me.status);

        }
    }, [data]);


    const createmutation = CreateMutation;


    const [create, error] = useMutation(createmutation, {
            refetchQueries: [{query: SearchQuery, MeQuery}],
            variables: {
                username: username,
                menu: menu,
                hi: hi
            },
            onCompleted: (data) => {
                window.location.href = '/order';


            },
            onError: () => {
                alert("메뉴를 선택해주세요.")
            },
        }
    )

    const [giveup] = useMutation(OrderResetMutation, {
            refetchQueries: [{query: SearchQuery, MeQuery}],
            onCompleted: (data) => {
                window.location.href = '/order';


            }
        }
    )


    function renderStat(title, value, value2) {
        return (

            <Column
                flexGrow={1}
                className={classes.statContainer}
                vertical='center'
                horizontal='center'
            >
                <span className={classes.statTitle}>{title}</span>
                <span className={classes.statValue}>{value}</span>
                <span className={classes.statValue2}>{value2}</span>
            </Column>
        );
    }


    return (

        <Row
            flexGrow={1}
            className={classes.container}
            horizontal='center'
            breakpoints={{1024: 'column'}}
        >
            <Column
                wrap
                flexGrow={7}
                flexBasis='735px'
                className={classes.graphSection}
                breakpoints={{1024: {width: 'calc(100% - 48px)', flexBasis: 'auto'}}}
            >
                <BoardTable/>
            </Column>
            <Column className={classes.separator} breakpoints={{1024: {display: 'none'}}}>
                <div/>
            </Column>
            <Column flexGrow={3} flexBasis='342px' breakpoints={{1024: classes.stats}}>
                {renderStat('Select 버튼을 클릭하세요!', '주문하기')}
                {renderStat('☕ 아메리카노 ☕', <TextField type='submit'
                                                    onClick={() => {
                                                        setMenu("아메리카노")
                                                        setHi("hot")
                                                    }}
                                                    value="hot"/>,
                    <TextField type='submit'
                               onClick={() => {
                                   setMenu("아메리카노")
                                   setHi("ice")
                               }}
                               value="Ice"/>)}


                {renderStat('☕ 카페모카 ☕', <TextField type='submit'
                                                   onClick={() => {
                                                       setMenu("카페모카")
                                                       setHi("hot")
                                                   }}
                                                   value="Hot"/>,
                    <TextField type='submit'
                               onClick={() => {
                                   setMenu("카페모카")
                                   setHi("ice")
                               }}
                               value="Ice"/>)}

                {renderStat('☕ 아이스티 ☕', <TextField type='submit'
                                                   onClick={() => {
                                                       setMenu("아이스티")
                                                       setHi("ice")
                                                   }}
                                                   value="Ice"/>,
                )}


                {renderStat('☕ 바닐라라떼 ☕', <TextField type='submit'
                                                    onClick={() => {
                                                        setMenu("바닐라라떼")
                                                        setHi("hot")
                                                    }}
                                                    value="Hot"/>,
                    <TextField type='submit'
                               onClick={() => {
                                   setMenu("바닐라라떼")
                                   setHi("ice")
                               }}
                               value="Ice"/>)}


                {status != "주문완료" &&
                renderStat(<TextField type='submit'
                                      onClick={create}
                                      value="Select"/>, <TextField type='submit'
                                                                   onClick={giveup}
                                                                   value="주문 포기"/>)}

                {status == "주문완료" &&
                renderStat("주문 취소는 유저 페이지에서 가능", "주문 완료")}

            </Column>
        </Row>


    );


}

export default OrderBoard;
