import React, { useState, useEffect } from 'react';

import JobAppContainer from '../JobAppContainer';

import { makeStyles } from '@material-ui/core/styles';
import {
    Button,
    Grid,
    Card,
    CardContent,
    Typography,
    CardHeader,
    CardActions
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
    },
    card : {
        height: '200px',  
    },
    // cardAction : {
    //     display: 'flex',
    // }
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
                                    <Grid  item  xs={6} sm={3}  key={data.indexOf(elem)}>
                                        <Card className={classes.card}>
                                            <CardHeader
                                                title={`${elem.jobTitle} at ${elem.company}`}
                                               
                                            />
                                            <CardContent>
                                                <Typography variant="body2" color="inherit" component="p">
                                                    {elem.jobDescription.substring(0,50)} ...
                                                </Typography>
                                            </CardContent>
                                            <CardActions className={classes.cardAction} disableSpacing>
                                                <Button size="small" color="primary">
                                                    <Link to={`${url}/${elem.jobID}`} onClick={onClick}>LEARN MORE</Link>  
                                                </Button>                   
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