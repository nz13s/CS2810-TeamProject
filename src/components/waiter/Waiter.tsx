import React from "react";
import { Switch, Route, Link } from "react-router-dom";
import { WaiterStyle } from "./Waiter.styled";
import {
  Button,
  Card,
  Col,
  Container,
  ListGroup,
  Navbar,
  Row
} from "react-bootstrap";
import WaiterManagement from "./WaiterManagement";
import WaiterEdit from "./WaiterEdit";
import Notification from "../../entities/Notification";
import API from "../../client/api";

interface State {
  notifications: Array<Notification>;
}

export default class Waiter extends React.Component<any, State> {
  socket: WebSocket;

  constructor(props: any) {
    super(props);

    this.state = {
      notifications: []
    };

    API.getNotifications().then(notifications =>
      this.setState({ notifications })
    );

    this.socket = API.getSocket();
    this.socket.onmessage = e => {
      const { messageType, content } = JSON.parse(e.data);
      if (messageType === "UPDATE") return;

      console.log(content);

      const notif = new Notification(
        content.notificationID,
        "",
        content.message,
        new Date(content.time),
        content.type,
        null
      );

      if (messageType === "DELETE") {
        this.setState({
          notifications: this.state.notifications.filter(
            x => x.id !== content.notificationID
          )
        });
        return;
      }

      switch (content.type) {
        case "READY":
          console.log("READY");
          break;
        case "ASSIST":
          console.log("ASSIST");
          break;
        case "CONFIRM":
          console.log("CONFIRM PLS");
          notif.extra = content.extraData.orderID;
          break;
      }

      this.setState({ notifications: [...this.state.notifications, notif] });
    };
  }

  async confirmOrder(notificationID: number, orderID: number): Promise<void> {
    await API.confirmOrder(orderID);
    await API.delNotification(notificationID);
  }

  componentWillUnmount() {
    this.socket.close();
  }

  render() {
    const { notifications } = this.state;

    return (
      <Switch>
        <Route exact path="/waiter">
          <WaiterStyle>
            <Container fluid className="mx-2">
              <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
                <Navbar.Brand href="/#/">Oaxaca Waiter</Navbar.Brand>
              </Navbar>

              <Row>
                <Col xs="6">
                  <h2>Notifications</h2>
                  <ListGroup variant="flush" className="d-inline-block w-100">
                    {notifications.map((notification, idx) => (
                      <ListGroup.Item key={idx} className="bg-dark text-white">
                        <Card
                          border="danger"
                          className="mb-3 bg-dark text-white">
                          <Card.Header>
                            <strong>{notification.title}</strong>
                          </Card.Header>
                          <Card.Body>
                            <Card.Text>{notification.content}</Card.Text>
                          </Card.Body>
                          <Card.Footer>
                            {(() => {
                              switch (notification.kind) {
                                case "READY":
                                  console.log("READY");
                                  return;
                                case "ASSIST":
                                  console.log("ASSIST");
                                  return;
                                case "CONFIRM":
                                  return (
                                    <Button
                                      className="mr-3"
                                      variant="success"
                                      onClick={() =>
                                        this.confirmOrder(
                                          notification.id,
                                          notification.extra
                                        )
                                      }>
                                      Check Order
                                    </Button>
                                  );
                                default:
                                  return;
                              }
                            })()}
                            <Button
                              variant="success"
                              onClick={() =>
                                API.delNotification(notification.id)
                              }>
                              Dismiss
                            </Button>
                          </Card.Footer>
                        </Card>
                      </ListGroup.Item>
                    ))}
                  </ListGroup>
                </Col>

                <Col xs="6">
                  <h2>Settings</h2>
                  <div className="mb-2">
                    <Link to="/waiter/edit">
                      <Button
                        block
                        className="waiter_button"
                        variant="outline-danger">
                        Edit Menu
                      </Button>
                    </Link>
                  </div>
                  <div className="mb-4">
                    <Link to="/waiter/management">
                      <Button
                        block
                        className="waiter_button"
                        variant="outline-warning">
                        Table Management
                      </Button>
                    </Link>
                  </div>
                </Col>
              </Row>
            </Container>
          </WaiterStyle>
        </Route>

        <Route exact path="/waiter/management" component={WaiterManagement} />
        <Route exact path="/waiter/edit" component={WaiterEdit} />
      </Switch>
    );
  }
}
