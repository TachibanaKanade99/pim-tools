import { React, useEffect, useRef, } from 'react';
import Translate from 'react-translate-component';
import { Row, Col, Nav, Navbar } from 'react-bootstrap';
import { Link, useLocation, } from 'react-router-dom';

import './Navigation.css';
import counterpart from 'counterpart';
import en from '../../lang/en';
import fr from '../../lang/fr';

counterpart.registerTranslations('en', en);
counterpart.registerTranslations('fr', fr);

export default function Navigation(props) {
  const activeNew = useRef(null);
  const activeList = useRef(null);
  let location = useLocation();

  useEffect(() => {
    if (location.pathname === '/') {
      activeList.current.classList.add('text-active');
      if (activeNew.current.classList.contains('text-active')) {
        activeNew.current.classList.remove('text-active');
      }
    }
    if (location.pathname === '/projects/new') {
      activeNew.current.classList.add('text-active');
      if (activeList.current.classList.contains('text-active')) {
        activeList.current.classList.remove('text-active');
      }
    }
  }, [location.pathname])

  return (
    <Navbar collapseOnSelect expand='lg'>
      <Navbar.Brand />
      <Navbar.Toggle aria-controls='responsive-navbar-nav' />
      <Navbar.Collapse id='responsive-navbar-nav'>
        <Nav className='navigation'>
          <Row>
            <Col xl={12}>
              <Link to='/' className='nav-link'>
                <p ref={activeList} className='text-semi-bold first-element text-active'>
                  <Translate content='navigation.title' />
                </p>
              </Link>
              <Link to='/projects/new' className='nav-link'>
                <p ref={activeNew} className='text-semi-bold'>
                  <Translate content='navigation.new' />
                </p>
              </Link>
              <Nav.Link className="sub-menu-text active-project"><Translate content='navigation.project' /></Nav.Link>
              <Nav.Link className="sub-menu-text"><Translate content='navigation.customer' /></Nav.Link>
              <Nav.Link className="sub-menu-text"><Translate content='navigation.supplier' /></Nav.Link>
            </Col>
          </Row>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  )
}