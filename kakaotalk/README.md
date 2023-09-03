# Recap Examples - Kakaotalk

![](./images/result.png)
> [사이트 이동](https://recap-examples.junyoung.dev/)<br><br>본 프로젝트는 [Recap](https://github.com/team-recap/recap) 라이브러리를 통해 카카오톡에서 진행한 회의를 자동으로 요약해주는 예제입니다. 카카오톡의 `대화 내보내기` 기능을 통해 얻은 대화 내용을 사이트 내에 입력하면 자동으로 회의 내용을 요약하여 보여줍니다.


## 이용 방법
### 1. 대화 데이터 추출
![](./images/step1.png)<br>
카카오톡 채팅방 내의 우측 상단 `더보기 버튼` > `대화 내용` > `대화 내보내기` 클릭 후 txt 파일 저장

### 2. 데이터 입력
저장한 txt 파일을 열어 전체 내용을 사이트 내 입력창에 입력

### 3. 요약 시작
사이트 하단의 `요약하기` 클릭


## 기술 스택
### Frontend
![React](https://shields.io/badge/react-black?logo=react&style=for-the-badge)
### Backend
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)


## 프로젝트 구조
![](./images/structure.png)

본 웹 서비스의 전반적인 클라이언트와 서버 간 통신은 웹 소켓을 통해 이루어집니다. 이때 소켓통신을 진행하는 이유는 대화를 요약한 결과 반환이 메시지의 길이에 따라 지연될 수 있기 때문입니다. 그러면 차례대로 데이터 플로우를 살펴보겠습니다.
### 1. 먼저, 소켓 통신을 통해 클라이언트가 서버로 대화 데이터를 전송합니다.
텍스트 형태로 이루어진 데이터를 클라이언트가 서버로 전달합니다.
### 2. 서버에서는 받은 데이터에서 메시지 데이터만 추출한 뒤 사용자별로 메시지를 정리합니다.
받은 데이터에서 대화방 이름, 참여자 이름 리스트는 필요 없기 때문에 이를 나타내는 데이터 row를 삭제하여 메시지를 저장합니다. 그리고 메시지에서 한 사람이 연속적으로 여러번 말한 경우 처음 메시지에 다른 메시지들을 이어붙입니다. 이때 만약 같은 사람이 작성한 두 메시지의 전송 날짜가 다른 경우는 해당 작업을 수행하지 않습니다.
### 3. Recap을 통해 텍스트 요약을 진행한 뒤 결과를 반환합니다.
Recap을 통해 각 메시지 별로 요약을 진행합니다. 요약을 완료하면 기존 메시지와 요약된 메시지를 같이 클라이언트로 전송합니다.