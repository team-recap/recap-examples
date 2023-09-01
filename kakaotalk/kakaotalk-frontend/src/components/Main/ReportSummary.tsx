import React from 'react';
import Bubble from "@components/Main/Bubble";
import { ReportWrap, Title } from "@styles/Main";

const ReportSummary = () => {
  return (
    <ReportWrap>
      <Title>회의록 요약</Title>
      <Bubble />
    </ReportWrap>
  );
};

export default ReportSummary;