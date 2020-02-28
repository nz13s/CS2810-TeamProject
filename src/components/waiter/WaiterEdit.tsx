import React from "react";
import {
  Button,
  Col,
  Container,
  Form,
  InputGroup,
  Navbar,
  Row
} from "react-bootstrap";
import { WaiterEditStyle } from "./WaiterEdit.styled";

export default class WaiterEdit extends React.Component<any, any> {
  constructor(props: any) {
    super(props);

    this.state = {};
  }

  render() {
    return (
      <WaiterEditStyle>
        <Container fluid className="mx-2">
          <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
            <Navbar.Brand href="/#/">Oaxaca Menu Edit</Navbar.Brand>
          </Navbar>

          <Row className="d-flex justify-content-center">
            <Col xs="4">
              <h2>Remove Menu Item</h2>
              <Form onSubmit={() => console.log("MOCK")}>
                <Form.Group controlId="itemID">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Menu Item ID:
                      </InputGroup.Text>
                    </InputGroup.Prepend>
                    <Form.Control
                      type="number"
                      aria-describedby="inputGroupPrepend"
                      required
                    />
                  </InputGroup>
                </Form.Group>
                <Button variant="danger" type="submit">
                  Remove
                </Button>
              </Form>
            </Col>
            <Col xs="4">
              <h2>Add Menu Item</h2>
              <Form onSubmit={() => console.log("MOCK")}>
                <Form.Group controlId="itemName">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Name
                      </InputGroup.Text>
                    </InputGroup.Prepend>
                    <Form.Control
                      type="text"
                      aria-describedby="inputGroupPrepend"
                      required
                    />
                  </InputGroup>
                </Form.Group>

                <Form.Group controlId="itemDescription">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Description
                      </InputGroup.Text>
                    </InputGroup.Prepend>
                    <Form.Control
                      type="text"
                      aria-describedby="inputGroupPrepend"
                      required
                    />
                  </InputGroup>
                </Form.Group>

                <Form.Group controlId="itemDescription">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Calories
                      </InputGroup.Text>
                    </InputGroup.Prepend>
                    <Form.Control
                      type="text"
                      aria-describedby="inputGroupPrepend"
                      required
                    />
                  </InputGroup>
                </Form.Group>

                <Form.Group controlId="itemDescription">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Allergens
                      </InputGroup.Text>
                    </InputGroup.Prepend>
                    <Form.Control
                      type="text"
                      aria-describedby="inputGroupPrepend"
                      required
                    />
                  </InputGroup>
                </Form.Group>

                <Form.Group controlId="itemPrice">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Price
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
                  Add
                </Button>
              </Form>
            </Col>
          </Row>
        </Container>
      </WaiterEditStyle>
    );
  }
}
