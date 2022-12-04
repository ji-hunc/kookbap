# 국밥

국민대학교 학식정보와 이에 대한 리뷰정보를 확인하고, 내가 좋아할 만한 메뉴를 추천받을 수 있는 서비스
<br>
<br>

⚠️ `클라이언트, 서버, 디비, 머신러닝에 관한 readme는 각각의 폴더에 있습니다. `⚠️
| 클라이언트 | 서버 | DB | 머신러닝 |
| :-------: | :------: | :------: | :------: |
| [README.md](https://github.com/ji-hunc/kookbap/tree/main/kookbap) | [README.md](https://github.com/ji-hunc/kookbap/tree/main/server) | [README.md](https://github.com/ji-hunc/kookbap/tree/main/db) | [README.md](https://github.com/ji-hunc/kookbap/tree/main/machineLearning) |

</br>

## 👩🏻‍💻 팀원

| 최지훈 👑 |  김민제  |  노종빈  |  유민석  |  조현민  |
| :-------: | :------: | :------: | :------: | :------: |
| 20191683  | 20191557 | 20180891 | 20191629 | 20191672 |

</br>

## 🎥 시연

| 메뉴 및 리뷰 조회                                                                                                                |                                                            리뷰 작성                                                            | 리뷰 수정 및 삭제                                                                                                                |
| -------------------------------------------------------------------------------------------------------------------------------- | :-----------------------------------------------------------------------------------------------------------------------------: | -------------------------------------------------------------------------------------------------------------------------------- |
| <img src = "https://user-images.githubusercontent.com/52407470/205451046-d8e8c297-a9bb-4eaa-b02b-6eca8237f048.gif" width ="250"> | <img src ="https://user-images.githubusercontent.com/52407470/205479915-a6c603bb-c386-49be-a800-062d1855bd4f.gif" width ="250"> | <img src = "https://user-images.githubusercontent.com/52407470/205480970-d5e462fd-1035-41de-88c9-e374ae5e5cc6.gif" width ="250"> |

</br>

## 🛠 개발 환경

#### 📦 기술스택

<img width="125" src="https://img.shields.io/badge/android%20API-31-brightgreen"> <img width="145" src="https://img.shields.io/badge/android%20SDK-12.0-green"> <img width="116" src="https://img.shields.io/badge/node-18.12.0-yellow"> <img width="113" src="https://img.shields.io/badge/mysql-8.0.31-blue">

#### 구조

<img width="800" src="https://user-images.githubusercontent.com/52407470/205481295-f81d9acd-0b2e-41d0-ab2a-cb1001eb0075.png">

</br>

## 🔥 사용방법

#### 바로 사용

-   앱 실행 후, 국민대 이메일 (@kookmin.ac.kr)로 회원가입 및 로그인
-   현재 원격 서버가 호스팅되고 있어, 안드로이드 앱만 설치후 바로 사용 가능.

#### 로컬 테스트

1. 소스파일 다운로드
2. `npm`, `mysql` 설치
3. mysql 실행 후`CREATE DATABASE Kookbob` 입력
4. `server` 디렉터리의 `dbConnector.js` 파일에서 개인 mysql 비밀번호 입력
5. `kookbap/java/URLConnector.java` 파일에서 원격주소 주석처리 및 로컬주소 활성화
6. `server` 디렉터리에서 `npm install` 로 실행에 필요한 패키지 다운로드
7. `server` 디렉터리에서 `npm run server` 로 서버 실행
8. 안드로이드 앱 실행 및 사용

</br>

#### 🗂 디렉터리 구조

```bash
├── db
├── machineLearning
├── kookbap
│   ├── java
│   │   ├── MainActivity.java
│   │   ├── LoginAndSignup
│   │   ├── Notification
│   │   ├── Retrofits
│   │   ├── ReviewRank
│   │   ├── cafeteriaFragments
│   ├── res
│   │   ├── layout
│   │   ├── menu
│   │   ├── drawable
├── server
│   ├── main.js
│   ├── routes

```

</br>

## 🔀 Git Flow

기능별 개발 및 충돌 예방을 위해 `Git WorkFlow`를 적용
<img width="600" src="https://user-images.githubusercontent.com/52407470/205478151-992575f7-3018-473a-83fc-f75dc43495f8.png">

실제 사용한 것은 아래의 3개

-   `main` : 유의미한 단위의 개발이 끝나서 사용이 가능한 상태 일 때 develop에서 main으로 psuh
-   `develop` : 가장 중심이 되는 브랜치이고, 이 곳에서 feature 브랜치가 나가고 들어옴.
-   `feature` : develop에서 나온 브랜치이며, 특정 기능을 개발 후 develop 으로 merge 브랜치 네이밍은 `feature/기능이름`으로 정함
    </br>
