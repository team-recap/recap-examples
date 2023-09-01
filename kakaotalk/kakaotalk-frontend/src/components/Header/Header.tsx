import HeaderImg from "@assets/images/Header.png";
import { HeaderWrap } from "@styles/Header";

const Header = () => {
  return (
    <>
      <HeaderWrap>
        <img style={{ width: 130, height: 25 }} src={HeaderImg} alt="logo" />
      </HeaderWrap>
    </>
  );
};

export default Header;