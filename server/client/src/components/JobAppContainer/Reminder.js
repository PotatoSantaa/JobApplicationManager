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

const Reminder = () => {
  const classes = useStyles();

  const [reminder, setReminder] = useState([]);

  useEffect(() => {
      
        fetch(`${process.env.REACT_APP_API_URL}/jobapp/getAllTasks`, {
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json',
                'Origin' : 'http://localhost:3000'
            },
        })
        .then(resp => resp.json())
        .then(resp => setReminder(resp))
        .catch(err => console.log(err))
        
    }, []);

  return (
    <div className={classes.root}>
      <Alert severity="warning">@Twilio's coding assessment: "We are currently experiencing high\nvolumes so please allow 2-3 weeks to follow up regarding application\nstatus.\nYou have been invited to attend the test *University Recruiting for Summer\n2021 SWE Tests (B) " </Alert>
    </div>
  );
}

export default Reminder;