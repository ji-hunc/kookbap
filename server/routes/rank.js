const express = require("express");
var router = express.Router();

var mysql = require("mysql");
var db = mysql.createConnection({
    host: "127.0.0.1",
    user: "root",
    password: "12341234",
    database: "Kookbob",
    port: "3306",
});
db.connect();

router.get("/", function (request, response) {
    response.send("랭킹입니다.");
});

// 메뉴에 관한 랭킹 가져옴. section :{menu, review} category:{menu:[star_avg,total_like], review :[review_like,total_review]}
router.get("/:section/:category", function (request, response) {
    var querySentence; //쿼리 문
    var section = request.params.section;
    var category = request.params.category;
    console.log(request.body);
    //menu 관련 rank
    if (section == "menu") {
        querySentence = `select menu_id, restaurant_name, menu_name, star_avg, total_like from Kookbob.menu order by ${category} desc limit ${request.query.endR};`;
    }
    //review 관련 rank
    else if (section == "review") {
        // 하트 많이받은 리뷰
        if (category == "review_like") {
            querySentence = `SELECT review_number, review_user_id, menu_name, write_date, review_like, description,image FROM Kookbob.review order by review_like desc limit ${request.query.endR};`;
        }
        //리뷰 많은 리뷰어
        else if (category == "total_review") {
            querySentence = `select review_user_id as user_id, nickname, total from Kookbob.user U join (select review_user_id, count(*) as total from Kookbob.review group by review_user_id order by total desc limit ${request.query.endR}) R on U.user_id = R.review_user_id order by total desc`;
        }
    }

    db.query(querySentence, function (error, results) {
        response.json(results);
        console.log(results);
    });
});

//export
module.exports = router;
