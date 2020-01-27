import styled from "styled-components";

const MenuStyle = styled.div`
  display: flex;

  .menu_item {
    min-width: 29%;
    max-width: 42%;
  }

  @media only screen and (max-device-width: 812px) {
    .menu_item {
      max-width: 100%;
    }
  }
`;

export { MenuStyle };
