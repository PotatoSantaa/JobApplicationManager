import React, { useState, useCallback, useContext } from 'react';
import { Redirect } from 'react-router-dom'
import { makeStyles } from '@material-ui/styles';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Container from '@material-ui/core/Container'
import app from '../../firebase'
import { AuthContext } from '../Auth/Auth'
import { useSnackbar } from 'notistack';

const Auth = () => {
    const styles = useStyles();
    const { enqueueSnackbar } = useSnackbar();    

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isLoginView, setIsLoginView] = useState(true);
    const [invalidMessage, setInvalidMessage] = useState(false);        

    const loginClicked = () => {
        if(validateInput()) {
            setInvalidMessage(false);
            handleSignIn();
        }             
    };

    const registerClicked = () => {
        if(validateInput()) {
            setInvalidMessage(false);
            handleSignUp();
        }
    };

    const handleSignUp = useCallback(async () => {        
        try {            
            await app.auth().createUserWithEmailAndPassword(email, password)                            
        } catch (error) {
            showError(error.message);
        }
    }, [email, password])

    const handleSignIn = useCallback(async () => {        
        try {
            await app.auth().signInWithEmailAndPassword(email, password)                            
        } catch (error) {
            showError(error.message);
        }
    }, [email, password])    

    const validateInput = () => {
        return new RegExp(/[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,15}/g).test(email) 
                && password.length !== 0
    }

    const showError = (message) => {
        enqueueSnackbar(message, { 
            variant: 'warning',
        });
    }

    const { user } = useContext(AuthContext);
    
    if (user) {
        console.log("The user is " + JSON.stringify(user))
        return <Redirect to="/dashboard"/>
    }    

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