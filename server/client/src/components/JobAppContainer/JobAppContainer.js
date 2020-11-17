import React, { useState, useEffect } from 'react';

import TaskBoard from '../TaskBoard';
import Reminder from '../JobAppContainer/Reminder';

import { makeStyles } from '@material-ui/core/styles';
import {
    Grid,
    Card,
    CardContent,
    Typography,
    CardHeader
} from '@material-ui/core/';

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

    const [jobApp, setJobApp] = useState();

    // var bodyFormData = new FormData();
    // bodyFormData.append('jobId', `${match.params.id}`);

    // console.log(match.params.id);

    // useEffect(() => {
      
    //     fetch(`${process.env.REACT_APP_API_URL}/jobapp/getJob`, {
    //         method: 'GET',
    //         headers: {
    //             'Content-Type' : 'multipart/form-data',
    //             'Origin' : 'http://localhost:3000'
    //         },
    //         jobId: `${match.params.id}` 
    //     })
    //     .then(resp => resp.json())
    //     .then(resp => setJobApp(resp))
    //     .catch(err => console.log(err))
        
    // }, []);
  
    return (
      <div className={classes.root}>
        <Grid container spacing={1}>
          <Grid style={{marginTop : '15px'}} item xs={12}>
            <Reminder jobID={match.params.id}/>
          </Grid>
          <Grid item xs={4}>
          <Grid  item style={{ minWidth: "20em", minHeight: "20em"}} xs={6} sm={3} >
                                        <Card>
                                            <CardHeader
                                                title="Twilio"
                                                subheader='JOB ID 10005'
                                            />
                                            <CardContent>
                                                <Typography variant="body2" color="textSecondary" component="p">
                                                • 7+ years of in-depth, hands-on software development experience in a modern programming language (Go (preferred), Python, Java, Ruby or C++). Scala is a plus.
                                                • Demonstrated success in building and maintaining reliable, robust, and performant software that tolerates and recovers from unreliable dependencies throughout the entire software lifecycle.
                                                • Clear ability to translate architectural concepts into concrete system designs to address product and business needs, and able to lead the execution of key initiatives for the team.
                                                • Experience in coaching and mentoring junior engineers and sharing proven standard methodologies.
                                                • Expert OO experience and software engineering expertise (coding, automated tests, profiling, monitoring, etc).
                                                • Proficiencies with MySQL, Redis and Kafka. DynamoDB a plus.
                                                • Experience working with Docker and Kubernetes.
                                                • Knowledge of AWS or other cloud computing services is a plus.
                                                </Typography>
                                            </CardContent>
                                        </Card>
                                    </Grid>
          </Grid>
          <Grid item xs={8}>
            <TaskBoard/>
          </Grid>
        </Grid>
      </div>
    );
}

export default JobAppContainer; 