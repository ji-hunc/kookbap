# 서버 테스트시 주의사항

-   **반드시 모듈들은 `npm i`로 받을 것**.
-   **모듈 다운로드 후 생성되는 `node_modules` 폴더는 절대 업로드 하지 말 것. `.gitignore` 참고**
-   **이미지 폴더는 직접생성해야함, 최상위 폴더에 public/images로 생성. 이 폴더도 깃에 업로드 X. `.gitignore` 참고**
-   실제 서버에서 돌아가는 주소는 할당받은 API 주소임
-   로컬에서 테스트 할 때는 `http://localhost:3000/` 로 접속할 것
-   안드로이드와 테스트 할 때는 RetrofitClient.java 파일에 `BASE_URL` 을 `http://10.0.2.2:3000/` 으로 할 것

---

# Modules

| Module        | Version |
| ------------- | ------- |
| node-schedule | 2.1.0   |

---

# FILES

-   ./
    | 파일명 | 설명 |
    | -------------- | --------------------- |
    | main.js | 기본 실행 파일 |
    | dbConnector.js | db연결 |
    | updateDb.js | 메뉴 데이터 업데이트. |

-   ./routes

    | 파일명    | 설명                   |
    | --------- | ---------------------- |
    | index.js  | ?                      |
    | menu.js   | 메뉴 데이터 전송       |
    | rank.js   | 각 항목별 랭킹 전송    |
    | review.js | 리뷰 데이터 전송       |
    | user.js   | 유저 정보 저장 및 전송 |
