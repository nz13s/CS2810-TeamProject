import React from "react";
import { Switch, Route, Link } from "react-router-dom";
import { WaiterStyle } from "./Waiter.styled";
import { Button, Col, Container, Navbar, Row } from "react-bootstrap";
import WaiterOrder from "./WaiterOrder";
import WaiterEdit from "./WaiterEdit";
import WaiterServe from "./WaiterServe";
import WaiterBill from "./WaiterBill";

export default class Waiter extends React.Component<any, any> {
  constructor(props: any) {
    super(props);

    this.state = {};
  }

  render() {
    return (
      <Switch>
        <Route exact path="/waiter">
          <WaiterStyle>
            <Container>
              <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
                <Navbar.Brand href="/#/">Oaxaca Waiter</Navbar.Brand>
              </Navbar>

              <div className="my-5 py-5" />

              <Row className="d-flex mb-2 justify-content-center">
                <Col xs="auto">
                  <Link to="/waiter/order">
                    <Button className="waiter_button" variant="success">
                      Take Order
                    </Button>
                  </Link>
                </Col>
                <Col xs="auto">
                  <Link to="/waiter/edit">
                    <Button className="waiter_button" variant="danger">
                      Edit Menu
                    </Button>
                  </Link>
                </Col>
              </Row>
              <Row className="d-flex justify-content-center">
                <Col xs="auto">
                  <Link to="/waiter/serve">
                    <Button className="waiter_button" variant="primary">
                      Serve Food
                    </Button>
                  </Link>
                </Col>
                <Col xs="auto">
                  <Link to="/waiter/bill">
                    <Button className="waiter_button" variant="info">
                      Check Bill
                    </Button>
                  </Link>
                </Col>
              </Row>
            </Container>
          </WaiterStyle>
        </Route>

        <Route exact path="/waiter/order">
          <WaiterOrder />
        </Route>

        <Route exact path="/waiter/edit">
          <WaiterEdit />
        </Route>

        <Route exact path="/waiter/serve">
          <WaiterServe />
        </Route>

        <Route exact path="/waiter/bill">
          <WaiterBill />
        </Route>
      </Switch>
    );
  }
}
