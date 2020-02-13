import styled from "styled-components";

const WaiterStyle = styled.div`
  display: flex;

  .waiter_button {
    font-size: 2.5rem;
    word-break: break-word;
    word-wrap: break-word;
  }

  a:hover,
  a:visited,
  a:link,
  a:active {
    text-decoration: none;
  }

  @media only screen and (max-device-width: 812px) {
    .waiter_button {
    }
  }
`;

export { WaiterStyle };
