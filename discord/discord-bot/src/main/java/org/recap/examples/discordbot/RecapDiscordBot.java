package org.recap.examples.discordbot;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class RecapDiscordBot extends ListenerAdapter {
    private boolean recording = false; // 녹음 여부
    private AudioRecordHandler audioRecordHandler;

    public static void main(String[] args) {
        Properties properties = new Properties();

        /*
        local.properties 파일 생성 후 discord.token에 디스코드 봇 토큰 추가 필요
        discord.token=[디스코드 봇 토큰]
         */
        try (FileInputStream fis = new FileInputStream("local.properties")) {
            properties.load(fis);
            String token = properties.getProperty("discord.token");

            // 디스코드 봇 연결
            JDABuilder
                    .createDefault(token)
                    .addEventListeners(new RecapDiscordBot())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        // 봇이 추가된 채팅 채널에 메시지가 전송되면 가져옴
        String message = event.getMessage().getContentRaw();
        String startT = "!회의 ";
        if (message.startsWith(startT)) {
            // 메시지가 [!회의 {음성 채널 이름}]으로 시작하는 경우
            this.commandStart(event.getChannel(), message.substring(startT.length()));
        }
        else if (message.equalsIgnoreCase("!종료")) {
            // 메시지가 [!종료]인 경우
            this.commandStop(event.getChannel());
        }
    }

    private void commandStart(TextChannel channel, String room) {
        if (!this.recording) { // 녹화가 진행되고 있지 않을 때만 실행

            Guild guild = channel.getGuild();
            List<VoiceChannel> channels = guild.getVoiceChannels(); // 디스코드 서버에 있는 음성 채널 목록

            // ${channels}에서 ${room} 음성 채널 검색
            for (VoiceChannel c : channels) {
                System.out.println(c.getName());
                if (c.getName().equals(room)) { // ${room}이 있다면 음성 채널 연결
                    this.recording = true;
                    this.audioRecordHandler = new AudioRecordHandler(System.currentTimeMillis() + "_" + room);

                    // 음성 채널의 id를 가져와서 봇을 채널에 연결
                    VoiceChannel voiceChannel = guild.getVoiceChannelById(c.getIdLong());
                    guild.getAudioManager().openAudioConnection(voiceChannel);

                    // audioRecordHandler를 설정하고 채널에 메시지 전송
                    guild.getAudioManager().setReceivingHandler(this.audioRecordHandler);
                    channel.sendMessage("녹화를 시작합니다.").queue();
                    return;
                }
            }
            channel.sendMessage("'" + room + "' 음성 채널을 찾을 수 없습니다.").queue();
        } else {
            channel.sendMessage("'" + this.audioRecordHandler.getRoom() + "' 채널에서 진행 중인 회의가 있습니다.").queue();
        }
    }

    private void commandStop(TextChannel channel) {
        if (this.recording) { // 녹화가 진행되고 있을 때만 실행
            this.recording = false;
            Guild guild = channel.getGuild();

            // 봇과 음성 채널의 연결을 해제
            guild.getAudioManager().closeAudioConnection();

            // 녹음 종료
            this.audioRecordHandler.stopRecording();
            channel.sendMessage("녹화를 종료합니다.").queue();
        } else {
            channel.sendMessage("진행 중인 회의가 없습니다.").queue();
        }
    }
}
