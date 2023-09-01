import React, { useState } from 'react';
import { NavWrap, NavMenu } from "@styles/Main";

interface NavbarProps {
  toggleCallback: (value: boolean) => void;
}

const Navbar: React.FC<NavbarProps> = ({ toggleCallback }) => {
  const [activeMenu, setActiveMenu] = useState<boolean>(true);

  return (
    <NavWrap>
       <NavMenu
        onClick={() => {
          toggleCallback(true);
          setActiveMenu(true);
        }}
        active={activeMenu === true}
        >카카오톡 회의록 입력</NavMenu>
       <NavMenu 
        onClick={() => {
          toggleCallback(false);
          setActiveMenu(false);
        }}
        active={activeMenu === false}
       >결과 요약문</NavMenu>
    </NavWrap>
  );
};

export default Navbar;