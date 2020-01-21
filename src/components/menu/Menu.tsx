import React from "react";
import { Button, Card, CardDeck, Container, Navbar } from "react-bootstrap";
import MenuItem from "../../entities/MenuItem";
import { MenuStyle } from "./Menu.styled";

interface State {
  id: number;
  menu: Map<string, Array<MenuItem>>;
}

export default class Menu extends React.Component<any, State> {
  constructor(props: any) {
    super(props);

    const mockMenu = new Map();
    mockMenu.set("Chicken", [
      new MenuItem(
        1,
        "Chicken",
        "100% chicken breast meat.",
        10,
        "https://d1ralsognjng37.cloudfront.net/b9b225fe-fc45-4170-b217-78863c2de64e"
      ),
      new MenuItem(
        2,
        "Chicken",
        "100% chicken breast meat.",
        10,
        "https://d1ralsognjng37.cloudfront.net/b9b225fe-fc45-4170-b217-78863c2de64e"
      ),
      new MenuItem(
        3,
        "Chicken",
        "100% chicken breast meat.",
        10,
        "https://d1ralsognjng37.cloudfront.net/b9b225fe-fc45-4170-b217-78863c2de64e"
      ),
      new MenuItem(
        4,
        "Sandwich",
        "You’re in for a spicy kick with our Chicken Tikka Sub. Tender chicken breast strips coated in a fragrant tikka spice – no wonder it’s one of the nation’s favourites!",
        10,
        "https://d1ralsognjng37.cloudfront.net/7fd17376-3a89-4265-8462-bafa40f31306.jpeg"
      ),
      new MenuItem(
        5,
        "Sandwich",
        "You’re in for a spicy kick with our Chicken Tikka Sub. Tender chicken breast strips coated in a fragrant tikka spice – no wonder it’s one of the nation’s favourites!",
        10,
        "https://d1ralsognjng37.cloudfront.net/7fd17376-3a89-4265-8462-bafa40f31306.jpeg"
      ),
      new MenuItem(
        6,
        "Sandwich",
        "You’re in for a spicy kick with our Chicken Tikka Sub. Tender chicken breast strips coated in a fragrant tikka spice – no wonder it’s one of the nation’s favourites!",
        10,
        "https://d1ralsognjng37.cloudfront.net/7fd17376-3a89-4265-8462-bafa40f31306.jpeg"
      )
    ]);
    mockMenu.set("Japanese", [
      new MenuItem(
        7,
        "Sushi",
        "Salmon, avocado, masago,wasabi, and soy sauce sachet.",
        10,
        "https://d1ralsognjng37.cloudfront.net/8d9b7b17-9701-4338-9144-757b7670f169"
      ),
      new MenuItem(
        8,
        "Rolls",
        "Marinated fried tofu with teriyaki sauce, brown sushi rice, sesame oil, avocado, red cabbage, roasted butternut squash, edamame, cucumber, sesame seed spring onions, mixed salad leaves, chives, and Japanese dressing.",
        10,
        "https://d1ralsognjng37.cloudfront.net/9b187fb4-2980-49a6-a6ad-39d3fbf7ed4f"
      ),
      new MenuItem(
        9,
        "Rolls",
        "Marinated fried tofu with teriyaki sauce, brown sushi rice, sesame oil, avocado, red cabbage, roasted butternut squash, edamame, cucumber, sesame seed spring onions, mixed salad leaves, chives, and Japanese dressing.",
        10,
        "https://d1ralsognjng37.cloudfront.net/9b187fb4-2980-49a6-a6ad-39d3fbf7ed4f"
      )
    ]);

    this.state = {
      id: 1337,
      menu: mockMenu
    };
  }

  render() {
    const { id, menu } = this.state;

    return (
      <MenuStyle>
        <Container>
          <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
            <Navbar.Brand href="/#/">Oaxaca Menu</Navbar.Brand>
            <Navbar.Collapse className="d-flex justify-content-end">
              <Navbar.Brand>Table #{id}</Navbar.Brand>
            </Navbar.Collapse>
          </Navbar>

          {Array.from(menu.entries()).map(([category, items]) => (
            <React.Fragment key={category}>
              <h2>{category}</h2>

              <CardDeck>
                {items.map(item => (
                  <Card
                    key={item.id}
                    className="mb-3 bg-dark text-white"
                    style={{ minWidth: "20vw" }}>
                    <Card.Header>{item.name}</Card.Header>

                    <Card.Img
                      className="img-fluid"
                      variant="top"
                      src={item.image}
                    />

                    <Card.Body>
                      <Card.Text>{item.description}</Card.Text>
                    </Card.Body>

                    <Card.Footer className="d-flex justify-content-end">
                      <Button>Order for only £{item.price}</Button>
                    </Card.Footer>
                  </Card>
                ))}
              </CardDeck>
            </React.Fragment>
          ))}
        </Container>
      </MenuStyle>
    );
  }
}
