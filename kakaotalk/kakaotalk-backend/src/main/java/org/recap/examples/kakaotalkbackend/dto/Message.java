package org.recap.examples.kakaotalkbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String sender; // 메시지 작성자
    private String senderProfileImageUrl; // 메시지 작성자의 프로필 사진 url
    private String originalMessage; // 기존 메시지
    private String summarizedMessage; // 요약된 메시지

    // 기존 메시지와 새로운 메시지를 합침
    public void appendOriginalMessage(String message) {
        originalMessage = originalMessage.concat(" " + message);
    }
}
