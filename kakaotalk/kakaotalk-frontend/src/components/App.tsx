import Header from "@components/Header/Header";
import Main from "@components/Main/Main";
import { styled } from "styled-components";

function App() {
  return (
    <Container>
      <InnerContainer>
        <Header />
        <Main />
      </InnerContainer>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  justify-content: center;
  min-width: 1100px;
`;

const InnerContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin-top: 50px;
  max-width: 1100px;
`;

export default App;
