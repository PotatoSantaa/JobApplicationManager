import React, { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../Auth/Auth'
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
    const { user } = useContext(AuthContext);    
    const classes = useStyles();

    const [jobApp, setJobApp] = useState([]);

    useEffect(() => {
        if (user) {
            getJob(user.uid)
        }        
    // eslint-disable-next-line        
    }, []);

    const getJob = (userID) => {
        fetch(`/jobapp/getJob/${userID}/${jobID}`, {
            method: 'GET',
        })
        .then(resp => resp.json())
        .then(resp => setJobApp(resp))
        .catch(err => console.log(err))
    }
  
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