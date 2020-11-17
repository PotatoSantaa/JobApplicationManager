import React, { useContext } from 'react';
import { Route, Redirect } from 'react-router-dom';
import { AuthContext } from './Auth'

/* A wrapper for <Route> that redirects to the login
screen if you're not yet authenticated. */
const PrivateRoute = ({ component: Component, ...rest }) => {
    const { currentUser } = useContext(AuthContext);
    return (
        <Route 
            {...rest} 
            render={props => !!currentUser ? (
                <Component {...props} />
            ) : (
                <Redirect to={{pathname: '/', state: {from: props.location}}} />
        )}/>
    )    
};

export default PrivateRoute;