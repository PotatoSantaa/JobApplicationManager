import React, { useState, useEffect } from 'react';

import { makeStyles } from '@material-ui/core/styles';

import {
    Card,
    CardContent,
    Typography,
    CardHeader
} from '@material-ui/core/';

const useStyles = makeStyles((theme) => ({
    card: {
        backgroundColor: theme.palette.primary.main,
        color: theme.palette.primary.contrastText
    },
}));

const JobDetailCard = ({ jobID }) => {

    const classes = useStyles();

    const [jobApp, setJobApp] = useState([]);

    useEffect(() => {
        fetch(`${process.env.REACT_APP_API_URL}/jobapp/getJob/${jobID}`, {
            method: 'GET',
        })
        .then(resp => resp.json())
        .then(resp => setJobApp(resp))
        .catch(err => console.log(err))
    // eslint-disable-next-line        
    }, []);
  
    return (
        <Card className={classes.card}>
            <CardHeader
                title={jobApp.jobTitle}
                subheader={`JOB ID ${jobApp.jobID}`}
            />
            <CardContent>
                <Typography variant="body2" color="inherit" component="p">
                 {jobApp.jobDescription}
                </Typography>
            </CardContent>
        </Card>
    );
}

export default JobDetailCard; 