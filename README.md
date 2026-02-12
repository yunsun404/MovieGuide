# MovieGuide
Personal preference-based movie recommendation site<br>

## 📌 주제 선정 이유
기존 OTT 서비스는 장르 기반 탐색 기능을 제공하지만, 다수의 장르를 조합하여 콘텐츠를 확인하는 기능은 제한적이다.
사용자는 원하는 장르를 개별적으로 선택해 확인해야 하며, 이 과정에서 탐색 시간이 증가하고 사용자 경험이 저하될 가능성이 존재한다.

본 프로젝트는 사용자가 선호 장르를 선택하면 해당 장르의 영화만을 통합적으로 추천하는 시스템을 구현하는 데 목적을 둔다. 이를 통해 개인 맞춤형 콘텐츠 탐색 경험을 제공하고자 한다. <br>

## ⚙️ 구현 방법

본 프로젝트는 TMDB(The Movie Database) API를 활용하여 영화 데이터를 수집하고, 이를 기반으로 사용자 선호 장르에 맞는 영화 목록을 제공하는 방식으로 구현하였다.

사용자는 회원가입 및 로그인 이후 선호 장르를 선택할 수 있으며, 선택된 장르는 데이터베이스에 저장된다. 이후 추천 페이지에서는 저장된 선호 장르 정보를 기반으로 해당 장르에 속하는 영화 데이터를 TMDB API를 통해 조회하여 사용자에게 제공한다.

프론트엔드와 백엔드는 REST 방식으로 연동되며, 서버 측에서는 서블릿을 통해 API 요청을 처리하고 JSON 형태의 데이터를 반환하도록 구성하였다.

## 🛠 기술 스택 및 개발 환경

- **Language**: Java, HTML, CSS, JavaScript  
- **Backend**: Java Servlet, JSP, JDBC  
- **Database**: MariaDB  
- **API**: TMDB Open API  
- **Development Environment**: Eclipse IDE, Apache Tomcat 9, Git, GitHub
- **Page Design** : Pigma


## 📌 데이터 흐름 구조 
- **Login**: 사용자 로그인  
- **Genre Selection**: 선호 장르 선택 후 DB 저장  
- **Recommendation Page**: 추천 페이지 접속  
- **User Preference Load**: 서버에서 사용자 선호 장르 조회  
- **External API Call**: TMDB API 호출  
- **Data Processing**: 장르에 해당하는 영화 목록 반환  
- **Rendering**: 프론트엔드에서 화면 출력

 
시현 영상: https://github.com/user-attachments/assets/44384590-b74f-4060-8ea9-ff1ad33aae67


