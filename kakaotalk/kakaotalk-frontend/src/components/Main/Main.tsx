import ReportInput from "@components/Main/ReportInput";
import ReportSummary from "@components/Main/ReportSummary";
import Navbar from "@components/Navbar/Navbar";
import { State, Tab } from "@states/State";
import { useRecoilValue } from "recoil";
import { styled } from "styled-components";

const Main = () => {
  const tab = useRecoilValue(State.tab);

  return (
    <Container>
      <Navbar />
      {
        tab === Tab.Input ? <ReportInput /> : <ReportSummary />
      }
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  gap: 15px;
`;

export default Main;