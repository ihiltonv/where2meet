import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

const CustomTableCell = withStyles(theme => ({
    head: {
        backgroundColor: theme.palette.common.black,
        color: theme.palette.common.white,
        fontSize: 20
    },
    body: {
        fontSize: 15,
    },
}))(TableCell);

const styles = theme => ({
        root: {
            width: '100%',
            height: '100%',
            marginTop: theme.spacing.unit * 3,
            overflowX: 'auto',
            overflowY: 'auto',
        },
        table: {
            minWidth: 500,
        },
        row: {
            '&:nth-of-type(odd)': {
                backgroundColor: theme.palette.background.default,
            },
            '&:nth-of-type(1)': {
                backgroundColor: "lawngreen",
            },
            '&:nth-of-type(2)': {
                backgroundColor: "limegreen ",
            },
            '&:nth-of-type(3)': {
                backgroundColor: "lightgreen",
            },
            maxHeight: "100px"
        },
    })
;

let id = 0;

function createData(venue, votes, location, url) {
    id += 1;
    return {id, venue, votes, location, url};
};

function createRows(data) {
    let dataRows = [];
    for (let i in data) {
        const item = data[i];
        dataRows.push(createData(item.venue, item.votes, item.location, item.url))
    }
    return dataRows;
}

function CustomizedTable(props) {
    const {classes, data} = props;
    id = 0;
    const rows = createRows(data);

    return (
        <Paper className={classes.root}>
            <Table className={classes.table}>
                <TableHead>
                    <TableRow>
                        <CustomTableCell>Rank</CustomTableCell>
                        <CustomTableCell align="center">Venue</CustomTableCell>
                        <CustomTableCell align="center">Votes</CustomTableCell>
                        <CustomTableCell align="center">Location</CustomTableCell>
                        <CustomTableCell align="center">URL</CustomTableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {rows.map(row => (
                        <TableRow className={classes.row} key={row.id}>
                            <CustomTableCell align={"center"}>
                                {row.id}
                            </CustomTableCell>
                            <CustomTableCell align="center">{row.venue}</CustomTableCell>
                            <CustomTableCell align="center">{row.votes}</CustomTableCell>
                            <CustomTableCell align="center">{row.location}</CustomTableCell>
                            <CustomTableCell align="center">{row.url}</CustomTableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Paper>
    );
}

CustomizedTable.propTypes = {
    classes: PropTypes.object.isRequired,
    data: [],
};

export default withStyles(styles)(CustomizedTable);