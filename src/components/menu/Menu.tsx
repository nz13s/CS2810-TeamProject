import React from "react";
import {
  Row,
  Button,
  Card,
  CardDeck,
  Container,
  Navbar,
  Col,
  ListGroup
} from "react-bootstrap";
import MenuItem from "../../entities/MenuItem";
import { MenuStyle } from "./Menu.styled";
import _ from "lodash";

interface State {
  tableID: number;
  menu: Map<string, Array<MenuItem>>;
  basket: Array<MenuItem>;
}

export default class Menu extends React.Component<any, State> {
  constructor(props: any) {
    super(props);

    const mockMenu = new Map();
    mockMenu.set("Chicken", [
      new MenuItem(
        1,
        "Chicken Breast",
        "100% chicken breast meat.",
        8,
        "https://d1ralsognjng37.cloudfront.net/b9b225fe-fc45-4170-b217-78863c2de64e"
      ),
      new MenuItem(
        2,
        "Chicken Burrito",
        "100% chicken breast meat.",
        7,
        "https://d1ralsognjng37.cloudfront.net/b9b225fe-fc45-4170-b217-78863c2de64e"
      ),
      new MenuItem(
        3,
        "Chicken Chipotle",
        "100% chicken breast meat.",
        5.5,
        "https://d1ralsognjng37.cloudfront.net/b9b225fe-fc45-4170-b217-78863c2de64e"
      ),
      new MenuItem(
        4,
        "Cheese Risotto",
        "You’re in for a spicy kick with our Chicken Tikka Sub.",
        7.75,
        "https://d1ralsognjng37.cloudfront.net/7fd17376-3a89-4265-8462-bafa40f31306.jpeg"
      ),
      new MenuItem(
        5,
        "Very Tasty Dish",
        "You’re in for a spicy kick with our Chicken Tikka Sub.",
        10,
        "https://d1ralsognjng37.cloudfront.net/7fd17376-3a89-4265-8462-bafa40f31306.jpeg"
      ),
      new MenuItem(
        6,
        "Tikka Sub",
        "You’re in for a spicy kick with our Chicken Tikka Sub.",
        3.25,
        "https://d1ralsognjng37.cloudfront.net/7fd17376-3a89-4265-8462-bafa40f31306.jpeg"
      )
    ]);
    mockMenu.set("Asian Fusion", [
      new MenuItem(
        7,
        "Salmon Sushi",
        "Salmon, avocado, masago,wasabi, and soy sauce sachet.",
        12,
        "https://d1ralsognjng37.cloudfront.net/8d9b7b17-9701-4338-9144-757b7670f169"
      ),
      new MenuItem(
        8,
        "Best Rolls",
        "Marinated fried tofu with teriyaki sauce, brown sushi rice, sesame oil",
        13.5,
        "https://d1ralsognjng37.cloudfront.net/9b187fb4-2980-49a6-a6ad-39d3fbf7ed4f"
      ),
      new MenuItem(
        9,
        "Bento Box",
        "Marinated fried tofu with teriyaki sauce, brown sushi rice, sesame oil",
        12.99,
        "https://d1ralsognjng37.cloudfront.net/9b187fb4-2980-49a6-a6ad-39d3fbf7ed4f"
      )
    ]);

    this.state = {
      tableID: 1337,
      menu: mockMenu,
      basket: []
    };
  }

  addToBasket(item: MenuItem) {
    this.setState({ basket: [...this.state.basket, item] });
  }

  render() {
    const { tableID, menu, basket } = this.state;

    return (
      <MenuStyle>
        <Container>
          <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
            <Navbar.Brand href="/#/">Oaxaca Menu</Navbar.Brand>
            <Navbar.Collapse className="d-flex justify-content-end">
              <Navbar.Brand>Table #{tableID}</Navbar.Brand>
            </Navbar.Collapse>
          </Navbar>
          <Row>
            <Col xs="4">
              <h2>Your Order</h2>
              <ListGroup variant="flush">
                {Object.entries(_.groupBy(basket, x => x.id)).map(
                  ([, items]) => (
                    <ListGroup.Item
                      key={items[0].id}
                      className="bg-dark text-white">
                      {items[0].name} x{items.length}
                    </ListGroup.Item>
                  )
                )}
                <ListGroup.Item className="bg-dark text-white">
                  Total: £
                  {basket.reduce((acc, curr) => acc + curr.price, 0.00).toFixed(2)}
                </ListGroup.Item>
              </ListGroup>
            </Col>
            <Col xs="8">
              {Array.from(menu.entries()).map(([category, items]) => (
                <React.Fragment key={category}>
                  <h2>{category}</h2>

                  <CardDeck>
                    {items.map(item => (
                      <Card
                        key={item.id}
                        className="mb-3 bg-dark text-white"
                        style={{ minWidth: "15vw" }}>
                        <Card.Header style={{ whiteSpace: "nowrap" }}>
                          {item.name}
                        </Card.Header>

                        <Card.Img
                          className="img-fluid"
                          variant="top"
                          src={item.image}
                        />

                        <Card.Body>
                          <Card.Subtitle className="mb-2 text-muted">
                            £{item.price}
                          </Card.Subtitle>
                          <Card.Text>{item.description}</Card.Text>
                        </Card.Body>

                        <Card.Footer className="d-flex justify-content-end">
                          <Button onClick={() => this.addToBasket(item)}>
                            Add to order
                          </Button>
                        </Card.Footer>
                      </Card>
                    ))}
                  </CardDeck>
                </React.Fragment>
              ))}
            </Col>
          </Row>
        </Container>
      </MenuStyle>
    );
  }
}
