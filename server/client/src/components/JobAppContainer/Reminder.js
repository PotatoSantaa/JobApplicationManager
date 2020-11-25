import React, { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../Auth/Auth'
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
  const { user } = useContext(AuthContext);    
  const classes = useStyles();

  const [reminder, setReminder] = useState('none');
  const [keyword, setKeyword] = useState('task');

  useEffect(() => {
      if (user) {
        getTask(user.uid);
      }
    }, [jobID]);

  const getTask = (userID) => {
    fetch(`/jobapp/getTask/${userID}/${jobID}`, {
        method: 'GET',
    })
    .then(resp => resp.json())
    .then(resp => {
      setReminder(resp);
      setKeyword(resp.taskKeyword)
    })
    .catch(err => console.log(err))
  }

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