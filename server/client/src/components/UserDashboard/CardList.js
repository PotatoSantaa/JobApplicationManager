import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {
    Grid,
    Card,
    CardContent,
    Typography,
    CardHeader
} from '@material-ui/core/';

const useStyles = makeStyles(theme => ({
    root: {
        flexGrow: 1,
        padding: theme.spacing(2)
    }
}))

export default function CardList() {
    const classes = useStyles()
    const [data, setData] = useState([]);

    useEffect(() => { 
        fetch(`${process.env.REACT_APP_API_URL}/jobapp/getAllJobs`, {
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json',
                // 'Origin' : 'http://localhost:3000',
            },
        })
        .then(resp => resp.json())
        .then(resp => setData(resp))
        .catch(err => console.log(err))      
    }, []);


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
                        </Card>
                     </Grid>
                ))}
            </Grid>
        </div>
    )
}