import React from "react";
import {
  Button,
  Col,
  Container,
  DropdownButton,
  Form,
  FormControl,
  InputGroup,
  Navbar,
  Row,
  Dropdown
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

                <Form.Group controlId="itemCalories">
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

                <Form.Group controlId="itemIngredients">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Ingredients
                      </InputGroup.Text>
                    </InputGroup.Prepend>
                    <FormControl
                      aria-label="Ingredients"
                      aria-describedby="basic-addon2"
                    />

                    <DropdownButton
                      as={InputGroup.Append}
                      variant="primary"
                      title="+"
                      id="input-group-dropdown-2">
                      {[
                        "Black Beans",
                        "Chicken",
                        "Tomato",
                        "Cord",
                        "Doritos",
                        "Wheat"
                      ].map((ingredient, idx) => (
                        <Dropdown.Item
                          key={idx}
                          as="button"
                          onClick={() => console.log(ingredient)}>
                          {ingredient}
                        </Dropdown.Item>
                      ))}
                    </DropdownButton>
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
