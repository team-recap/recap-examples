package org.recap.examples.discordbot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat.Type;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class AudioRecordHandler implements AudioReceiveHandler {
    static final int RECORD_DELAY = 1000; // 1초 딜레이
    private final Map<Long, ByteArrayOutputStream> userAudioMap = new HashMap<>(); // 음성 데이터
    private final Map<Long, Pair<String, Timer>> userTimerMap = new HashMap<>(); // 유저별로 문장을 끊어 저장하기 위한 타이머
    private boolean recording = true; // 녹음 여부
    private final String room; // 음성 채널 이름

    public AudioRecordHandler(String room) {
        this.room = room;
    }

    public String getRoom() {
        return this.room;
    }

    public void handleUserAudio(@NotNull UserAudio userAudio) {
        try {
            // 말을 하고 있는 유저의 id를 가져옴
            long userId = userAudio.getUser().getIdLong();
            byte[] audioData = userAudio.getAudioData(1.0);

            // 음성 데이터 출력
            System.out.println(Arrays.toString(audioData));

            // 저장된 음성 데이터가 없다면 새로운 ByteArrayOutputStream을 생성
            if (this.userAudioMap.get(userId) == null) {
                this.userAudioMap.put(userId, new ByteArrayOutputStream());
            }

            // 디스코드에서 받아온 음성 데이터 추가
            this.userAudioMap.get(userId).write(audioData);

            // 이미 사용자의 타이머가 작동 중이라면 멈추기
            if (this.userTimerMap.get(userId) != null) {
                ((Timer)((Pair<?, ?>)this.userTimerMap.get(userId)).getRight()).cancel();
            }

            // 타이머 추가
            String name = userAudio.getUser().getName();
            Timer timer = this.getTimer(userId, userAudio.getUser().getName());
            this.userTimerMap.put(userId, Pair.of(name, timer));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Timer getTimer(final Long userId, final String name) {
        TimerTask task = new TimerTask() {
            public void run() {
                // RECORD_DELAY 만큼 말을 하지 않았다면 음성 데이터를 파일로 저장
                AudioRecordHandler.this.saveVoice(userId, name);
            }
        };
        // RECORD_DELAY 후에 타이머 실행
        Timer timer = new Timer();
        timer.schedule(task, RECORD_DELAY);
        return timer;
    }

    /**
     * 음성 데이터를 wav 파일로 저장
     * @param userId 음성 데이터를 저장할 사용자의 디스코드 userId
     * @param name 사용자 이름
     */
    public void saveVoice(Long userId, String name) {
        // 해당하는 사용자의 음성 데이터를 가져옴
        ByteArrayOutputStream outputStream = this.userAudioMap.get(userId);

        if (outputStream != null) {
            AudioFormat audioFormat = AudioReceiveHandler.OUTPUT_FORMAT;
            try {
                byte[] audioData = outputStream.toByteArray();
                AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(audioData), audioFormat, audioData.length / audioFormat.getFrameSize());

                // 음성 데이터의 길이를 구함
                float durationInMillis = (float)(1000L * ais.getFrameLength()) / audioFormat.getFrameRate();
                if (durationInMillis >= 1000.0F) { // 길이가 1초 이상이라면 파일로 저장

                    // audio/{채널 이름}/{사용자명}/{현재시간.wav} 구조로 파일을 저장
                    File root = new File("audio/" + this.room + "/" + name);
                    String path = root.getPath();
                    File file = new File(path + "/" + System.currentTimeMillis() + ".wav");

                    // 폴더가 없다면 생성
                    boolean mkdirs = root.mkdirs();

                    // 파일에 음성 데이터를 저장
                    AudioSystem.write(ais, Type.WAVE, file);
                    ais.close();
                    System.out.println("saveVoice: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // 파일로 저장한 음성 데이터를 삭제
            this.userAudioMap.remove(userId);
            this.userTimerMap.remove(userId);
        }
    }

    /**
     * recording이 true인 경우 디스코드로부터 음성 데이터를 받을 수 있고 false인 경우 받지 않음
     */
    public boolean canReceiveUser() {
        return this.recording;
    }

    public void stopRecording() {
        this.recording = false;
        // 아직 저장되지 않은 음성 데이터가 있다면 저장
        for (Long key : this.userTimerMap.keySet()) {
            this.saveVoice(key, (String) ((Pair<?, ?>) this.userTimerMap.get(key)).getLeft());
        }
    }
}
