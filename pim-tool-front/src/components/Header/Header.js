import React from 'react';
import Translate from 'react-translate-component';
import { Container, Row, Col } from 'react-bootstrap';

import './Header.css';
import logo from '../../../src/images/logo_elca.png';
import counterpart from 'counterpart';
import en from '../../lang/en';
import fr from '../../lang/fr';

// local components
import LocaleChange from '../LocaleChange/LocaleChange';

counterpart.registerTranslations('en', en);
counterpart.registerTranslations('fr', fr);

export default function Header() {
  return (
    <div className="content">
      <Container fluid>
        <Row>
          <Col xl={1}>
          </Col>
          <Col xl={1} className='text-center'>
            <img className="logo" src={logo} alt="logo" />
          </Col>
          <Col xl={6}>
            <p className="name">
              <Translate content="header.name" />
            </p>
          </Col>
          <Col xl={4}>
            <LocaleChange />
          </Col>
        </Row>
      </Container>
    </div>
  )
}