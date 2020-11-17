import React, { useState, useEffect } from 'react';

import JobAppContainer from '../JobAppContainer';

import { makeStyles } from '@material-ui/core/styles';
import CardActions from '@material-ui/core/CardActions';
import {
    Grid,
    Card,
    CardContent,
    Typography,
    CardHeader
} from '@material-ui/core/';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link,
    useRouteMatch
} from "react-router-dom";


const useStyles = makeStyles(theme => ({
    root: {
        flexGrow: 1,
        padding: theme.spacing(2)
    }
}))

const CardList = ({ onClick }) => {
    const classes = useStyles()
    const [data, setData] = useState([]);

    let { url } = useRouteMatch();

    useEffect(() => { 
        fetch(`${process.env.REACT_APP_API_URL}/jobapp/getAllJobs`, {
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json',
            },
        })
        .then(resp => resp.json())
        .then(resp => setData(resp))
        .catch(err => console.log(err))      
    }, []);


    return (
        <Router>
            <Switch>
                <Route exact path={url} render={() => {
                    return (
                        <div className={classes.root}>
                            <Grid
                                container
                                spacing={1}
                                direction="row"
                                justify="space-between"
                                alignItems="flex-start"
                            >
                                {data.map(elem => (
                                    <Grid  item style={{ minWidth: "20em", minHeight: "20em"}} xs={6} sm={3}  key={data.indexOf(elem)}>
                                        <Card>
                                            <CardHeader
                                                title={`${elem.jobTitle} at ${elem.company}`}
                                                subheader={`JOB ID ${elem.jobID}`}
                                            />
                                            <CardContent>
                                                <Typography variant="body2" color="textSecondary" component="p">
                                                    {elem.jobDescription}
                                                </Typography>
                                            </CardContent>
                                            <CardActions disableSpacing>
                                                <Link to={`${url}/${elem.jobID}`} onClick={onClick}> more details</Link>                
                                            </CardActions>
                                        </Card>
                                    </Grid>
                                ))}
                            </Grid>
                        </div>
                    );
                }}/>

                <Route  
                    exact path={`${url}/:id`}
                    component={JobAppContainer}
                />
            </Switch>
        </Router>  
    );
}

export default CardList;