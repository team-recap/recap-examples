package org.recap.examples.kakaotalkbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.recap.Summarizer;
import org.recap.examples.kakaotalkbackend.component.MessageParser;
import org.recap.examples.kakaotalkbackend.dto.Message;
import org.recap.examples.kakaotalkbackend.dto.RequestPacket;
import org.recap.examples.kakaotalkbackend.dto.ResponsePacket;
import org.recap.graph.Graph;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
@AllArgsConstructor
public class SummarizerService {
    private final ObjectMapper mapper; // 클라이언트에게 메시지를 보낼 때 문자열로 변환시키 위한 클래스
    private final Summarizer summarizer = new Summarizer(); // recap 요약기

    public void request(WebSocketSession session, RequestPacket requestPacket) { // 카카오톡 대화 메시지 요약 수행
        Message[] messages = MessageParser.conversationToMessages(requestPacket.getText()); // 초기 메시지 데이터 구축

        // 100줄 이상의 대화는 서버 과부화 방지를 위해 차단
        if (messages.length >= 100) {
            // 오류 메시지 반환
            Message error = Message.builder()
                    .sender("오류")
                    .summarizedMessage("100개 이상의 메시지는 처리할 수 없습니다.")
                    .originalMessage("100개 이상의 메시지는 처리할 수 없습니다.")
                    .build();

            messages = new Message[1];
            messages[0] = error;
        } else { // 100줄 미만의 대화는 요약 처리 진행
            for (Message message : messages) {
                String summarizedMessage = "<ul>"; // 요약된 텍스트가 저장될 변수

                // summarize 메서드로 요약을 진행한 뒤 summarizedMessage에 누적
                for (String summarizedSentence : summarizer.summarize(message.getOriginalMessage(), Graph.SimilarityMethods.COSINE_SIMILARITY)) {
                    summarizedMessage = summarizedMessage.concat("<li>" + summarizedSentence + "</li>");
                }

                // 메시지에 요약본 저장
                message.setSummarizedMessage(summarizedMessage.concat("</ul>")); // 결과 텍스트 맨 마지막에 닫은 ul 태그 추가
            }
        }

        // 서버에서 사용자에게 전달할 패킷 생성
        ResponsePacket responsePacket = ResponsePacket.builder()
                .type(ResponsePacket.MessageType.DATA)
                .messages(messages)
                .build();
        sendMessage(session, responsePacket);
    }

    // 대기방 내의 특정 세션에게 메시지 전달
    public void sendMessage(WebSocketSession session, ResponsePacket message) {
        try {
            if (session.isOpen()) { // 세션이 열려 있을 경우에만 메시지를 전송함
                session.sendMessage(new TextMessage(mapper.writeValueAsString(message))); // 메시지 내용을 문자열로 변환하여 메시지를 전송함
                System.out.println(session.getId() + " <- " + message.toSimpleString());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
