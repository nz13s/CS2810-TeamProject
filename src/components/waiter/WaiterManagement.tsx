import React from "react";
import { WaiterManagementStyle } from "./WaiterManagement.styled";
import {
  Button,
  Card,
  CardGroup,
  Col,
  Container,
  Form,
  InputGroup,
  Navbar,
  Row
} from "react-bootstrap";

export default class WaiterManagement extends React.Component<any, any> {
  constructor(props: any) {
    super(props);

    this.state = {};
  }

  render() {
    return (
      <WaiterManagementStyle>
        <Container fluid className="mx-2">
          <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
            <Navbar.Brand href="/#/">Oaxaca Table Management</Navbar.Brand>
          </Navbar>

          <Row>
            <Col xs="3">
              <h2>Table Finder</h2>

              <Form onSubmit={() => console.log("MOCK")}>
                <Form.Group controlId="persons">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Persons
                      </InputGroup.Text>
                    </InputGroup.Prepend>
                    <Form.Control
                      type="number"
                      aria-describedby="inputGroupPrepend"
                      required
                    />
                  </InputGroup>
                </Form.Group>
                <Button variant="success" type="submit">
                  Find
                </Button>
              </Form>
            </Col>

            <Col xs="9">
              <h2>Tables</h2>

              <CardGroup>
                <Card bg="dark" text="white" className="mb-3 mr-2">
                  <Card.Body>
                    <Card.Title>Table #1</Card.Title>
                    <Card.Text>Table near the terrace</Card.Text>
                  </Card.Body>
                  <Card.Footer>
                    <small className="text-muted">Reserved at 13:37</small>
                  </Card.Footer>
                </Card>

                <Card bg="dark" text="white" className="mb-3 mr-2">
                  <Card.Body>
                    <Card.Title>Table #2</Card.Title>
                    <Card.Text>Table near the windows</Card.Text>
                  </Card.Body>
                  <Card.Footer>
                    <Button variant="success" block>
                      Reserve
                    </Button>
                  </Card.Footer>
                </Card>

                <Card bg="dark" text="white" className="mb-3 mr-2">
                  <Card.Body>
                    <Card.Title>Table #3</Card.Title>
                    <Card.Text>Table near the garden</Card.Text>
                  </Card.Body>
                  <Card.Footer>
                    <small className="text-muted">Reserved at 13:37</small>
                  </Card.Footer>
                </Card>

                <Card bg="dark" text="white" className="mb-3 mr-2">
                  <Card.Body>
                    <Card.Title>Table #4</Card.Title>
                    <Card.Text>Table near the sea</Card.Text>
                  </Card.Body>
                  <Card.Footer>
                    <Button variant="success" block>
                      Reserve
                    </Button>
                  </Card.Footer>
                </Card>

                <Card bg="dark" text="white" className="mb-3 mr-2">
                  <Card.Body>
                    <Card.Title>Table #4</Card.Title>
                    <Card.Text>Table near wuhan</Card.Text>
                  </Card.Body>
                  <Card.Footer>
                    <Button variant="success" block>
                      Reserve
                    </Button>
                  </Card.Footer>
                </Card>

                <Card bg="dark" text="white" className="mb-3 mr-2">
                  <Card.Body>
                    <Card.Title>Table #4</Card.Title>
                    <Card.Text>Table near Big Ben</Card.Text>
                  </Card.Body>
                  <Card.Footer>
                    <small className="text-muted">Reserved at 13:37</small>
                  </Card.Footer>
                </Card>
              </CardGroup>
            </Col>
          </Row>
        </Container>
      </WaiterManagementStyle>
    );
  }
}
