const express = require('express');
const request = require('request');
const app = express()
var router = express.Router();

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
    // db.query(`SELECT * FROM review`, function(error, results) {
    //     console.log(results);
    //     response.json(results);
    // })
});


router.post('/post', function(request, response) {
    console.log(request.body);
    // console.log(request);
    // 앱에서 보낸 데이터.
    // [Object: null prototype] {
    //     reviewUserId: 'jihun',
    //     menuName: 'menu name',
    //     writeDate: '2022-11-13 00:01:02',
    //     star: '0.0',
    //     reviewLike: '0',
    //     description: 'delicious',
    //     image: 'ImageFile'
    //     restaurant: '한울식당'
    //   }

    // 실제 DB에 채워 넣어야할 데이터
    // review_number 은 auto_increase, review_menu_id_reviewd(서버에서 메뉴테이블에 해당 메뉴가 있는지 확인 후 없으면 추가, 있으면 그 번호)
    // DateTime 가공
    // image 처리
    console.log("Enter!!!!!");
    // console.log(request.body);
    // console.log(request.body.menuName);

    // db.query(`SELECT * FROM menu`)
    db.query(`SELECT EXISTS (SELECT menu_name FROM menu WHERE menu_name='${request.body.menuName}' limit 1) as success;`, function(error, results) {
        // results의 형식: [ RowDataPacket { success: 1 } ]
        if (results[0].success == 0) {
            // 클라이언트로부터 받아온 menu_name 데이터가 menu 테이블에 없음.
            // 신규 메뉴로 menu 테이블에 등록해야함
            // console.log("no menuname in menu");
            // console.log(results[0]);
            db.query(`INSERT INTO menu (restaurant_name, menu_name, count_review, star_avg, total_like)
            VALUES ('${request.body.restaurantName}', '${request.body.menuName}', 0, 0, 0);`)

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
            const review_menu_id_reviewd = results2[0].menu_id;

            db.query(`INSERT INTO review (review_user_id, review_menu_id_reviewd, menu_name, write_date, star, review_like, description, image)
            VALUES ('${request.body.reviewUserId}', '${review_menu_id_reviewd}', '${request.body.menuName}', '${new Date().toISOString().slice(0, 19).replace('T', ' ')}', '${request.body.star}', '${request.body.reviewLike}', '${request.body.description}', 'image');`)
        })
        
    })

    

    response.json(
        {
        "success": true,
        "message": "GOOD"
        }
    )
})

module.exports = router;