import React from "react";
import {
  Button,
  Card,
  CardGroup,
  Col,
  Container,
  Navbar,
  Row
} from "react-bootstrap";
import _ from "lodash";

export default class WelcomingStaff extends React.Component<any, any> {
  render() {
    return (
      <Container fluid className="mx-2">
        <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
          <Navbar.Brand href="/#/">Oaxaca Table Management</Navbar.Brand>
        </Navbar>

        <Row>
          <Col xs="3">
            <h2>Select Table State</h2>

            <Button
              id="occupied_button"
              //onClick={() => this.saveBasket()}
              variant="success"
              block>
              Occupied
            </Button>
            <Button
              id="unoccupied_button"
              //onClick={() => this.saveBasket()}
              variant="success"
              block>
              Unoccupied
            </Button>
          </Col>

          <Col xs="9">
            <h2>Tables</h2>

            <CardGroup>
              {Array(9)
                .fill(1)
                .map((_table, idx) => (
                  <Card key={idx} bg="dark" text="white" className="mb-3 mr-2">
                    <Card.Body>
                      <Card.Title>Table #{_.random(1, 20, false)}</Card.Title>
                      <Card.Text>Table near the terrace</Card.Text>
                    </Card.Body>
                    <Card.Footer>
                      {Math.random() < 0.5 ? (
                        <small className="text-muted">
                          Reserved at {_.random(1, 24, false)}:37
                        </small>
                      ) : (
                        <Button variant="success" block>
                          Reserve
                        </Button>
                      )}
                    </Card.Footer>
                  </Card>
                ))}
            </CardGroup>
          </Col>
        </Row>
      </Container>
    );
  }
}
