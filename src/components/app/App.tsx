import React from "react";
import { Switch, Route, Link } from "react-router-dom";

import { Button, Col, Container, Row } from "react-bootstrap";

import { AppStyle } from "./App.styled";
import Menu from "../menu/Menu";
import Waiter from "../waiter/Waiter";
import Kitchen from "../kitchen/Kitchen";
import API from "../../client/api";
import Login from "../login/Login";
import WelcomingStaff from "../WelcomingStaff/WelcomingStaff";

interface State {
  authenticated: boolean;
}

export default class App extends React.Component<any, State> {
  constructor(props: any) {
    super(props);

    this.state = {
      authenticated: false
    };

    API.getSession().then(() =>
      API.validateSession(true).then(authed =>
        this.setState({ authenticated: authed })
      )
    );
  }

  render() {
    const { authenticated } = this.state;

    return (
      <Switch>
        <Route exact path="/">
          <AppStyle>
            <Container>
              <Row className="d-flex mb-5 justify-content-center">
                <h2>Oaxaca Restaurant</h2>
              </Row>
              <Row className="d-flex justify-content-center">
                <Col xs="auto">
                  <Link to="/menu">
                    <Button variant="outline-danger" size="lg">
                      Menu
                    </Button>
                  </Link>
                </Col>

                {authenticated ? (
                  <>
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
                    <Col xs="auto">
                      <Link to="/welcomingstaff">
                        <Button variant="outline-primary" size="lg">
                          Welcoming Staff
                        </Button>
                      </Link>
                    </Col>
                  </>
                ) : (
                  <Col xs="auto">
                    <Link to="/login">
                      <Button variant="outline-warning" size="lg">
                        Login
                      </Button>
                    </Link>
                  </Col>
                )}
              </Row>
            </Container>
          </AppStyle>
        </Route>

        <Route path="/menu" component={Menu} />
        <Route path="/login" component={Login} />
        <Route path="/waiter" component={Waiter} />
        <Route path="/kitchen" component={Kitchen} />
        <Route path="/welcomingstaff" component={WelcomingStaff} />
      </Switch>
    );
  }
}
