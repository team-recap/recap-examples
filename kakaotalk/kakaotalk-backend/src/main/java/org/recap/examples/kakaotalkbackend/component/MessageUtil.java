package org.recap.examples.kakaotalkbackend.component;

import org.recap.examples.kakaotalkbackend.dto.Message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MessageUtil {
    private static final String defaultProfileImageUrl = "/imgs/profile";
    private static final int numberOfProfileImages = 4;

    /*
    카카오톡 대화 데이터를 전처리하여 Message[] 구조로 반환

    · 카카오톡 대화 데이터 형식
      1 ~ 3번 줄 : 대화방 정보 (해당 메서드에서는 따로 처리하지 않음)
      4 ~ n번 줄 : '[작성자] [작성 시간] 메시지 내용' or '-----------날짜-----------'
    · 만약 같은 날에 똑같은 작성자가 연속으로 채팅을 친 경우 하나로 묶음
     */
    public static Message[] conversationToMessages(String conversation) {
        LinkedList<Message> messages = new LinkedList<>(); // 최종 결과
        String[] lines = conversation.split("\n"); // escape character로 문장을 나눔

        int lineNumber = 0; // 줄 수
        int lastDay = 0; // 이전 대화의 날짜
        int currentDay = 0; // 현재 대화의 날짜
        Map<String, String> profileImageUrls = new HashMap<>(); // 각 유저의 프로필 이미지 url이 저장됨
        for (String line : lines) {
            lineNumber++;
            if (lineNumber <= 3) // 채팅방 사람들의 이름과 저장한 날짜 행을 건너뜀
                continue;

            if (line.startsWith("-")) { // 날짜 변경선
                currentDay++;
            } else if (line.startsWith("[")) { // 채팅
                String[] splittedLine = line.split(" ", 4); // '[작성자]', '[오전/오후', '시간]', '메시지 내용' 에서 limit를 4로 줘 메시지 내용 안에서 띄어쓰기를 해도 무시하도록 함
                String sender = splittedLine[0].substring(1, splittedLine[0].length() - 1); // 작성자 추출
                String message = splittedLine[3]; // 메시지 내용 추출

                // 2000 글자 이상인 글은 서버 과부화 방지를 위해 차단
                if (message.length() >= 2000)
                    continue;

                if (lastDay == currentDay) { // 같은 날짜에 이야기 했다면
                    if (messages.getLast().getSender().equals(sender)) { // 같은 작성자인지 확인
                        if (messages.getLast().getOriginalMessage().length() < 2000) // 이전 글이 2000글자 미만인 경우에만 그룹화 진행
                            messages.getLast().appendOriginalMessage(message); // 이전 메시지와 그룹화 진행
                    } else { // 그렇지 않다면
                        // 이전에 채팅을 치지 않았던 사용자라면, 새로운 프로필 이미지 지정
                        if (!profileImageUrls.containsKey(sender))
                            profileImageUrls.put(sender, defaultProfileImageUrl +
                                    (profileImageUrls.size() % numberOfProfileImages + 1) + ".png");

                        messages.add(Message.builder().sender(sender)
                                .originalMessage(message)
                                .senderProfileImageUrl(profileImageUrls.get(sender))
                                .build()); // 새로운 메시지 그룹 생성
                    }
                } else { // 다른 날짜의 채팅이라면 새로운 메시지 그룹 생성
                    // 이전에 채팅을 치지 않았던 사용자라면, 새로운 프로필 이미지 지정
                    if (!profileImageUrls.containsKey(sender))
                        profileImageUrls.put(sender, defaultProfileImageUrl +
                                (profileImageUrls.size() % numberOfProfileImages + 1) + ".png");

                    messages.add(Message.builder().sender(sender)
                            .originalMessage(message)
                            .senderProfileImageUrl(profileImageUrls.get(sender))
                            .build()); // 새로운 메시지 그룹 생성
                    lastDay = currentDay;
                }
            }
        }
        return messages.toArray(new Message[0]);
    }
}
