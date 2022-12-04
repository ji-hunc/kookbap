const express = require('express');
const router = express.Router();

// db 연결
var db = require("../dbConnector");


// recommendMenu.js 에서 latentFactorMachineLearning.py를 실행하여 결과값을 받아와 저장하기
// child-process모듈의 spawn 취득
const spawn = require('child_process').spawn;

// spawn을 통해 "python latentFactorMachineLearning.py" 명령어 실행
const result = spawn('python3', ['/workspace/Kookbap_server/recommendMenu/latentFactorMachineLearning.py']);

// stdout의 'data'이벤트리스너로 실행결과를 받아 recommendMenuId에 String형식으로 저장 이후 recommendMenuIdArr에 recommendMenuId를 split하여 2차원 배열로 저장
var recommendMenuId;
var recommendMenuIdArr = [];
result.stdout.on('data', function(data) {
	recommendMenuId = data.toString().substring(2, data.toString().length-3);
	recommendMenuIdArr = recommendMenuId.split("], [");
});

// 에러 발생 시, stderr의 'data'이벤트리스너로 실행결과를 받는다.
result.stderr.on('data', function(data) {
    console.log(data.toString());
});

// 대한민국 시간 구하는 코드들로 서버 시간이 한국 시간과 맞지 않아 구현함
// 1. 현재 시간(Locale)
const curr = new Date();
console.log("현재시간(Locale) : " + curr + '<br>');

// 2. UTC 시간 계산
const utc = 
	  curr.getTime() + 
	  (curr.getTimezoneOffset() * 60 * 1000);

// 3. UTC to KST (UTC + 9시간)
const KR_TIME_DIFF = 9 * 60 * 60 * 1000;  //한국 시간(KST)은 UTC시간보다 9시간 더 빠르므로 9시간을 밀리초 단위로 변환.
const kr_curr = new Date(utc + (KR_TIME_DIFF));  //UTC 시간을 한국 시간으로 변환하기 위해 utc 밀리초 값에 9시간을 더함.

var year = kr_curr.getFullYear();
var month = ('0' + (kr_curr.getMonth() + 1)).slice(-2);
var day = ('0' + kr_curr.getDate()).slice(-2);
var dateString = year + '-' + month  + '-' + day;


// Response
router.get('/:userName', function(request, response) {
    try{
		// userName을 받아와 DB의 user 테이블에서 userId를 가져옴
        db.query(`SELECT user_num FROM user WHERE user_id = '${request.params.userName}';`, function(error, results1){
			// userName이 kookbap에 가입되지 않다면 null값을 전송
            if(results1[0]== undefined){
                response.json();
                return;
            }
            var userRecommendMenuArr = [];
			// 유저의 오늘의 추천 메뉴를 arr로 저장
            userRecommendMenuArr = (recommendMenuIdArr[results1[0]['user_num']].split(","));
			// 오늘의 추천 메뉴 중 상위 5개만 불러옴
            db.query(`SELECT * FROM menu INNER JOIN menu_appearance ON menu_appearance.menu_id = menu.menu_id 
            WHERE date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[0])}'
            OR date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[1])}'
            OR date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[2])}'
            OR date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[3])}'
            OR date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[4])}';`, function(error, results) {
                if (error){
                    console.log(error);
                }
				// 결과값 전송
				console.log(results);
                response.json(results);
            });
        });

    }
    catch(e){
        response.json("error");
    }
});

module.exports = router;

