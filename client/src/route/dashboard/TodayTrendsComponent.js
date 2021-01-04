import React from 'react';
import {Column, Row} from 'simple-flexbox';
import {createUseStyles, useTheme} from 'react-jss';
import LineChart from 'react-svg-line-chart';

const data = [];

for (let x = 1; x <= 24; x++) {
    data.push({x: x, y: Math.floor(Math.random() * 100)});
}

const useStyles = createUseStyles((theme) => ({
    container: {
        backgroundColor: '#FFFFFF',
        border: `1px solid ${theme.color.lightGrayishBlue2}`,
        borderRadius: 4,
        cursor: 'pointer'
    },
    graphContainer: {
        marginLeft: 0,
        marginRight: 0,
        width: '60%',
        alignSelf: "center"
    },
    graphSection: {
        padding: 20
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
        padding: '24px 32px 24px 32px',
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
        textAlign: 'center',
        color: theme.color.veryDarkGrayishBlue
    }
}));

function TodayTrendsComponent() {
    const theme = useTheme();
    const classes = useStyles({theme});

    function renderLegend(color, title) {
        return (
            <Row vertical='center'>
                <div style={{width: 20, border: '2px solid', marginLeft: 40, borderColor: color}}></div>
                <span className={classes.legendTitle}>{title}</span>
            </Row>
        );
    }

    function renderStat(title, value) {
        return (
            <Column
                flexGrow={1}
                className={classes.statContainer}
                vertical='center'
                horizontal='center'
            >
                <span className={classes.statTitle}>{title}</span>
                <span className={classes.statValue}>{value}</span>
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
                flexGrow={5}
                flexBasis='735px'
                className={classes.graphSection}
                breakpoints={{1024: {width: 'calc(100% - 48px)', flexBasis: 'auto'}}}
            >
                <Row wrap horizontal='space-between'>
                    <Column>
                        <span className={classes.graphTitle}>매출추이</span>
                        <span
                            className={classes.graphSubtitle}>{renderLegend(theme.color.lightBlue, '총 매출액')}{renderLegend(theme.color.yellow, '순수익')}</span>
                    </Column>
                </Row>
                <div className={classes.graphContainer}>
                    <LineChart
                        data={data}
                        viewBoxWidth={500}
                        pointsStrokeColor={theme.color.lightBlue}
                        areaColor={theme.color.lightBlue}
                        areaVisible={true}
                    />
                </div>
            </Column>
            <Column className={classes.separator} breakpoints={{1024: {display: 'none'}}}>
                <div/>
            </Column>
            <Column flexGrow={3} flexBasis='342px' breakpoints={{1024: classes.stats}}>
                {renderStat('총 매출액', '970,403 원')}
                {renderStat('순수익', '560,302 원')}
            </Column>
        </Row>
    );
}

export default TodayTrendsComponent;
