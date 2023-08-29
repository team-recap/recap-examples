package org.recap.examples.kakaotalkbackend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestPacket {
    public enum MessageType { DATA, PING }
    // DATA - 데이터
    // PING - 세션 끊김 방지 (클라이언트)

    private MessageType type; // 패킷 종류
    private String text; // 카카오톡 대화 데이터

    // 톰캣 로그용 메서드
    public String toSimpleString() {
        if (this.toString().length() > 150)
            return this.toString().replaceAll("\n", "/n").substring(0, 150) + "...";
        else
            return this.toString().replaceAll("\n", "/n");
    }
}
