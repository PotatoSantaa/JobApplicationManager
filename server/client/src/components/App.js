import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import SignIn from './SignIn';
import Header from './Header';
import Footer from './Footer';
import Kanban from './KanbanBoard';
import TaskBoard from './TaskBoard';

import { CookiesProvider } from 'react-cookie'; 

export default function App() {
  return (
    <CookiesProvider>
      <BrowserRouter>
        <Switch>
          { /* Unauthenticated view */ }
          <Route exact path={["/"]}>
              <Route exact path='/' component={SignIn}/>                                     
          </Route>
          { /* Authenticated view */ }
          <Route exact path={["/task", "/kanban" ]}>
            <Header/>
              <Switch>
                  <Route exact path='/task' component={TaskBoard}/> 
                  <Route exact path='/kanban' component={Kanban}/>                                       
              </Switch>
            <Footer/>                                      
          </Route>
          { /* Wrong url view */ }
          <Route path="*" component={NotFoundPage}/>

        </Switch>
      </BrowserRouter>
    </CookiesProvider>
  );
}

const NotFoundPage = () => <h1>404 Page Not Found</h1>;