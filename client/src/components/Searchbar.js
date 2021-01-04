import React, {useEffect, useState} from 'react';
import {Row} from 'simple-flexbox';
import {createUseStyles, useTheme} from 'react-jss';


const useStyles = createUseStyles((theme) => ({
    addButton: {
        backgroundColor: theme.color.black,
        color: theme.color.grayishBlue2,
        fontSize: '15px !important',
        padding: '10px !important',
        width: "fit-content",
        marginLeft:"900px"
    },
    itemTitle: {
        ...theme.typography.itemTitle,
        color: theme.color.veryDarkGrayishBlue,
        width: "50%"
    },
    itemValue: {
        color: theme.color.grayishBlue2,
        width: "50%"
    },
    greyTitle: {
        color: theme.color.grayishBlue3
    },
    checkboxWrapper: {
        cursor: 'pointer',
        marginRight: 16
    },
    input: {
        color: theme.color.black,
        display: "block",
        width: "50%",
        padding: "20px 5px 20px",
        fontSize: '15px !important',
        fontFamily: "Open Sans",
        fontWeight: "600",
        border: "0",
        borderRadius: "3px",
        outline: 0,
        // textIndent: "70px",
        transition: "all .3s ease-in-out",
        margin: "0px auto",
        alignItems: "center",
        justifyContent: "center",
        alignSelf: "center",

    }

}));

function Task(props) {
    const theme = useTheme();
    const classes = useStyles({theme});


    function renderAddButton() {
        return (
            <Row
                horizontal='center'
                vertical='center'
                className={[classes.addButton].join(' ')}
                // onClick={create}
            >
                +
            </Row>
        );
    }

    return (
        <div className="hmenu">
            <Row horizontal='space-between' vertical='center'>

                {renderAddButton()}

                <span className={[classes.itemTitle, classes.greyTitle].join(' ')}>
                        <input type="text" placeholder="품목. 상품 위치, 가격 등 검색"
                               className={classes.input}/>
                    </span>

            </Row>
        </div>
    );
}


export default Task;
