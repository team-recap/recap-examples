import { styled } from "styled-components";

interface NavMenuProps {
  active: boolean;
}

export const MainWrap = styled.div`
  display: flex;
  margin-top: 5px;
  justify-content: center;
`;

export const NavWrap = styled.div`
  width: 330px;
  /* height: 85px; */
`;

export const NavMenu = styled.div<NavMenuProps>`
  width: 300px;
  height: 50px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  line-height: 15.93px;
  display: flex;
  align-items: center;
  padding-left: 15px;
  margin-bottom: 5px;
  background-color: ${(props) => (props.active ? "#FFB049" : "transparent")};
  color: ${(props) => (props.active ? "#fff" : "#3e3e3e")};

  &:hover {
    background-color: ${(props) => (props.active ? "#FFB049" : "#F6F6F6")};
  }
`;

export const ReportWrap = styled.div`
  border-left: 1px #f6f6f6 solid;
  padding-left: 15px;
  width: 770px;
`;

export const TitleWrap = styled.div`
  width: 750px;
  margin-bottom: 15px;
`;

export const Title = styled.div`
  font-weight: 700;
  font-size: 18px;
  line-height: 20.27px;
  margin-bottom: 15px;
  color: #3e3e3e;
`;

export const SubTitle = styled.div`
  font-weight: 500;
  font-size: 14px;
  color: #969696;
`;

export const TextArea = styled.textarea`
  width: 750px;
  height: 200px;
  border-radius: 10px;
  border: 1px solid #efefef;
  margin-bottom: 15px;
  font-size: 14px;
  font-weight: 500;
  line-height: 17px;
  letter-spacing: 0em;
  padding: 10px;
  text-align: left;
  resize: none;
  box-sizing: border-box;

  &::placeholder {
    color: #a5a5a5;
  }
  &:focus {
    animation-timing-function: ease-out;
    animation-duration: 100ms;
  }
`;

export const Button = styled.button`
  width: 750px;
  height: 45px;
  border-radius: 10px;
  background-color: #ffb049;
  font-weight: 500;
  font-size: 14px;
  line-height: 17.38px;
  color: #fff;
  border: 0;
  cursor: pointer;

  &:hover {
    background-color: #e39939;
  }
`;

export const BubbleContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

export const BubbleWrapContainer = styled.div`
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  align-content: flex-start;
`;

export const ProfileWrap = styled.div`
  display: flex;
  align-items: center;
`;

export const ProfileImg = styled.img`
  width: 35px;
  height: 35px;
  margin-right: 10px;
`;

export const ProfileName = styled.div`
  font-size: 13px;
  font-weight: 500;
  letter-spacing: 0em;
  text-align: left;
  color: #3e3e3e;
`;

export const ContentWrap = styled.div`
  border-radius: 10px;
  background-color: #f6f6f6;
  padding: 15px;
  margin-top: 12px;
`;

export const ContentTitle = styled.div`
  font-size: 13px;
  font-weight: 500;
  letter-spacing: 0em;
  text-align: left;
  padding-right: 10px;
  cursor: pointer;
`;

export const Content = styled.div`
  font-size: 14px;
  font-weight: 500;
  line-height: 25px;
  letter-spacing: 0em;
  text-align: left;
  max-width: 710px;
  color: #3e3e3e;

  ul {
    margin: 0;
    padding: 0 20px;
  }
`;
