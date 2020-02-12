import React from "react";
import _ from "lodash";

import { KitchenStyle } from "./Kitchen.styled";
import {
  Button,
  ButtonGroup,
  Card,
  Col,
  Container,
  ListGroup,
  Navbar,
  Row
} from "react-bootstrap";
import OrderItem from "../../entities/OrderItem";
import QueueType from "../../entities/QueueType";
import API from "../../client/api";

interface State {
  orders: QueueType;
}

export default class Kitchen extends React.Component<any, State> {
  constructor(props: any) {
    super(props);

    this.state = {
      orders: [[], [], []]
    };

    API.getQueue().then(queue => this.setState({ orders: queue }));
    setInterval(() => API.getQueue(), 2500);
  }

  async moveItem(currentIndex: number, delta: number, item: OrderItem) {
    API.moveOrder(item.id, currentIndex + 1);

    const orders = [...this.state.orders];
    orders[currentIndex] = orders[currentIndex].filter(x => x.id !== item.id);

    if (currentIndex + delta <= 2)
      orders[currentIndex + delta] = [...orders[currentIndex + delta], item];

    this.setState({ orders: orders });
  }

  render() {
    const { orders } = this.state;
    const [placed, preparing, ready] = orders;

    return (
      <KitchenStyle>
        <Container>
          <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
            <Navbar.Brand href="/#/">Oaxaca Kitchen</Navbar.Brand>
          </Navbar>

          <div className="mb-5 d-flex justify-content-center">
            <h1>
              Orders Waiting:{" "}
              {orders.reduce((acc, curr) => acc + curr.length, 0)}
            </h1>
          </div>

          <Row>
            <Col>
              <h2>Placed Orders: {placed.length}</h2>
              <ListGroup>
                {_.sortBy(placed, x => x.rank).map(item => (
                  <ListGroup.Item key={item.id} className="bg-dark text-white">
                    <Card border="danger" className="mb-3 bg-dark text-white">
                      <Card.Header>Order #{item.id}</Card.Header>
                      <Card.Body>
                        {Object.entries(_.groupBy(item.foods, x => x.id)).map(
                          ([, items], idx) => (
                            <Card.Text key={idx}>
                              {items.length}x {items[0].name}
                              <br />
                            </Card.Text>
                          )
                        )}
                        <Card.Subtitle>
                          Time Ordered: {item.ordered.toLocaleTimeString()}
                        </Card.Subtitle>
                      </Card.Body>
                      <Card.Footer>
                        <Button
                          variant="warning"
                          onClick={() => this.moveItem(0, 1, item)}
                          block>
                          Preparing ⇒
                        </Button>
                      </Card.Footer>
                    </Card>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            </Col>

            <Col>
              <h2>Preparing: {preparing.length}</h2>
              <ListGroup>
                {_.sortBy(preparing, x => x.rank).map(item => (
                  <ListGroup.Item key={item.id} className="bg-dark text-white">
                    <Card border="warning" className="mb-3 bg-dark text-white">
                      <Card.Header>Order #{item.id}</Card.Header>
                      <Card.Body>
                        {Object.entries(_.groupBy(item.foods, x => x.id)).map(
                          ([, items], idx) => (
                            <Card.Text key={idx}>
                              {items.length}x {items[0].name}
                              <br />
                            </Card.Text>
                          )
                        )}
                        <Card.Subtitle>
                          Time Ordered: {item.ordered.toLocaleTimeString()}
                        </Card.Subtitle>
                      </Card.Body>
                      <Card.Footer>
                        <ButtonGroup className="d-flex">
                          <Button
                            variant="danger"
                            onClick={() => this.moveItem(1, -1, item)}>
                            ⇐ Placed
                          </Button>
                          <Button
                            variant="success"
                            onClick={() => this.moveItem(1, 1, item)}>
                            Ready ⇒
                          </Button>
                        </ButtonGroup>
                      </Card.Footer>
                    </Card>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            </Col>

            <Col>
              <h2>Ready: {ready.length}</h2>
              <ListGroup>
                {_.sortBy(ready, x => x.rank).map(item => (
                  <ListGroup.Item key={item.id} className="bg-dark text-white">
                    <Card border="success" className="mb-3 bg-dark text-white">
                      <Card.Header>Order #{item.id}</Card.Header>
                      <Card.Body>
                        {Object.entries(_.groupBy(item.foods, x => x.id)).map(
                          ([, items], idx) => (
                            <Card.Text key={idx}>
                              {items.length}x {items[0].name}
                              <br />
                            </Card.Text>
                          )
                        )}
                        <Card.Subtitle>
                          Time Ordered: {item.ordered.toLocaleTimeString()}
                        </Card.Subtitle>
                      </Card.Body>
                      <Card.Footer>
                        <ButtonGroup className="d-flex">
                          <Button
                            variant="warning"
                            onClick={() => this.moveItem(2, -1, item)}>
                            ⇐ Preparing
                          </Button>
                          <Button onClick={() => this.moveItem(2, 1, item)}>
                            Served
                          </Button>
                        </ButtonGroup>
                      </Card.Footer>
                    </Card>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            </Col>
          </Row>
        </Container>
      </KitchenStyle>
    );
  }
}
