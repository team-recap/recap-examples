import React, { useState } from 'react';
import Navbar from "@components/Navbar/Navbar";
import ReportInput from "@components/Main/ReportInput";
import ReportSummary from "@components/Main/ReportSummary";
import { MainWrap } from "@styles/Main";

const Main = () => {
  const [toggle, setToggle] = useState<boolean>(true);

  const handleToggle = (value: boolean) => {
    setToggle(value);
  };

  return (
    <MainWrap>
      <Navbar toggleCallback={handleToggle} />
      {toggle ? <ReportInput /> : <ReportSummary />}
    </MainWrap>
  );
};

export default Main;