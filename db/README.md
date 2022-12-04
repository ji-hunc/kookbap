# DB INFO

`  MySQL`  
`  version : 5.7.40 -0ubuntu0.18.04.1`

# DB구조

![dbStructure](https://user-images.githubusercontent.com/95959567/205449117-cd07eb22-1993-450a-afe2-8d1d95678ab1.png)

-   관계형 DB를 사용
-   메뉴가 삭제되면 리뷰,좋아요 전체 날라감
-   유저가 탈퇴해도 리뷰는 남아있게 설정
-   ## user
    -   user_id : 유저 고유 아이디 `PK`
    -   nickname : 유저 닉네임
    -   writing_count : 쓴 글 갯수(안쓰는 필드)
    -   user_num : 머신러닝 돌릴때 쓸 유저코드
    -   E-mail : 회원 가입 시 이메일
-   ## menu
    -   menu_Id : 메뉴 고유 아이디 `PK`
    -   restaurant_name : 식당 이름 `FK` `CASECADE`
    -   menu_name : 메뉴 이름
    -   count_review : 리뷰 개수. (로딩 시 연산 시간 줄이기위해 추가)
    -   star_avg : 별점 평균. (로딩 시 연산 시간 줄이기위해 추가)
    -   total_like : 총 좋아요. (로딩 시 연산 시간 줄이기위해 추가)
-   ## menu_appearance : 메뉴 등장 데이터.
    -   menu_appearance_id : 메뉴 등장 고유번호 `PK`
    -   menu_Id : 메뉴 Id `FK` `CASECADE`
    -   date : 나온 날짜
    -   price : 가격
    -   subMenu : 반찬
-   ## restaurant
    -   name : 식당 이름 `PK`
-   ## review
    -   review_number : 리뷰 고유번호 `PK`
    -   review_user_id : 리뷰쓴 유저 아이디 `FK` `SETNULL`
    -   review_menu_id_reviewd : 리뷰 메뉴 아이디 `FK` `CASECADE`
    -   menu_name : 메뉴이름
    -   write_date : 쓴 날자
    -   star : 유저가 준 평점
    -   review_like : 리뷰 좋아요 개수. (로딩 시 연산시간 줄이기위해 추가)
    -   description : 리뷰 내용
    -   image : 사진 저장 경로
-   ## review_like
    -   review_like_id : 리뷰좋아요 고유번호 `PK`
    -   Rlike_user_Id : 좋아요 누른 유저 ID `FK` `CASECADE`
    -   Rlike_review_no : 좋아요 눌린 리뷰 ID `FK` `CASECADE`
-   ## menu_like
    -   menu_like_id : 메뉴좋아요 고유번호 `PK`
    -   Mliked_user_id : 좋아요 누른 유저 ID `FK` `CASECADE`
    -   Mliked_menu_id : 좋아요 눌린 메뉴 ID `FK` `CASECADE`

---

# FILES

db 생성 및 기타 sql문

-   ## dbStructure.sql
    -   db구조 생성 파일
-   ## DeleteAllMenu.sql
    -   menu, menu_appearance 테이블 데이터 전체삭제
-   ## menuDeduplication.sql
    -   menu 테이블 데이터 중복 제거
