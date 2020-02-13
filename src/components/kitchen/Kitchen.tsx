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
  interval: number;

  constructor(props: any) {
    super(props);

    this.state = {
      orders: [[], [], []]
    };

    API.getQueue().then(queue => this.setState({ orders: queue }));
    this.interval = setInterval(
      () => API.getQueue().then(queue => this.setState({ orders: queue })),
      1000
    );
  }

  async moveItem(currentIndex: number, delta: number, item: OrderItem) {
    await API.moveOrder(item.id, currentIndex + delta);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  render() {
    const { orders } = this.state;
    const [placed, preparing, ready] = orders;

    return (
      <KitchenStyle>
        <Container fluid className="mx-2">
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
                          Time ordered: {item.timeOrdered.toLocaleTimeString()}
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
                          Preparing since:{" "}
                          {item.timePreparing.toLocaleTimeString()}
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
                          Ready since: {item.timeReady.toLocaleTimeString()}
                        </Card.Subtitle>
                      </Card.Body>
                      <Card.Footer>
                        <Button
                          variant="warning"
                          onClick={() => this.moveItem(2, -1, item)}
                          block>
                          ⇐ Preparing
                        </Button>
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
