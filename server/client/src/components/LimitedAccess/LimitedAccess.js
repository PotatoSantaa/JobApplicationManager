import React, { useState, useEffect } from 'react';

import {
    Button,
    Grid,
    Card,
    CardHeader,
    CardActions,
    Avatar,
    IconButton,
    Chip
} from '@material-ui/core/';
import ChatIcon from '@material-ui/icons/Chat';
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

    const recruiters = [
        {
            name: 'Donald Trump',
            title: 'Campus Recruiter at POTUS'
        }, {
            name: 'Rich Brian',
            title: 'Campus Recruiter at Rising88'
        }, {
            name: 'Pewd Die Pie',
            title: 'Hiring Manager at Youtube'
        }, {
            name: 'Joji',
            title: 'Technical Recruiter at 88Rising'
        }, {
            name: 'Jake Paul',
            title: 'Senior Recruiter at Hype'
        }, {
            name: 'Logan Paul',
            title: 'Program Manager at Haus'
        }
    ];

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
            <Chip label="Premium Access" variant="outlined" color="primary" />
            <Grid container spacing={2} style={{paddingTop : '10px'}}>
                <Grid item xs={8}>
                    <Grid
                        container
                        spacing={3}
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
                </Grid>
                <Grid item xs={4}>
                    <Grid
                        container
                        spacing={2}
                        direction="column"
                        justify="space-between"
                    >
                        {recruiters.map(elem => (
                            <Grid  item  xs key={recruiters.indexOf(elem)}>
                                <Card className={classes.root}  >
                                    <CardHeader
                                        avatar={
                                        <Avatar aria-label="recipe" className={classes.avatar}>
                                            :)
                                        </Avatar>
                                        }
                                        action={
                                        <IconButton aria-label="settings">
                                            <ChatIcon />
                                        </IconButton>
                                        }
                                        title={elem.name}
                                        subheader={elem.title}
                                    />
                                </Card>
                            </Grid> 
                        ))}
                    </Grid> 
                </Grid>
            </Grid> 
        </div>
    );
};

export default LimitedAccess;