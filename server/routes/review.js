const express = require('express');
const request = require('request');
const app = express()
var router = express.Router();

var bodyParser = require('body-parser');
var parser = bodyParser.urlencoded({extended: false});

const multer = require('multer');
const upload = multer({dest: 'images/'}); //dest : 저장 위치


var mysql = require('mysql');
var db = mysql.createConnection({
  host     : '127.0.0.1',
  user     : 'root',
  password : 'wlgns620',
  database : 'Kookbob',
  port     : '3306'
});
db.connect();


router.get('/:menuName', function(request, response) {
    db.query(`SELECT * FROM review WHERE menu_name = '${request.params.menuName}'`, function(error, results) {
        console.log(request.params.menuName);
        response.json(results);
        console.log(results);
    })
});


router.get('/', function(request, response) {
    response.send("GOOD");
    //   response.send(`<img src="/images/qq_jihun_2022-11-14 03:29:02.png" style="width:300px; display:block; margin:10px;">`);
});


router.post('/post', parser, function(request, response) {
    console.log(request.body);
    console.log("@@@@@@@@@@@@@@@@@@@@");
    console.log(request.files);
    console.log("Enter!!!!!");

    db.query(`SELECT EXISTS (SELECT menu_name FROM menu WHERE menu_name='${request.body.menuName}' limit 1) as success;`, function(error, results) {
        // results의 형식: [ RowDataPacket { success: 1 } ]
        if (results[0].success == 0) {
            // 클라이언트로부터 받아온 menu_name 데이터가 menu 테이블에 없음.
            // 신규 메뉴로 menu 테이블에 등록해야함
            // console.log("no menuname in menu");
            // console.log(results[0]);
            db.query(`INSERT INTO menu (restaurant_name, menu_name, count_review, star_avg, total_like)
            VALUES ('${request.body.restaurantName}', '${request.body.menuName}', 0, 0, 0);`);

            // 클라이언트로부터 받아온 menu_name 데이터가 menu 테이블에 있음. 
            // console.log("already exist");
            // console.log(results);
        }
        // 클라리언트로부터 받아온 리뷰내용 review 테이블에 post하는 부분
        // console.log(results[0]);
        db.query(`SELECT menu_id FROM menu WHERE menu_name = '${request.body.menuName}';`, function(error, results2) {
            if (error) {
                console.log(error);
            }
            // review_menu_id_reviewd 받아오는 부분
            const review_menu_id_reviewd = results2[0].menu_id;

            // 이미지 이름 변경하는 부분
            request.files.image.name = `${request.body.menuName}_${request.body.reviewUserId}_${new Date().toISOString().slice(0, 19).replace('T', ' ')}.png`;
            const { image } = request.files;
            image.mv(__dirname + '/../images/' + image.name);
            var imageUrl = request.files.image.name;

            // DB에 INSERT
            db.query(`INSERT INTO review (review_user_id, review_menu_id_reviewd, menu_name, write_date, star, review_like, description, image)
            VALUES ('${request.body.reviewUserId}', '${review_menu_id_reviewd}', '${request.body.menuName}', '${new Date().toISOString().slice(0, 19).replace('T', ' ')}', '${request.body.star}', '${request.body.reviewLike}', '${request.body.description}', '${imageUrl}');`)
        });
    })

    // 결과값 전송
    response.json(
        {
        "success": true,
        "message": "GOOD"
        }
    )
})

module.exports = router;