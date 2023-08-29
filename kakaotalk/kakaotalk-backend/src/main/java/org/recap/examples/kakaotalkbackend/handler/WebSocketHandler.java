package org.recap.examples.kakaotalkbackend.handler;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.recap.examples.kakaotalkbackend.dto.RequestPacket;
import org.recap.examples.kakaotalkbackend.dto.ResponsePacket;
import org.recap.examples.kakaotalkbackend.service.SummarizerService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS) // json으로 파싱할 때 데이터 내의 escape 문자 무시
            .build();
    private final SummarizerService service;

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
        RequestPacket requestPacket = mapper.readValue(message.getPayload(), RequestPacket.class);

        System.out.println(session.getId() + " -> " + requestPacket.toSimpleString());

        if (requestPacket.getType().equals(RequestPacket.MessageType.DATA)) { // 사용자가 카카오톡 대화 데이터를 보낸 경우
            service.request(session, requestPacket);
        } else if (requestPacket.getType().equals(RequestPacket.MessageType.PING)) { // 사용자가 소켓 통신 유지를 위한 PING을 보낸 경우
            service.sendMessage(session, ResponsePacket.builder().type(ResponsePacket.MessageType.PONG).build()); // 사용자에게 PONG을 보냄
        }
    }
}
