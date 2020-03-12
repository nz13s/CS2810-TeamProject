import React, { SyntheticEvent } from "react";
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
import API from "../../client/api";
import MenuItem from "../../entities/MenuItem";
import Ingredient from "../../entities/Ingredient";

export default class WaiterEdit extends React.Component<any, any> {
  constructor(props: any) {
    super(props);

    this.state = {};
  }

  async deleteMenuItem(e: SyntheticEvent): Promise<void> {
    e.preventDefault();
    e.stopPropagation();

    const form = e.currentTarget as HTMLFormElement;
    const itemElement = form.elements.namedItem("itemID") as HTMLInputElement;
    const itemID = Number(itemElement.value);

    try {
      await API.delMenuItem(itemID);
      alert(`${itemID} made unavailable!`);
    } catch {
      alert(`${itemID} cannot be removed, try again later`);
    }
  }

  async addMenuItem(e: SyntheticEvent): Promise<void> {
    e.preventDefault();
    e.stopPropagation();

    const form = e.currentTarget as HTMLFormElement;

    const itemName = form.elements.namedItem("itemName") as HTMLInputElement;
    const itemCategory = form.elements.namedItem(
      "itemCategory"
    ) as HTMLInputElement;
    const itemDescription = form.elements.namedItem(
      "itemDescription"
    ) as HTMLInputElement;
    const itemCalories = form.elements.namedItem(
      "itemCalories"
    ) as HTMLInputElement;
    const itemIngredients = form.elements.namedItem(
      "itemIngredients"
    ) as HTMLInputElement;
    const itemPrice = form.elements.namedItem("itemPrice") as HTMLInputElement;
    const itemImage = form.elements.namedItem("itemImage") as HTMLInputElement;

    const item = new MenuItem(
      0,
      itemName.value,
      Number(itemCategory.value),
      itemDescription.value,
      Number(itemPrice.value),
      itemIngredients.value
        .split(",")
        .map(x => new Ingredient(Number(x.trim()), "", false)),
      Number(itemCalories.value),
      itemImage.value
    );

    try {
      await API.newMenuItem(item);
      alert(`${item.name} added to the menu!`);
    } catch {
      alert(`${item.name} cannot be added at the moment, try again later`);
    }
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
              <Form onSubmit={this.deleteMenuItem.bind(this)}>
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
              <Form onSubmit={this.addMenuItem.bind(this)}>
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

                <Form.Group controlId="itemCategory">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Category
                      </InputGroup.Text>
                    </InputGroup.Prepend>
                    <Form.Control
                      type="number"
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

                <Form.Group controlId="itemImage">
                  <InputGroup>
                    <InputGroup.Prepend>
                      <InputGroup.Text id="inputGroupPrepend">
                        Image URL
                      </InputGroup.Text>
                    </InputGroup.Prepend>
                    <Form.Control
                      type="text"
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
