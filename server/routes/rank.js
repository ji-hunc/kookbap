const express = require("express");
var router = express.Router();
var db = require("../dbConnector");

router.get("/", function (request, response) {
    response.send("랭킹입니다.");
});

// 메뉴에 관한 랭킹 가져옴. section :{menu, review} category:{menu:[star_avg,total_like,count_review], review :[review_like,total_review]}
router.get("/:section/:category", function (request, response) {
    var querySentence; //쿼리 문
    var section = request.params.section;
    var category = request.params.category;
    var limitRange = request.query.endR;
    var userId = request.query.userId;
    //menu 관련 rank
    if (section == "menu") {
        querySentence = `SELECT m.menu_id as menu_id, restaurant_name, menu_name, count_review, star_avg, total_like,  price, 
        menu_like_id, image, Mliked_user_id, if (Mliked_user_id = '${userId}', true, false) as userLikeTrueFalse 
       FROM (menu m INNER JOIN (select max(price)as price, menu_id  from menu_appearance group by menu_id) a ON m.menu_id = a.menu_id) 
       left join (select * from menu_like where Mliked_user_id= '${userId}') l on m.menu_id = l.Mliked_menu_id 
       left join (SELECT review_menu_id_reviewd, image FROM Kookbob.review R where (R.review_menu_id_reviewd, R.write_date) in (SELECT review_menu_id_reviewd, max(write_date) 
       FROM Kookbob.review group by review_menu_id_reviewd)) i on m.menu_id = i.review_menu_id_reviewd order by ${category} desc limit ${limitRange};`;
    }
    //review 관련 rank
    else if (section == "review") {
        // 하트 많이받은 리뷰 -안쓰는 중-
        if (category == "review_like") {
            querySentence = `SELECT review_number, review_user_id, menu_name, write_date, review_like, description,image FROM Kookbob.review order by review_like desc limit ${limitRange};`;
        }
        //리뷰 많은 리뷰어
        else if (category == "total_review") {
            querySentence = `select review_user_id as user_id, nickname, total from Kookbob.user U join (select review_user_id, count(*) as total from Kookbob.review group by review_user_id order by total desc limit ${limitRange}) R on U.user_id = R.review_user_id order by total desc`;
        }
    }
    // json 형식으로 전달.
    db.query(querySentence, function (error, results) {
        response.json(results);
        console.log(results);
    });
});

//export
module.exports = router;
