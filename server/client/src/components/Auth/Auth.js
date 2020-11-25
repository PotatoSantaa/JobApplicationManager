import React, { useState, useEffect } from 'react'
import app from '../../firebase'

export const AuthContext = React.createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    /* Observer provided by Firebase, it will update the value 
    of our currentUser state whenever something changes */
    useEffect(() => {        
        app.auth().onAuthStateChanged((user) => {
            if (user) {
              setUser(user)
            } else {
              setUser(null)
            }
          });    
    }, []);

    /* The AuthContext will store the value of the current user
    if null -> not logged in, if non-null -> user is logged in */
    return (
        <AuthContext.Provider value={{ user }} >
            { children }
        </AuthContext.Provider>
    );
};

