# AndroidFineDust


프로젝트 명 : fineDust (미세먼지)
※ 본 프로젝트는 패스트 캠퍼스 안드로이드 강의를 참고하고 진행했습니다!

<img width="366" alt="스크린샷 2021-08-16 오후 1 29 17" src="https://user-images.githubusercontent.com/59818827/129527170-e194ce7e-ab4c-4008-8a66-a9ac70992064.png">

 
사용기술 : 
 
LocationManager(FusedLocationProviderClient)
 
Retrofit2
 
Coroutine 
 
App Widgets
 
공공 API
 
한국환경공단_에어코리아_측정소정보API

한국환경공단_에어코리아_대기오염정보API

Kakao Local API
 
 
API 호출 방식
 
 
 <img width="1189" alt="스크린샷 2021-08-16 오후 1 36 11" src="https://user-images.githubusercontent.com/59818827/129514312-7181dd90-4ea8-4acc-991a-7e67745bec1c.png">


 
프로젝트 설명:
 
사용자의 기기 의 GPS를 이용해서 현재 위치(경도,위도) 를 얻어서 Kakao Local API를 이용해서 TM좌표를 얻은 다음 
TM좌표를 이용해서 내 주변 측정소 정보를 얻은 다음 측정소 의 정보를 이용해서 대기오염 정보를 가지고 와서 앱 화면에 띄운다
앱 위젯을 지원해서 위젯을 통해 자신의 위치에서의 미세먼지 정보를 알 수 있다
 
프로젝트 아쉬운점:
 
안드로이드 11버전 이하 에서는 위젯을 통한 대기오염 정보가 보여지지 않는 이슈가 있다 

