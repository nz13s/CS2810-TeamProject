import React from "react";
import {
  Row,
  Button,
  Card,
  CardDeck,
  Container,
  Navbar,
  Col,
  ListGroup,
  ButtonGroup,
  Toast
} from "react-bootstrap";
import MenuItem from "../../entities/MenuItem";
import { MenuStyle } from "./Menu.styled";
import _ from "lodash";
import axios from "axios";

type MenuType = Map<string, Array<MenuItem>>;
type BasketType = Array<MenuItem>;
interface State {
  tableID: number;
  menu: MenuType;
  basket: BasketType;
  errors: Array<React.ReactNode>;
}

export default class Menu extends React.Component<any, State> {
  constructor(props: any) {
    super(props);

    this.state = {
      tableID: 1337,
      menu: new Map(),
      basket: [],
      errors: []
    };

    this.fetchBasket().then(basket => this.setState({ basket: basket }));
    this.fetchMenu().then(menu => this.setState({ menu: menu }));
  }

  async fetchMenu(): Promise<MenuType> {
    let response;
    try {
      response = await axios.get("https://tomcat.xhex.uk/menutest/menu");
    } catch (e) {
      this.addError(`${e.message}: Couldn't fetch menu from server`);
      return new Map();
    }

    const { categories } = response.data;

    const menu = new Map();
    categories.forEach((e: any) => {
      const { categoryName, list } = e;

      const items = list.map(
        (item: any) =>
          new MenuItem(
            item.foodID,
            item.foodName,
            item.foodDescription,
            item.price,
            "https://d1ralsognjng37.cloudfront.net/b9b225fe-fc45-4170-b217-78863c2de64e"
          )
      );

      menu.set(categoryName, items);
    });
    return menu;
  }

  async fetchBasket(): Promise<BasketType> {
    let response;
    try {
      response = await axios.get("https://tomcat.xhex.uk/backendtest/order");
    } catch (e) {
      this.addError(`${e.message}: Couldn't fetch order from server`);
      return [];
    }

    const { items } = response.data;

    return items.map((item: any) => {
      return new MenuItem(
        item.foodID,
        item.foodName,
        item.foodDescription,
        item.price,
        ""
      );
    });
  }

  async addToBasket(item: MenuItem) {
    try {
      await axios.post(
        `https://tomcat.xhex.uk/backendtest/order?item=${item.id}&count=1`
      );
    } catch (e) {
      this.addError(`${e.message}: Couldn't add item to order`);
      return;
    }

    const basket = await this.fetchBasket();
    this.setState({ basket: basket });
  }

  async delFromBasket(item: MenuItem) {
    try {
      await axios.delete(
        `https://tomcat.xhex.uk/backendtest/order?item=${item.id}&count=1`
      );
    } catch (e) {
      this.addError(`${e.message}: Couldn't remove item to order`);
      return;
    }

    const basket = await this.fetchBasket();
    this.setState({ basket: basket });
  }

  addError(error: string) {
    const element = (
      <Toast
        key={error + Math.random() * 100}
        onClose={() =>
          this.setState({
            errors: this.state.errors.filter(x => x !== element)
          })
        }
        delay={5000}
        autohide={true}
        className="text-white bg-dark"
        style={{ width: "20vw" }}>
        <Toast.Header className="bg-white">
          <strong className="mr-auto text-primary">Server</strong>
        </Toast.Header>
        <Toast.Body>{error}</Toast.Body>
      </Toast>
    );

    this.setState({ errors: [element, ...this.state.errors] });
  }

  render() {
    const { tableID, menu, basket, errors } = this.state;

    return (
      <MenuStyle>
        <div
          className="pr-3 mr-3"
          style={{ zIndex: 2000, position: "fixed", top: 0, right: 0 }}>
          {errors}
        </div>
        <Container>
          <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
            <Navbar.Brand href="/#/">Oaxaca Menu</Navbar.Brand>
            <Navbar.Collapse className="d-flex justify-content-center">
              <Navbar.Brand>Table #{tableID}</Navbar.Brand>
            </Navbar.Collapse>
          </Navbar>
          <Row>
            <Col xs="4">
              <div className="sticky-top">
                <h2>Your Order</h2>
                <ListGroup variant="flush" className="d-inline-block w-100">
                  {Object.entries(_.groupBy(basket, x => x.id)).map(
                    ([, items]) => (
                      <ListGroup.Item
                        key={items[0].id}
                        className="bg-dark text-white"
                        style={{ overflow: "hidden" }}>
                        {items[0].name} x{items.length}
                        <ButtonGroup
                          className="ml-2"
                          style={{ float: "right" }}>
                          <Button
                            onClick={() => this.addToBasket(items[0])}
                            variant="secondary">
                            +
                          </Button>
                          <Button
                            onClick={() => this.delFromBasket(items[0])}
                            variant="secondary">
                            —
                          </Button>
                        </ButtonGroup>
                      </ListGroup.Item>
                    )
                  )}
                  <ListGroup.Item className="bg-dark text-white">
                    Total: £
                    {basket
                      .reduce((acc, curr) => acc + curr.price, 0.0)
                      .toFixed(2)}
                  </ListGroup.Item>
                </ListGroup>
              </div>
            </Col>
            <Col xs="8">
              {Array.from(menu.entries()).map(([category, items]) => (
                <React.Fragment key={category}>
                  <h2>{category}</h2>
                  <CardDeck>
                    {items.map(item => (
                      <Card
                        bg="dark"
                        text="white"
                        key={item.id}
                        className="mb-3 menu_item">
                        <Card.Img
                          className="img-fluid"
                          variant="top"
                          src={item.image}
                        />

                        <Card.Body>
                          <Card.Title>
                            <strong>{item.name}</strong>
                          </Card.Title>
                          <Card.Text>{item.description}</Card.Text>
                        </Card.Body>

                        <Card.Footer className="d-flex justify-content-between">
                          <h4 className="mt-1">£{item.price.toFixed(2)}</h4>
                          <Button
                            variant="success"
                            onClick={() => this.addToBasket(item)}>
                            Add
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
