import React, { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../Auth/Auth'
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

import CircularProgress from '@material-ui/core/CircularProgress';

const useStyles = makeStyles(theme => ({
    root: {
        flexGrow: 1,
        padding: theme.spacing(2)
    },
    card : {
        height: '200px',  
    },
    loading: {
        display: 'flex',
        '& > * + *': {
          marginLeft: theme.spacing(5),          
        },
    },
}))

const CardList = ({...props}) => {
    const { user } = useContext(AuthContext);    
    const {
        onClick,
        updateCardList,
        setUpdateCardList
    } =  props;

    const classes = useStyles()
    const [data, setData] = useState([]);
    const [initialized, setInitialized] = useState(false);
    let { url } = useRouteMatch();

    useEffect(() => {
        if (!initialized) {
            getAllJobs();            
        }
    }, [initialized, updateCardList])

    useEffect(() => {
        if (user && updateCardList) {            
            getAllJobs(user.uid);            
        }
    }, [user, updateCardList])
    
    const getAllJobs = (userID) => {                
        fetch(`/jobapp/getAllJobs/${userID}`, {
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json',
            },
        })
        .then(resp => resp.json())
        .then(resp => {
            console.log(JSON.stringify(userID));
            setData(resp)
            setInitialized(true)
            setUpdateCardList(false)
        })        
        .catch(err => console.log(err))    
    }

    return (
        <Router>
            <Switch>
                <Route exact path={url} render={() => {
                    return (
                        <React.Fragment>
                            { initialized ? (
                                <div className={classes.root} >
                                    <Grid
                                        container
                                        spacing={1}
                                        direction="row"
                                        justify="space-between"
                                        alignItems="flex-start"
                                    >                                
                                        {data && data.length !== 0 && data.map(elem => (
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
                            ) : (
                                <div style={{ display: 'flex', justifyContent: 'center' }}>
                                    <CircularProgress style={{ marginTop: '20em', color: "green" }} />                    
                                </div>
                            )}                     
                            
                        </React.Fragment>
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