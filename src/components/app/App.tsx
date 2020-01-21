import React from "react";
import {Switch, Route, Link} from "react-router-dom";

import {Button, Col, Container, Row} from "react-bootstrap";

import {AppStyle} from "./App.styled";
import Menu from "../menu/Menu";
import Waiter from "../waiter/Waiter";
import Kitchen from "../kitchen/Kitchen";

export default class App extends React.Component<any, any> {
  render() {
    return (
      <Switch>
        <Route exact path="/">
          <AppStyle>
            <Container>
              <Row className="d-flex mb-5 justify-content-center">
                <h2>
                  Oaxaca Restaurant
                </h2>
              </Row>
              <Row className="d-flex justify-content-center">
                <Col xs="auto">
                  <Link to="/menu">
                    <Button variant="outline-danger" size="lg">
                      Menu
                    </Button>
                  </Link>
                </Col>
                <Col xs="auto">
                  <Link to="/waiter">
                    <Button variant="outline-secondary" size="lg">
                      Waiter
                    </Button>
                  </Link>
                </Col>
                <Col xs="auto">
                  <Link to="/kitchen">
                    <Button variant="outline-info" size="lg">
                      Kitchen
                    </Button>
                  </Link>
                </Col>
              </Row>
            </Container>
          </AppStyle>
        </Route>

        <Route exact path="/menu" component={Menu}/>
        <Route exact path="/waiter" component={Waiter}/>
        <Route exact path="/kitchen" component={Kitchen}/>
      </Switch>
    );
  }
}
