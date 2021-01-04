import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import Menu from "../layout/Menu";
import LineChart from 'react-svg-line-chart';
import {Column, Row} from "simple-flexbox";
import {lightBlue} from "@material-ui/core/colors";


const data = [];

for (let x = 1; x <= 24; x++) {
    data.push({x: x, y: Math.floor(Math.random() * 100)});
}

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        padding: "35px 0",
        borderTop: "1px solid #d9d9d9",
        fontsize: "12px",
        backgroundColor: "#f1f1f1",
        textAlign: "center",
    },
    paper: {
        padding: "100px",
        spacing: 1,
        textAlign: 'center',
        color: theme.palette.text.secondary,
        m: "3rm"

    }, paper1: {
        padding: "30px",
        spacing: 2,
        textAlign: 'center',
        color: theme.palette.text.secondary,
        marginTop:1,

    }, graphContainer: {
        marginLeft: 0,
        marginRight: 0,

        width: '100%',
        alignSelf: "center"
    }, h1: {
        textAlign: "left",
        font: "normal normal 600 18px/21px Apple SD Gothic Neo",
        letterSpacing: "0.2px",
        color: "#232328",
        opacity: 1,
        marginBottom:"15px",
        marginLeft:"15px",

    },
    legendTitle: {
        ...theme.typography.smallSubtitle,
        fontWeight: '600',
        color: "grey",
        marginLeft: 8
    },
}));

export default function CenteredGrid() {
    const classes = useStyles();

    function renderLegend(color, title) {
        return (
            <Row vertical='center'>
                <div style={{width: 20, border: '2px solid', marginLeft: 40, borderColor: color}}></div>
                <span className={classes.legendTitle}>{title}</span>
            </Row>
        );
    }

    return (
        <div>
            <Menu/>
            <div className="header">
                매출 관리
            </div>

            <div className={classes.root}>
                <Grid container spacing={3}>

                    <Grid item xs={8}>
                        <Paper className={classes.paper1}>
                            <div className={classes.graphContainer}>
                                <h1 className={classes.h1}>매출 추이</h1>
                                <h1 className={classes.h1}> {renderLegend("lightBlue", '총 매출액')}{renderLegend("yellow", '순수익')}</h1>

                                <LineChart
                                    data={data}
                                    viewBoxWidth={500}
                                    pointsStrokeColor={lightBlue}
                                    areaColor={lightBlue}
                                    areaVisible={true}
                                />
                            </div>
                        </Paper>
                    </Grid>
                    <Grid item xs={4}>
                        <Paper className={classes.paper}>xs=6</Paper>
                        <Paper className={classes.paper}>xs=6</Paper>
                    </Grid>
                    <Grid item xs={12}>
                        <Paper className={classes.paper}>xs=12</Paper>
                    </Grid>
                    <Grid item xs={3}>
                        <Paper className={classes.paper}>xs=3</Paper>
                    </Grid>
                    <Grid item xs={3}>
                        <Paper className={classes.paper}>xs=3</Paper>
                    </Grid>
                    <Grid item xs={3}>
                        <Paper className={classes.paper}>xs=3</Paper>
                    </Grid>
                    <Grid item xs={3}>
                        <Paper className={classes.paper}>xs=3</Paper>
                    </Grid>
                </Grid>
            </div>
        </div>
    );
}
