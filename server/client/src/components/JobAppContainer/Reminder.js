import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Alert from '@material-ui/lab/Alert';

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
      
        fetch(`${process.env.REACT_APP_API_URL}/jobapp/getTask/${jobID}`, {
            method: 'GET',
        })
        .then(resp => resp.json())
        .then(resp => {
          setReminder(resp);
          setKeyword(resp.taskKeyword)
        })
        .catch(err => console.log(err))
        
    }, []);

  return (
    <div className={classes.root}>
      {reminder === 'none' ? 
        null :  <Alert severity="warning">REMINDER {reminder.company}'s {keyword}: "{reminder.taskDate}" </Alert>
      }  
    </div>
  );
}

export default Reminder;