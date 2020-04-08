import styled from "styled-components";

const AppStyle = styled.div`
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const HeadingStyle = styled.h1`
  font-family: "LuloCleanW01-OneBold";
  text-align: center;
  color: white;
  letter-spacing: 0.11em;
  text-align: center;
`;

const ButtonStyle = styled.button`
  border: none;
  background: #404040;
  color: #ffffff !important;
  font-weight: 100;
  padding: 20px;
  text-transform: uppercase;
  border-radius: 6px;
  display: inline-block;
  transition: all 0.3s ease 0s;
  box-shadow: 0px 0px 42px 41px rgba(0,0,0,0.75);

  &:hover{
    color: #404040 !important;
    font-weight: 700 !important;
    letter-spacing: 3px;
    background: none;
    -webkit-box-shadow: 0px 5px 40px -10px rgba(0,0,0,0.57);
    -moz-box-shadow: 0px 5px 40px -10px rgba(0,0,0,0.57);
    transition: all 0.3s ease 0s;
  }
`;

export { AppStyle, HeadingStyle, ButtonStyle };
