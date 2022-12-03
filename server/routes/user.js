const express = require("express");
const request = require("request");
var db = require("../dbConnector");
var router = express.Router();

router.post("/like", (req, res) => {
    var user_id = req.body.user_id;
    var card_id = req.body.card_id;
    // var pushOrNot = req.body.pushOrNot; // 1이면 하트 채운거, 0이면 하트 비운거
    var type = req.body.type;
    console.log(req.body);

    if (type == "menu") {
        // menu 좋아요 버튼을 눌렀을 때
        db.query(
            `select count(*) as existCheck from menu_like where Mliked_user_id = '${user_id}' and Mliked_menu_id ='${card_id}'`,
            function (error, results) {
                var existCheck = results[0].existCheck;
                console.log(results);
                if (existCheck == 0) {
                    console.log("insert");
                    db.query(
                        `INSERT INTO menu_like (Mliked_user_id, Mliked_menu_id) VALUES ('${user_id}','${card_id}');`,
                        function (error, result) {
                            if (error) {
                                console.log(error);
                            }
                        }
                    );
                } else if (existCheck == 1) {
                    console.log("delete");
                    db.query(
                        `Delete from menu_like where Mliked_user_id = '${user_id}' and Mliked_menu_id ='${card_id}'`,
                        function (error, result) {
                            if (error) {
                                console.log(error);
                            }
                        }
                    );
                } else if (existCheck > 1) {
                    console.log("else");
                    db.query("set SQL_SAFE_updates= 0;");
                    db.query(
                        "delete t1 from menu_like t1 join menu_like t2 on t1.Mliked_user_id = t2.Mliked_user_id and t1.Mliked_menu_id = t2.Mliked_menu_id where t1.menu_like_id>t2.menu_like_id;"
                    );
                    db.query("set SQL_SAFE_updates= 1;");
                    console.log("중복제거 완료");
                }
                db.query(
                    `SELECT count(*) as total FROM Kookbob.menu_like where Mliked_menu_id = '${card_id}';`,
                    function (error, results) {
                        var updateTotal = results[0]["total"];
                        db.query(
                            `update menu set total_like = "${updateTotal}" where menu_Id ="${card_id}";`
                        );
                    }
                );
            }
        );
    } else {
        // review 좋아요 버튼을 눌렀을 때
        var review_id = req.body.card_id;
        db.query(
            `select count(*) as existCheck from review_like where Rlike_user_id = '${user_id}' and Rlike_review_no ='${review_id}'`,
            function (error, results) {
                var existCheck = results[0].existCheck;
                console.log(results);
                if (existCheck == 0) {
                    console.log("insert");
                    db.query(
                        `INSERT INTO review_like (Rlike_user_id, Rlike_review_no) VALUES ('${user_id}','${review_id}');`,
                        function (error, result) {
                            if (error) {
                                console.log(error);
                            }
                        }
                    );
                } else if (existCheck == 1) {
                    console.log("delete");
                    db.query(
                        `Delete from review_like where Rlike_user_id = '${user_id}' and Rlike_review_no ='${review_id}'`,
                        function (error, result) {
                            if (error) {
                                console.log(error);
                            }
                        }
                    );
                } else if (existCheck > 1) {
                    console.log("else");
                    db.query("set SQL_SAFE_updates= 0;");
                    db.query(
                        "delete t1 from review_like t1 join review_like t2 on t1.Rlike_user_id = t2.Rlike_user_id and t1.Rlike_review_no = t2.Rlike_review_no where t1.review_like_id>t2.review_like_id;"
                    );
                    db.query("set SQL_SAFE_updates= 1;");
                    console.log("중복제거 완료");
                }
                db.query(
                    `SELECT count(*) as total FROM Kookbob.review_like where Rlike_review_no = '${review_id}';`,
                    function (error, results) {
                        var updateTotal = results[0]["total"];
                        db.query(
                            `update review set review_like = "${updateTotal}" where review_number ="${review_id}";`
                        );
                    }
                );
            }
        );
    }

    res.json({
        success: true,
        message: "GOOD",
    });
});

module.exports = router;
