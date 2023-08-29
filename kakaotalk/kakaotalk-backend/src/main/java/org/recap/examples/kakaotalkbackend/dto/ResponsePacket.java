package org.recap.examples.kakaotalkbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePacket {
    public enum MessageType { DATA, PONG }
    // DATA - 데이터
    // PONG - 세션 끊김 방지 (서버)

    private MessageType type; // 패킷 종류
    private Message[] messages; // 메시지 배열

    // 톰캣 로그용 메서드
    public String toSimpleString() {
        if (this.toString().length() > 150)
            return this.toString().replaceAll("\n", "/n").substring(0, 150) + "...";
        else
            return this.toString().replaceAll("\n", "/n");
    }
}
