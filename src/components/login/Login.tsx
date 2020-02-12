import React, { SyntheticEvent } from "react";
import { Button, Container, Form, InputGroup, Navbar } from "react-bootstrap";
import { LoginStyle } from "./Login.styled";
import API from "../../client/api";

interface State {
  successful: boolean;
}

export default class Login extends React.Component<any, State> {
  constructor(props: any) {
    super(props);

    this.state = {
      successful: false
    };
  }

  async handleSubmit(e: SyntheticEvent) {
    e.preventDefault();
    e.stopPropagation();

    const form = e.currentTarget as HTMLFormElement;
    const usernameElement = form.elements.namedItem(
      "username"
    ) as HTMLInputElement;
    const passwordElement = form.elements.namedItem(
      "password"
    ) as HTMLInputElement;
    const username = usernameElement.value;
    const password = passwordElement.value;

    const valid = await API.login(username, password);
    if (!valid) alert("Wrong Password");
    else window.location.href = window.location.origin;
  }

  render() {
    return (
      <LoginStyle>
        <Container>
          <Navbar className="mb-5 mt-1" variant="dark" bg="dark">
            <Navbar.Brand href="/#/">Oaxaca</Navbar.Brand>
          </Navbar>

          <div className="w-25" style={{ margin: "30vh auto" }}>
            <h2>Login</h2>

            <Form onSubmit={this.handleSubmit.bind(this)}>
              <Form.Group controlId="username">
                <InputGroup>
                  <InputGroup.Prepend>
                    <InputGroup.Text id="inputGroupPrepend">
                      Username
                    </InputGroup.Text>
                  </InputGroup.Prepend>
                  <Form.Control
                    type="text"
                    aria-describedby="inputGroupPrepend"
                    required
                  />
                </InputGroup>
              </Form.Group>

              <Form.Group controlId="password">
                <InputGroup>
                  <InputGroup.Prepend>
                    <InputGroup.Text id="inputGroupPrepend">
                      Password
                    </InputGroup.Text>
                  </InputGroup.Prepend>
                  <Form.Control
                    type="text"
                    aria-describedby="inputGroupPrepend"
                    required
                  />
                </InputGroup>
              </Form.Group>

              <Button type="submit">Submit</Button>
            </Form>
          </div>
        </Container>
      </LoginStyle>
    );
  }
}
