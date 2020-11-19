import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import SignIn from './SignIn';
import Header from './Header';
import Footer from './Footer';
import UserDashboard from './UserDashboard';
import LimitedAccess from './LimitedAccess';

import { AuthProvider } from './Auth/Auth'
// import PrivateRoute from './Auth/PrivateRoute';

export default function App() {
  return (    
    <AuthProvider>
      <BrowserRouter>
        <Switch>
          { /* Unauthenticated view */ }
          <Route exact path={["/"]}>
              <Route exact path='/' component={SignIn}/>   
                                               
          </Route>
          { /* Authenticated view */ }
          <Route exact path={["/limited", "/dashboard" ]}>
            <Header/>
              <Switch>                       
                  <Route exact path='/dashboard' component={UserDashboard}/> 
                  <Route exact path='/limited' component={LimitedAccess}/>                                                              
              </Switch>
            <Footer/>                                      
          </Route>
          { /* Wrong url view */ }
          <Route path="*" component={NotFoundPage}/>

        </Switch>
      </BrowserRouter>    
    </AuthProvider>
  );
}

const NotFoundPage = () => <h1>404 Page Not Found</h1>;
