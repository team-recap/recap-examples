## 실행 방법
1. local.properties 파일을 생성하고 디스코드 봇을 [생성](https://discord.com/developers)합니다.
2. Settings > Build-A-Bot > TOKEN 값을 local.properties에 작성합니다.
3. 디스코드 서버에 봇을 추가하고 소스 코드를 실행합니다.
### local.properties
```
discord.token=[디스코드 봇 TOKEN]
```

## 명령어
- !회의 [음성 채널 이름] : 봇이 음성 채널로 이동하여 사용자의 음성을 녹음합니다.
- !종료 : 음성 녹음을 종료합니다.