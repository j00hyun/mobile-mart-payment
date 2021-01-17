import React from 'react';
import {Column} from 'simple-flexbox';
import {createUseStyles, useTheme} from 'react-jss';

const useStyles = createUseStyles((theme) => ({
    container: {
        backgroundColor: '#f6f6f6',
        border: `3px solid ${theme.color.lightGrayishBlue2}`,
        borderRadius: 4,
        cursor: 'pointer',
        maxWidth: 350,
        padding: '16px 32px 16px 32px',
        '&:hover': {
            borderColor: theme.color.darkRed,
            '&:nth-child(n) > span': {
                color: theme.color.darkRed
            }
        }
    },
    title: {
        ...theme.typography.cardTitle,
        marginBottom: 12,
        minWidth: 102,
        textAlign: 'center'
    },
    value: {
        color: theme.color.veryDarkGrayishBlue,
        fontSize: 40,
        letterSpacing: '1px',
        lineHeight: '50px',
        textAlign: 'center'
    }
}));

function MiniCardComponent({className = '', title, value}) {
    const theme = useTheme();
    const classes = useStyles({theme});
    const composedClassName = [classes.container, className].join(' ');
    return (
        <Column flexGrow={1} className={composedClassName} horizontal='center' vertical='center'>
            <span className={classes.title}>{title}</span>
            <span className={classes.value}>{value}</span>
        </Column>
    );
}

export default MiniCardComponent;
