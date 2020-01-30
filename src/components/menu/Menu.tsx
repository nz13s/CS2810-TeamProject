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
  notifications: Array<React.ReactNode>;
}

const servlet = `backend_sprint1`;
const baseURL = `https://cors.x7.workers.dev/https://tomcat.xhex.uk/${servlet}`;

export default class Menu extends React.Component<any, State> {
  constructor(props: any) {
    super(props);

    this.state = {
      tableID: _.random(1, 8, false),
      menu: new Map(),
      basket: [],
      notifications: []
    };

    this.validateSession().then(isValid => {
      this.getSession(!isValid).then(() => {
        this.fetchMenu().then(menu => this.setState({ menu: menu }));
        this.fetchBasket().then(basket => this.setState({ basket: basket }));
      });
    });
  }

  async validateSession(): Promise<boolean> {
    try {
      await axios({
        method: "GET",
        url: `${baseURL}/menu`,
        headers: {
          "X-Session-ID": localStorage.getItem("session")
        }
      });
      return true;
    } catch (e) {
      return e.response.status !== 401;
    }
  }

  async getSession(forced = false) {
    if (localStorage.getItem("session") && !forced) return;

    try {
      const { headers } = await axios.get(`${baseURL}/hello`);
      localStorage.setItem("session", headers["x-session-id"]);
    } catch (e) {
      this.addNotification(
        `${e.message}: Couldn't fetch SessionID from server`
      );
    }
  }

  async fetchMenu(): Promise<MenuType> {
    let response;
    try {
      response = await axios({
        method: "GET",
        url: `${baseURL}/menu`,
        headers: {
          "X-Session-ID": localStorage.getItem("session")
        }
      });
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't fetch menu from server`);
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
      response = await axios({
        method: "GET",
        url: `${baseURL}/order`,
        headers: {
          "X-Session-ID": localStorage.getItem("session")
        }
      });
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't fetch order from server`);
      return [];
    }

    const { items } = response.data;

    return items.flatMap((item: any) => {
      const { food, amount } = item;
      const menuItem = new MenuItem(
        food.foodID,
        food.foodName,
        food.foodDescription,
        food.price,
        ""
      );
      return Array(amount).fill(menuItem);
    });
  }

  async addToBasket(item: MenuItem) {
    try {
      await axios({
        method: "POST",
        url: `${baseURL}/order?item=${item.id}&count=1`,
        headers: {
          "X-Session-ID": localStorage.getItem("session")
        }
      });
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't add item to order`);
      return;
    }

    const basket = await this.fetchBasket();
    this.setState({ basket: basket });
  }

  async delFromBasket(item: MenuItem) {
    try {
      await axios({
        method: "DELETE",
        url: `${baseURL}/order?item=${item.id}&count=1`,
        headers: {
          "X-Session-ID": localStorage.getItem("session")
        }
      });
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't remove item to order`);
      return;
    }

    const basket = await this.fetchBasket();
    this.setState({ basket: basket });
  }

  async saveBasket() {
    if (this.state.basket.length === 0)
      return this.addNotification("Cannot checkout empty order");

    try {
      const { tableID } = this.state;
      await axios({
        method: "POST",
        url: `${baseURL}/save?table_num=${tableID}`,
        headers: {
          "X-Session-ID": localStorage.getItem("session")
        }
      });
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't checkout your order`);
      return;
    }

    this.addNotification(
      "Successful checkout, your order is being prepared!",
      "Notification"
    );
    const basket = await this.fetchBasket();
    this.setState({ basket: basket });
  }

  addNotification(message: string, title = "Error") {
    const element = (
      <Toast
        key={_.random(1, 9999, false)}
        onClose={() =>
          this.setState({
            notifications: this.state.notifications.filter(x => x !== element)
          })
        }
        delay={8000}
        autohide={true}
        className="text-white bg-dark"
        style={{ width: "20vw" }}>
        <Toast.Header className="bg-white">
          <strong className="mr-auto text-primary">{title}</strong>
        </Toast.Header>
        <Toast.Body>{message}</Toast.Body>
      </Toast>
    );

    this.setState({ notifications: [element, ...this.state.notifications] });
  }

  render() {
    const { tableID, menu, basket, notifications } = this.state;

    return (
      <MenuStyle>
        <div
          className="pr-3 mr-3"
          style={{ zIndex: 2000, position: "fixed", top: 0, right: 0 }}>
          {notifications}
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
                  <ListGroup.Item className="bg-dark text-white">
                    <Button
                      id="checkout_button"
                      onClick={() => this.saveBasket()}
                      variant="success"
                      block>
                      Checkout
                    </Button>
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
