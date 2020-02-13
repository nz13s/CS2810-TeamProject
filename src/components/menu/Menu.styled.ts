import styled from "styled-components";

const MenuStyle = styled.div`
  display: flex;

  .menu_item {
    min-width: 30vh;
    max-width: 35vh;
  }

  .section_button {
    min-width: 13vw;
    // max-width: 15vw;
  }

  @media only screen and (max-device-width: 812px) {
    .menu_item {
      max-width: 100%;
    }

    //.section_button {
    //  max-width: 100%;
    //}

    #checkout_button {
      word-break: break-word;
      word-wrap: break-word;
      hyphens: auto;
      -ms-hyphens: auto;
      -moz-hyphens: auto;
      -webkit-hyphens: auto;
    }
  }
`;

export { MenuStyle };
