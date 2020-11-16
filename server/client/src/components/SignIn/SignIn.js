import React, { useState, useEffect } from 'react';
import { useCookies } from 'react-cookie';

import { API } from './API';

import { makeStyles } from '@material-ui/styles';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Container from '@material-ui/core/Container'


const Auth = () => {
    const styles = useStyles();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [token, setToken, deleteToken] = useCookies(['usr-token']);  

    const [ isLoginView, setIsLoginView ] = useState(true);
    const [invalidMessage, setInvalidMessage] = useState(false);
    
    // useEffect(() => {
    //     console.log(token);
    //     if(token['usr-token']) window.location.href = '/dashboard';
    // }, [token]);

    const loginClicked = () => {
        // Fix bearer token
        // API.loginUser({email, password}, token['usr-token'])
        // .then(resp => setToken('usr-token', resp.token))
        if(email !== null && password !== null) {
            setInvalidMessage(false);
            window.location.href = '/dashboard';
        } 
       else {
            setInvalidMessage(true);
            console.log(error);
        }    
    };

    const registerClicked = () => {
        console.log("registered");
    };

   

    return (
        <Container maxWidth="md" className={styles.container}>
                <div className={styles.heading}>
                    {isLoginView ?  <h2>Login</h2> : <h2>Signup</h2>}
                    {invalidMessage ? <h5 style= {{ color: 'red' }}>Invalid credentials</h5> : null}
                </div>
                <TextField
                    required 
                    id="standard-required" 
                    label="Email" 
                    onChange={event => setEmail(event.target.value)}
                />
                <TextField
                    id="standard-password-input-required"
                    label="Password"
                    type="password"
                    autoComplete="current-password" 
                    onChange={event => setPassword(event.target.value)}    
                />
                {isLoginView ?
                    <Button 
                    size="small"
                    variant="contained"
                    onClick={loginClicked} 
                    className={styles.button}
                    >
                        Sign in
                    </Button> :
                    <Button 
                        size="small"
                        variant="contained"
                        onClick={registerClicked} 
                        className={styles.button}
                    >
                        Register
                    </Button> 
                }
            {isLoginView ?
                    <p className={styles.p} style={{cursor:'pointer'}} onClick={() => setIsLoginView(false)}>Don't have an account? Register here.</p>:
                    <p className={styles.p} style={{cursor:'pointer'}} onClick={() => setIsLoginView(true)}>Already have an account? Login here.</p>
                   
            }
        </Container>
    );
}

const useStyles = makeStyles({
    container: {
        height: '95vh', 
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        color: '#3f51b5',
    },
    heading: {
        height: '15vh', 
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
    },
    button: {
        marginTop: '30px',
        color: '#3f51b5',
    },
    p: {
        marginTop: '30px',
    },
});



export default Auth;