import React from "react";
import {Link, Route, Switch} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";

import {AppStyle, CenterStyle, HeadingStyle, LoginButtonStyle, LoginStyle, MenuButtonStyle} from "./App.styled";
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
            <LoginStyle>
              <Container>
                <Row className="d-flex flex-row-reverse bd-highlight mb-3">
                  {authenticated ? (
                    <>
                      <Col xs="auto">
                        <Link to="/waiter">
                          <MenuButtonStyle>
                            Waiter
                          </MenuButtonStyle>
                        </Link>
                      </Col>
                      <Col xs="auto">
                        <Link to="/kitchen">
                          <MenuButtonStyle>
                            Kitchen
                          </MenuButtonStyle>
                        </Link>
                      </Col>
                      <Col xs="auto">
                        <Link to="/welcomingstaff">
                          <MenuButtonStyle>
                            Welcoming Staff
                          </MenuButtonStyle>
                        </Link>
                      </Col>
                    </>
                  ) : (
                    <Col xs="auto">
                      <Link to="/login">
                        <LoginButtonStyle>
                        </LoginButtonStyle>
                      </Link>
                    </Col>
                  )}
                </Row>
              </Container>
            </LoginStyle>
            <CenterStyle>
              <Container>
                <Row className="d-flex mb-5 align-content-center justify-content-center">
                  <HeadingStyle>Oaxaca Restaurant</HeadingStyle>
                </Row>
                <Row className="d-flex mb-auto align-content-center justify-content-center">
                  <Col xs="auto">
                    <Link to="/menu">
                      <MenuButtonStyle>Click to Enter a world of magic</MenuButtonStyle>
                    </Link>
                  </Col>
                </Row>
              </Container>
            </CenterStyle>
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
