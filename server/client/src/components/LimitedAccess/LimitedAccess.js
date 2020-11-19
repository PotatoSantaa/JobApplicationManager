import React, { useState, useEffect } from 'react';

import {
    Button,
    Grid,
    Card,
    CardHeader,
    CardActions
} from '@material-ui/core/';
import { makeStyles } from '@material-ui/core/styles';

import '../../styles/UserDashboard.css';

const useStyles = makeStyles(theme => ({
    root: {
        flexGrow: 1,
        padding: theme.spacing(2)
    },
    card : {
        height: '200px',
        alignItems: 'flex-start'
    },
}));

const LimitedAccess = () => {

    const classes = useStyles();
    const [listing, setListing] = useState([]);

    useEffect(() => {
        fetch(`/api/indeed`, {
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json',
            }
        })
        .then(resp => resp.json())
        .then(resp => setListing(resp))
        .catch(err => console.log(err))   
        // eslint-disable-next-line
    }, []);


    return (
        <div className="UserDashboard">
            <div className={classes.root}>
                <Grid
                    container
                    spacing={1}
                    direction="row"
                    justify="space-between"
                    alignItems="flex-start"
                >
                    {listing.map(elem => (
                        <Grid  item  xs={6} sm={3}  key={listing.indexOf(elem)}>
                            <Card className={classes.card}>
                                <CardHeader
                                    title={`${elem[0]}`}
                                    subheader='Not Applied'
                                />
                                <CardActions  disableSpacing>
                                    <Button size="small" color="primary" href={`${elem[1]}`}>
                                        Apply Externally 
                                    </Button>                   
                                </CardActions>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
            </div>
        </div>
    );
};

export default LimitedAccess;