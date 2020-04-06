import React from "react";
import API from "../../client/api";
import Notification from "../../entities/Notification";
import { Button, Card, ListGroup } from "react-bootstrap";
import { Route, Switch } from "react-router-dom";

interface State {
  notifications: Array<Notification>;
}

export default class OrderTrack extends React.Component<any, State> {
  interval: number;
  tableNum: number = this.props.getTableNum;

  constructor(props: any) {
    super(props);

    this.state = {
      notifications: []
    };

    API.getCustomerNotifications(this.tableNum).then(notifications =>
      this.setState({ notifications })
    );

    this.interval = setInterval(
      () =>
        API.getCustomerNotifications(this.tableNum).then(notifications =>
          this.setState({ notifications })
        ),
      1000
    );
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  render() {
    const { notifications } = this.state;

    return (
      <Switch>
        <Route exact path="/menu/ordertrack">
          <ListGroup variant="flush" className="d-inline-block w-100">
            {notifications.map((notification, idx) => (
              <ListGroup.Item key={idx} className="bg-dark text-white">
                <Card border="danger" className="bg-dark text-white">
                  <Card.Header>
                    <strong>{notification.title}</strong>
                  </Card.Header>
                  <Card.Body>
                    <Card.Text style={{ fontSize: 15 }}>
                      {notification.content}
                    </Card.Text>
                    <Card.Subtitle>
                      {notification.time.toLocaleTimeString()}
                    </Card.Subtitle>
                  </Card.Body>
                  <Card.Footer>
                    <Button
                      variant="success"
                      onClick={() =>
                        API.delCustomerNotification(notification.id)
                      }>
                      Dismiss
                    </Button>
                  </Card.Footer>
                </Card>
              </ListGroup.Item>
            ))}
          </ListGroup>
        </Route>
      </Switch>
    );

    //return <h1>Order Tracking stuff</h1>;
  }
}
