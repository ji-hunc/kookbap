const express = require("express");
const request = require("request");
var db = require("../dbConnector");
var router = express.Router();

router.get("/", (req, res) => {
    res.send("menu");
    // res.json(menuJsonObject);
});

// 날짜별 메뉴 데이터 뿌려줌
router.get("/:date", (req, res) => {
    var userId = req.query.userId;
    db.query(
        `SELECT m.menu_id as menu_id, restaurant_name, menu_name, count_review, star_avg, total_like, date, price, \
		subMenu, menu_like_id, image, Mliked_user_id, if (Mliked_user_id = '${userId}', true, false) as userLikeTrueFalse \
		FROM (menu m INNER JOIN (select menu_id, date, price, subMenu from menu_appearance where date = '${req.params.date}') a ON m.menu_id = a.menu_id) \
		left join (select * from menu_like where Mliked_user_id= '${userId}') l on m.menu_id = l.Mliked_menu_id \
		left join (SELECT review_menu_id_reviewd, image FROM Kookbob.review R where (R.review_menu_id_reviewd, R.write_date) in (SELECT review_menu_id_reviewd, max(write_date) \
		FROM Kookbob.review group by review_menu_id_reviewd)) i on m.menu_id = i.review_menu_id_reviewd;
		;`,
        function (error, results) {
            if (error) {
                console.log(error);
            }
            res.json(results);
            // console.log(results);
        }
    );
});

// const options = {
//     	uri: "https://kmucoop.kookmin.ac.kr/menu/menujson.php?sdate=2022-11-01&edate=2022-11-30"
// 	};
// request(options, function(error, response, body) {
//     if (error) {
//         console.log(error);
//     }
//     menuJsonObject = JSON.parse(body);

// 	hanulMenus = menuJsonObject['한울식당(법학관 지하1층)'];
// 	for (var i=21; i < 22; i++) {
// 		var date = "2022-11-" + i;

// 		menuEachDate = hanulMenus[date];
// 		cafeteriaName = Object.keys(menuEachDate);

// 		for (var j=0; j<cafeteriaName.length; j++) {
// 			var menu = menuEachDate[cafeteriaName[j]]["메뉴"];
// 			var price = menuEachDate[cafeteriaName[j]]["가격"];
// 			if (!(menu.length == 0) && !(price == "")) {
// 				price = price.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");

// 				menu = menu.split("\r\n");

// 				console.log(menu[1], price);
// 			}
// 		}
// 	}
// });

module.exports = router;
