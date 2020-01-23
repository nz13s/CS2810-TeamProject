import styled from "styled-components";

const WaiterStyle = styled.div`
  display: flex;
  height: 100%;
  overflow-y: hidden;

  .waiter_button {
    height: 15vh;
    width: 20vw;
  }

  @media only screen and (max-device-width: 812px) {
    .waiter_button {
      height: 15vh;
      width: 40vw;
    }
  }
`;

export { WaiterStyle };
