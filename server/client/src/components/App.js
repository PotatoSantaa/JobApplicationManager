import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import SignIn from './SignIn';
import Header from './Header';
import Footer from './Footer';
import TaskBoard from './TaskBoard';
import UserDashboard from './UserDashboard';
import LimitedAccess from './LimitedAccess';

import { AuthProvider } from './Auth/Auth'
import PrivateRoute from './Auth/PrivateRoute';

export default function App() {
  return (    
    <AuthProvider>
      <BrowserRouter>
        <Switch>
          { /* Unauthenticated view */ }
          <Route exact path={["/","/limited"]}>
              <Route exact path='/' component={SignIn}/>   
              <Route exact path='/limited' component={LimitedAccess}/>                                   
          </Route>
          { /* Authenticated view */ }
          <Route exact path={["/task", "/dashboard" ]}>
            <Header/>
              <Switch>                  
                  <Route exact path='/task' component={TaskBoard}/>      
                  <Route exact path='/dashboard' component={UserDashboard}/>                                                             
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
