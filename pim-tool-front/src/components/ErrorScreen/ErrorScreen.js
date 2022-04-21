import React from 'react';
import Translate from 'react-translate-component';
import { Container, Row, Col } from 'react-bootstrap';
import { Link, } from 'react-router-dom';

import './ErrorScreen.css';
import Image from '../../../src/images/error.png'

export default function ErrorScreen() {
  return (
    <Container>
      <Row>
        <Col>
          <img src={Image} alt="error" className="error" />
        </Col>
        <Col className="text-container">
          <div>
            <div>
              <Translate content="errorScreen.unexpected" />
            </div>

            <div>
              <Translate content="errorScreen.please" />
              <Translate className="red-text" content="errorScreen.contact" />
            </div>

            <div>
              <Translate content="errorScreen.or" />
              <Link to="/">
                <Translate content="errorScreen.back" />
              </Link>
            </div>
          </div>
        </Col>
      </Row>
    </Container>
  )
}