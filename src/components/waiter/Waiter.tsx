import React from "react";
import { WaiterStyle } from "./Waiter.styled";
import { Button, Col, Container, Navbar, Row } from "react-bootstrap";

export default class Waiter extends React.Component<any, any> {
  constructor(props: any) {
    super(props);

    this.state = {};
  }

  render() {
    return (
      <WaiterStyle>
        <Container>
          <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
            <Navbar.Brand href="/#/">Oaxaca Waiter</Navbar.Brand>
          </Navbar>

          <div className="my-5 py-5" />

          <Row className="d-flex mb-2 justify-content-center">
            <Col xs="auto">
              <Button
                variant="success"
                size="lg"
                style={{ width: "40vw", height: "20vh" }}>
                Take Order
              </Button>
            </Col>
            <Col xs="auto">
              <Button
                variant="danger"
                size="lg"
                style={{ width: "40vw", height: "20vh" }}>
                Change Menu
              </Button>
            </Col>
          </Row>
          <Row className="d-flex justify-content-center">
            <Col xs="auto">
              <Button
                variant="primary"
                size="lg"
                style={{ width: "40vw", height: "20vh" }}>
                Serve Food
              </Button>
            </Col>
            <Col xs="auto">
              <Button
                variant="info"
                size="lg"
                style={{ width: "40vw", height: "20vh" }}>
                Check Bill
              </Button>
            </Col>
          </Row>
        </Container>
      </WaiterStyle>
    );
  }
}
