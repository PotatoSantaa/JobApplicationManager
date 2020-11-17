import React, { useState } from 'react';

import TaskBoard from '../TaskBoard';
import Reminder from './Reminder';
import JobDetailCard from './JobDetailCard';

import Grid from '@material-ui/core/Grid';
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
    const [jobApp, setJobApp] = useState();
  
    return (
      <div className={classes.root}>
        <Grid container spacing={1}>
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