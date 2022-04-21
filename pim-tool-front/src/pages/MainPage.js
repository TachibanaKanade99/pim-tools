import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { BrowserRouter, Route, Redirect, Switch, } from 'react-router-dom';

import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation';
import ErrorScreen from '../components/ErrorScreen/ErrorScreen';
import ProjectForm from '../components/ProjectForm/ProjectForm';
import ProjectsList from '../components/ProjectsList/ProjectsList';

export default function MainPage() {
  return (
    <div className='main'>
      <Container fluid>
        <Row>
          <Col>
            <Header />
          </Col>
        </Row>

        <BrowserRouter>
          <Switch>

            <Route exact path='/error' component={ErrorScreen} />

            <Row>
              <Col xl={1} />
              <Col xl={2}>
                <Navigation />
              </Col>
              <Col xl={8} className='px-0'>

                <Route exact path='/projects/:id/edit'>
                  <ProjectForm title='Edit Project information' />
                </Route>

                <Route exact path='/projects/new'>
                  <ProjectForm title='New Project' />
                </Route>

                <Route exact path='/projects'>
                  <ProjectsList />
                </Route>

                <Route exact path='/'>
                  <Redirect to='/projects' />
                </Route>

              </Col>
            </Row>

          </Switch>
        </BrowserRouter>

      </Container>
    </div>
  )
}