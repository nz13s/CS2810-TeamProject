import React from "react";
import {
  Button,
  ButtonGroup,
  Card,
  CardGroup,
  Col,
  Container,
  ListGroup,
  Navbar,
  Row,
  Toast
} from "react-bootstrap";
import { MenuStyle } from "./Menu.styled";
import _ from "lodash";

import MenuItem from "../../entities/MenuItem";
import MenuType from "../../entities/MenuType";
import BasketType from "../../entities/BasketType";
import API from "../../client/api";
import { IoIosWarning } from "react-icons/all";
import { Link, Route, Switch } from "react-router-dom";
import OrderTrack from "./OrderTrack";

interface State {
  tableID: number;
  menu: MenuType;
  basket: BasketType;
  notifications: Array<React.ReactNode>;
}

export default class Menu extends React.Component<any, State> {
  private disabled = true;
  constructor(props: any) {
    super(props);

    this.state = {
      tableID: _.random(1, 8, false),
      menu: new Map(),
      basket: [],
      notifications: []
    };

    this.fetchMenu();
    this.fetchBasket();
  }

  async fetchMenu(): Promise<void> {
    try {
      const menu = await API.fetchMenu();
      this.setState({ menu });
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't fetch menu from server`);
    }
  }

  async fetchBasket(): Promise<void> {
    try {
      const basket = await API.fetchBasket();
      this.setState({ basket });
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't fetch order from server`);
    }
  }

  async addToBasket(item: MenuItem): Promise<void> {
    try {
      await API.addToBasket(item);
      await this.fetchBasket();
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't add item to order`);
    }
  }

  async delFromBasket(item: MenuItem): Promise<void> {
    try {
      await API.delFromBasket(item);
      await this.fetchBasket();
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't remove item from order`);
    }
  }

  async saveBasket(): Promise<void> {
    if (this.state.basket.length === 0)
      return this.addNotification("Cannot checkout empty order");

    try {
      await API.saveBasket(this.state.tableID);
      await this.fetchBasket();
      this.addNotification(
        "Successful checkout, your order is being prepared! You can track your order by pressing the 'Track Order' button.",
        "Notification"
      );
      this.disabled = false;
    } catch (e) {
      this.addNotification(`${e.message}: Couldn't checkout your order`);
    }
  }

  async callWaiter(tableID: number): Promise<void> {
    await API.callWaiter(tableID);
    this.addNotification("Waiter called!", "Notification");
  }

  addNotification(message: string, title = "Error"): void {
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

  scrollToTarget(id: string): void {
    const element = document.getElementById(id);
    if (!element) return;

    element.scrollIntoView({
      block: "start",
      behavior: "smooth"
    });
  }

  cancelOrder() {

  }

  render() {
    const { tableID, menu, basket, notifications } = this.state;

    return (
      <Switch>
        <MenuStyle>
          <div
            className="pr-3 mr-3"
            style={{ zIndex: 2000, position: "fixed", top: 0, right: 0 }}>
            {notifications}
          </div>
          <Container fluid className="mx-2">
            <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
              <Navbar.Brand href="/#/">Oaxaca Menu</Navbar.Brand>
              <Navbar.Collapse className="d-flex justify-content-center">
                <Navbar.Brand>Table #{tableID}</Navbar.Brand>
                <Navbar.Brand>
                  <Button
                    variant="warning"
                    onClick={() => this.callWaiter(tableID)}>
                    Call Waiter
                  </Button>
                </Navbar.Brand>
              </Navbar.Collapse>
            </Navbar>
            <Row>
              <Col xs="3">
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
                    <ListGroup.Item className="bg-dark text-white">
                      <Link to="/menu/ordertrack">
                        <OrderTrack getTableNum={tableID} />
                        <Button
                          id="order_track_button"
                          variant="success"
                          block
                          disabled={this.disabled}>
                          Track Order
                        </Button>
                      </Link>
                    </ListGroup.Item>
                    <ListGroup.Item className="bg-dark text-white">
                      <Button
                        id="cancel_order_button"
                        onClick={() => this.cancelOrder()}
                        variant="success"
                        block
                        disabled={this.disabled}>
                        Cancel Order
                      </Button>
                    </ListGroup.Item>
                  </ListGroup>
                </div>
              </Col>
              <Col xs="9">
                <div
                  id="sections"
                  className="sticky-top mb-2 text-white rounded"
                  style={{ backgroundColor: "#1d1d1d" }}>
                  <ButtonGroup className="d-flex flex-wrap justify-content-center">
                    {Array.from(menu.keys()).map((category, idx) => (
                      <Button
                        key={idx}
                        className="section_button rounded mx-1 my-1"
                        variant="secondary"
                        onClick={() =>
                          this.scrollToTarget(`section_${category}`)
                        }>
                        {category}
                      </Button>
                    ))}
                  </ButtonGroup>
                </div>

                {Array.from(menu.entries()).map(([category, items]) => (
                  <React.Fragment key={category}>
                    <div
                      id={`section_${category}`}
                      style={{
                        position: "relative",
                        top: "-12vh"
                      }}
                    />
                    <h1>{category}</h1>
                    <CardGroup>
                      {items.map(item => (
                        <Card
                          bg="dark"
                          text="white"
                          key={item.id}
                          className="mb-3 mr-2 menu_item">
                          <Card.Img
                            className="img-fluid menu_image"
                            variant="top"
                            src={item.image}
                          />

                          <Card.Body>
                            <Card.Title>
                              <strong>{item.name}</strong>
                            </Card.Title>
                            <Card.Text>{item.description}</Card.Text>
                            <Card.Text>
                              {item.ingredients.map((ingredient, idx) => (
                                <React.Fragment key={idx}>
                                  {ingredient.allergen ? <IoIosWarning /> : ""}{" "}
                                  {ingredient.name}
                                  {idx === item.ingredients.length - 1
                                    ? ""
                                    : ", "}
                                </React.Fragment>
                              ))}
                            </Card.Text>
                            <Card.Text>{item.calories}kcal</Card.Text>
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
                    </CardGroup>
                  </React.Fragment>
                ))}
              </Col>
            </Row>
          </Container>
        </MenuStyle>

        <Route exact path="/menu/ordertrack" component={OrderTrack} />
      </Switch>
    );
  }
}
