
# 우리동네 미세먼지
*※ 본 프로젝트는 패스트 캠퍼스 안드로이드 강의를 참고하고 진행했습니다!*

# Architecture
<img width="800"  alt="스크린샷 2021-08-16 오후 1 29 17" src="https://user-images.githubusercontent.com/59818827/206915768-5653de80-9b25-4f20-b9c5-4683ba897145.png">
*** 1번 -> 2번 -> 3번 순 호출<br>
 
## 사용기술 : 
 
1. LocationManager(FusedLocationProviderClient)
 
2. Retrofit2
 
3. Coroutine 

4. OkHttp
 
5. App Widgets
 
6. 공공 API
 
+ 한국환경공단_에어코리아_측정소정보API

+ 한국환경공단_에어코리아_대기오염정보API

7. Kakao Local API
 
 
## API 호출 방식
 
 
 <img width="700" alt="스크린샷 2021-08-16 오후 1 36 11" src="https://user-images.githubusercontent.com/59818827/129514312-7181dd90-4ea8-4acc-991a-7e67745bec1c.png">

---

 
## 프로젝트 설명:
 
사용자의 기기 의 GPS를 이용해서 현재 위치(**경도**,**위도**) 를 얻어서 Kakao Local API를 이용해서 TM좌표를 얻은 다음 
TM좌표를 이용해서 내 주변 측정소 정보를 획득한 후 가까운 측정소에서 대기오염 정보를 가지고 와서 앱 화면에 띄운다

 
 ---<br>
 ## 상세 기능:
 
 
<img src="https://user-images.githubusercontent.com/59818827/197322896-16537e46-6f6f-46be-bc67-349f89dfb279.jpg" width="366">

대기 환경에 따라서 좋음,보통,나쁨,매우 나쁨으로 나뉘어 지고 거기에 따라서 배경색 과 이모지가 달라진다<br>
|대기 환경|배경색|이모지|
|:---:|:---:|:---:|
|**좋음**|파랑색|☺| 
|**보통**|녹색|🙂|
|**나쁨**|노랑색|☹| 
|**매우 나쁨**|빨강색|😡| <br>

대기환경 정보는 한시간 간격 데이터를 받아서 유저에게 보여준다(ex 00시,01시,02시~24시)
