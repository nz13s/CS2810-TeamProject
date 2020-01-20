import React from "react";

import {AppStyle} from "./App.styled";

interface State {

}

export default class App extends React.Component<any, State> {

  constructor(props: any) {
    super(props);
    this.state = {};
  }


  render() {
    return (
      <AppStyle>
        <p>
          Restaurant Management System
        </p>
      </AppStyle>
    );
  }
}
