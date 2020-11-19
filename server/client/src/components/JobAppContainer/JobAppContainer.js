import React, { useState } from 'react';

import TaskBoard from '../TaskBoard';
import Reminder from './Reminder';
import JobDetailCard from './JobDetailCard';

import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';


const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  paper: {
    padding: theme.spacing(2),
    textAlign: 'center',
    color: theme.palette.text.secondary,
  },
}));

const JobAppContainer = ({ match }) => {

    const classes = useStyles();
    // eslint-disable-next-line

    const handleDelete = () => {
      fetch(`/jobapp/deleteJob/${match.params.id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type' : 'application/json',
        },
      })
      .then(resp => resp.json())
      .catch(err => console.log(err));
      
      window.location.href = '/dashboard';
    };
  
    return (
      <div className={classes.root}>
        <Grid container spacing={1}>
           <Grid  item xs={12}>
            <Button
              variant="contained" 
              color="primary"
              onClick={handleDelete}
            >
              DELETE APPLICATION
            </Button>
          </Grid>   
          <Grid style={{marginTop : '15px'}} item xs={12}>
            <Reminder jobID={match.params.id}/>
          </Grid>
          <Grid item xs={4}>
              <JobDetailCard jobID={match.params.id}/>
          </Grid> 
          <Grid item xs={8}>
            <TaskBoard/>
          </Grid>
        </Grid>
      </div>
    );
}

export default JobAppContainer; 