import useSocket from "@hooks/useSocket";
import { Message } from "@states/State";
import { BubbleContainer, BubbleWrapContainer, Content, ContentTitle, ContentWrap, ProfileImg, ProfileName, ProfileWrap } from "@styles/Main";
import { useState } from 'react';

const Bubble = () => {
  const {
    messages
  } = useSocket();

  return (
    <BubbleContainer>
      {
        messages.map((message) => (
          <BubbleWrap key={Math.random()} message={message} />
        ))
      }
    </BubbleContainer>
  );
};

export default Bubble;

const BubbleWrap = ({ message }: { message: Message }) => {
  const [tab, setTab] = useState<boolean>(true);
  return <BubbleWrapContainer >
    <ProfileWrap>
      <ProfileImg src={`https://${process.env.REACT_APP_API_URL}${message.senderProfileImageUrl}`} alt={message.sender} />
      <ProfileName>{message.sender}</ProfileName>
    </ProfileWrap>
    <ContentWrap>
      <div style={{ display: "flex", marginBottom: "7px" }}>
        <ContentTitle
          onClick={() => { setTab(true) }}
          style={{ color: tab ? "#636363" : "#B7B7B7" }}
        >요약본</ContentTitle>
        <ContentTitle
          onClick={() => { setTab(false) }}
          style={{ color: tab ? "#B7B7B7" : "#636363" }}
        >원본</ContentTitle>
      </div>
      <Content>
        {tab ? (
          <div dangerouslySetInnerHTML={{ __html: message.summarizedMessage }} />
        ) : (
          message.originalMessage
        )}
      </Content>
    </ContentWrap>
  </BubbleWrapContainer>
}