import styled from "styled-components";

const AppStyle = styled.div`
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: -1;
  background-size: 100% 100%;
  background-image: url(https://i.pinimg.com/originals/67/1c/fd/671cfd5e840afd6dd9dcf2a41060faa6.jpg);
`;

const CenterStyle = styled.div`
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 0;
`;

const LoginStyle = styled.div`
  height: 100%;
  position:absolute;
  top:5%;
  right:5%;
  z-index: 1;
`;

const HeadingStyle = styled.h1`
  font-family: "LuloCleanW01-OneBold";
  font-size: 500%;
  text-align: center;
  color: white;
  letter-spacing: 0.11em;
  text-align: center;
`;

const MenuButtonStyle = styled.button`
  border: none;
  background: rgba(0,0,0,0.8);
  color: #ffffff !important;
  padding: 20px;
  border-radius: 6px;
  display: inline-block;
  transition: all 0.3s ease 0s;
  box-shadow: 0px 0px 45px 45px rgba(0, 0, 0, 0.89);
  width: auto;

  font-family: "LuloCleanW01-OneBold";
  text-align: center;
  color: white;
  letter-spacing: 0.11em;
  text-align: center;
  word-wrap: break-word;

  &:hover {
    color: black !important;
    font-weight: 700 !important;
    letter-spacing: 3px;
    background: white;
    -webkit-box-shadow: 0px 5px 40px -10px rgba(0, 0, 0, 0.57);
    -moz-box-shadow: 0px 5px 40px -10px rgba(0, 0, 0, 0.57);
    transition: all 0.3s ease 0s;
  }
`;

const LoginButtonStyle = styled.button`
  border: none;
  background: rgba(0,0,0,0.8);
  color: #ffffff !important;
  padding: 20px;
  border-radius: 6px;
  display: inline-block;
  transition: all 0.3s ease 0s;
  box-shadow: 0px 0px 45px 45px rgba(0, 0, 0, 0.89);
  width: auto;
  border-radius: 50%;
  background-image: url(https://library.kissclipart.com/20180830/cge/kissclipart-padlock-clipart-padlock-clip-art-5f140843fc866a30.jpg);
  background-size: 100% 100%;
  &:hover {
    color: black !important;
    -webkit-box-shadow: 0px 5px 40px -10px rgba(0, 0, 0, 0.57);
    -moz-box-shadow: 0px 5px 40px -10px rgba(0, 0, 0, 0.57);
    transition: all 0.3s ease 0s;
  }
`;

export { AppStyle, LoginButtonStyle, CenterStyle, HeadingStyle, MenuButtonStyle, LoginStyle };
