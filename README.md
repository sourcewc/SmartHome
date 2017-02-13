# SmartHome
android & artik

아틱 코드는 아두이노로 작성했습니다.

Node.js 서버를 이용하여 run 폴더에 있는 아두이노 바이너리 파일을 실행합니다.
아틱 클라우드로 메세지 전송시에 send.py를 실행시켜 메세지를 보냅니다.
아틱 클라우드 설정은 config 파일에 있습니다.

Android 코드는 네이버 음성인식 API를통하여 음성에서 나온 정보를 토대로 REST PUT 형태로 OKhttp 를 통해 통신을 실시하여 ARTIK을 제어합니다.
마찬가지로 ARTIKCLOUD를 통해 온습도 센서의 평균치를 확인할수있는 그래프가있습니다.

Layout구성은 ViewPager에 Fragment탭을 붙인형태로 구성되었습니다.
이미지 출처는 flaticon.com 입니다.
