import styled from "styled-components";

const AppStyle = styled.div`
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0.4;
  background-size:100% 100%;
  background-image: url(https://i.pinimg.com/originals/67/1c/fd/671cfd5e840afd6dd9dcf2a41060faa6.jpg);
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
  background: 0;
  color: #ffffff !important;
  padding: 20px;
  border-radius: 6px;
  display: inline-block;
  transition: all 0.3s ease 0s;
  box-shadow: 0px 0px 42px 41px rgba(0,0,0,0.75);
  width: auto;

  font-family: "LuloCleanW01-OneBold";
  text-align: center;
  color: white;
  letter-spacing: 0.11em;
  text-align: center;
  word-wrap:break-word;

  &:hover{
    color: black !important;
    font-weight: 700 !important;
    letter-spacing: 3px;
    background: white;
    -webkit-box-shadow: 0px 5px 40px -10px rgba(0,0,0,0.57);
    -moz-box-shadow: 0px 5px 40px -10px rgba(0,0,0,0.57);
    transition: all 0.3s ease 0s;
  }
`;

export { AppStyle, HeadingStyle, ButtonStyle };
