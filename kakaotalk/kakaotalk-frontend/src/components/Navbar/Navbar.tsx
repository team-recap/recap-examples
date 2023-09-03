import { State, Tab } from "@states/State";
import { NavMenu, NavWrap } from "@styles/Main";
import React from 'react';
import { useRecoilState } from "recoil";

interface NavbarProps { }

const Navbar: React.FC<NavbarProps> = () => {
  const [tab, setTab] = useRecoilState(State.tab);

  return (
    <NavWrap>
      <NavMenu
        onClick={() => {
          setTab(Tab.Input);

        }}
        active={tab === Tab.Input}
      >카카오톡 회의록 입력</NavMenu>
      <NavMenu
        onClick={() => {
          setTab(Tab.Summary);

        }}
        active={tab === Tab.Summary}
      >결과 요약문</NavMenu>
    </NavWrap>
  );
};

export default Navbar;