import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {Alert, AlertTitle} from '@material-ui/lab';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    '& > * + *': {
      marginTop: theme.spacing(2),
    },
  },
}));

const Reminder = ({ jobID }) => {
  const classes = useStyles();

  const [reminder, setReminder] = useState('none');
  const [keyword, setKeyword] = useState('task');

  useEffect(() => {
      
        fetch(`/jobapp/getTask/${jobID}`, {
            method: 'GET',
        })
        .then(resp => resp.json())
        .then(resp => {
          setReminder(resp);
          setKeyword(resp.taskKeyword)
        })
        .catch(err => console.log(err))
        
    }, [jobID]);

  return (
    <div className={classes.root}>
      <Alert severity="info">
        <strong>Daily Reminder: </strong>PLEASE RESOLVE TODO LIST TO MAKE ROOM FOR NEW TASKS 
      </Alert>
      {reminder === 'none' ? 
        null :  
        <Alert severity="warning">
          <AlertTitle>New Task Alert</AlertTitle>
          {reminder.company}'s {keyword}: <strong>{reminder.taskDate}</strong> 
        </Alert>
      }  
    </div>
  );
}

export default Reminder;