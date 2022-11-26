const express = require("express");
const request = require("request");
const app = express();
var router = express.Router();

var bodyParser = require("body-parser");
var parser = bodyParser.urlencoded({ extended: false });

const multer = require("multer");
const upload = multer({ dest: "images/" }); //dest : 저장 위치
var fs = require("fs");

var mysql = require("mysql");
var db = mysql.createConnection({
    host: "127.0.0.1",
    user: "root",
    password: "wlgns620",
    database: "Kookbob",
    port: "3306",
});
db.connect();

// 메뉴 이름으로 리뷰들 조회. 정렬기준도 있음
router.get("/:menuName", function (request, response) {
    //request에서 받아온 query로 어떻게 정렬할지 지정.
    var orderBy = request.query.orderBy;
    switch (orderBy) {
        case "최신순":
            orderBy = "write_date desc";
            break;
        case "높은 평점순":
            orderBy = "star desc";
            break;
        case "낮은 평점순":
            orderBy = "star asc";
            break;
    }

    db.query(
        `SELECT * FROM review WHERE menu_name = '${request.params.menuName}' order by ${orderBy} ;`,
        function (error, results) {
            console.log(request.params.menuName);
            response.json(results);
            console.log(results);
        }
    );
});

// user가 쓴 모든 리뷰들을 조회
router.get("/users/:userName", function (request, response) {
    db.query(
        `SELECT * FROM review WHERE review_user_id = '${request.params.userName}'`,
        function (error, results) {
            console.log(request.params.menuName);
            response.json(results);
            console.log(results);
        }
    );
});

router.get("/", function (request, response) {
    response.send("GOOD");
});

// 리뷰 작성
router.post("/post", parser, function (request, response) {
    console.log("Enter!!!!!");
    console.log("@@@@@@@@@@@@@@@@@@@@");
    console.log(request.body);
    console.log("@@@@@@@@@@@@@@@@@@@@");
    console.log(request.files);
    console.log("@@@@@@@@@@@@@@@@@@@@");

    db.query(
        `SELECT menu_id FROM menu WHERE menu_name = '${request.body.menuName}';`,
        function (error, results) {
            if (error) {
                console.log(error);
            }
            // review_menu_id_reviewd 받아오는 부분
            const review_menu_id_reviewd = results[0].menu_id;

            // 이미지 이름 변경하는 부분
            request.files.image.name = `${request.body.menuName}_${
                request.body.reviewUserId
            }_${new Date()
                .toISOString()
                .slice(0, 19)
                .replace("T", " ")}.png`;
            const { image } = request.files;
            image.mv(__dirname + "/../public/images/" + image.name);
            var imageUrl = request.files.image.name;

            // 리뷰 내용 DB에 INSERT
            db.query(`INSERT INTO review (review_user_id, review_menu_id_reviewd, menu_name, write_date, star, review_like, description, image) 
            VALUES (
                '${request.body.reviewUserId}', 
                '${review_menu_id_reviewd}', 
                '${request.body.menuName}', 
                '${new Date().toISOString().slice(0, 19).replace("T", " ")}', 
                '${request.body.star}', 
                '${request.body.reviewLike}', 
                '${request.body.description}', 
                '${imageUrl}'
            );`);

            // 메뉴 테이블에 review_count을 1올리고, 평점 평균을 새로 반영하여 계산후 바꿔줌
            // db.query(
            //     `UPDATE review SET star = '${request.body.star}', description = '${request.body.description}' WHERE review_number = '${request.body.reviewNumber}';`
            // );
            db.query(`SELECT count_review, star_avg FROM menu WHERE menu_id = ${review_menu_id_reviewd}`, function(error, results2) {
                if (error) {
                    console.log(error);
                }
                var count_review = results2[0]['count_review'];
                var star_avg = results2[0]['star_avg'];

                if (count_review == null) {count_review = 0}
                if (star_avg == null) {star_avg = 0}

                star = request.body.star;
                star *= 1; // string을 number로 바꾸기 위해
                console.log(((star_avg * count_review) + star) / (count_review + 1));
                db.query(`UPDATE menu SET count_review = ${count_review + 1}, star_avg = ${((star_avg * count_review) + star) / (count_review + 1)} WHERE menu_id = ${review_menu_id_reviewd}`, function(error, results3) {
                    if (error) {
                        console.log(error);
                    }
                })
            })
            
        }
    );

    // 결과값 전송 일단은 무조건 GOOD
    response.json({
        success: true,
        message: "GOOD",
    });
});

// 리뷰 수정
router.post("/modify", parser, function (request, response) {
    console.log(request.body);

    if (request.body.isUploadNewImage == "false") {
        // 이미지를 수정하지 않은 경우
        db.query(
            `UPDATE review SET star = '${request.body.star}', description = '${request.body.description}' WHERE review_number = '${request.body.reviewNumber}';`
        );
        console.log("modified");
    } else {
        // 이미지를 수정한 경우
        // 기존의 이미지 삭제
        db.query(
            `SELECT image FROM review WHERE review_number = '${request.body.reviewNumber}';`,
            function (error, result) {
                if (error) {
                    console.log(error);
                }
                file_name = result[0].image;
                console.log("public/images/" + file_name);

                try {
                    fs.unlinkSync("public/images/" + file_name);
                    console.log("image delete!");
                } catch (error) {
                    console.log(error);
                }
            }
        );

        // 새로운 이미지 파일이름 변경 후, 다시 저장 및 review table에 Image link 업데이트
        request.files.image.name = `${request.body.menuName}_${
            request.body.reviewUserId
        }_${new Date().toISOString().slice(0, 19).replace("T", " ")}.png`;
        const { image } = request.files;
        image.mv(__dirname + "/../public/images/" + image.name);
        var imageUrl = request.files.image.name;
        db.query(
            `UPDATE review SET star = '${request.body.star}', description = '${request.body.description}', image = '${imageUrl}' WHERE review_number = '${request.body.reviewNumber}';`
        );
    }

    // 결과값 전송 일단은 무조건 GOOD
    response.json({
        success: true,
        message: "GOOD",
    });
});

// 리뷰 삭제
router.post("/delete", function (request, response) {
    console.log(request.body);
    // reviewNumber로 지울 리뷰의 사진파일 이름을 구하고, 삭제
    db.query(
        `SELECT image FROM review WHERE review_number = '${request.body.reviewNumber}';`,
        function (error, result) {
            if (error) {
                console.log(error);
            }
            file_name = result[0].image;
            console.log("public/images/" + file_name);

            try {
                fs.unlinkSync("public/images/" + file_name);
                console.log("image delete!");
            } catch (error) {
                console.log(error);
            }
        }
    );
    // 사진파일은 위에서 지웠고, review table에서 해당 Row 삭제
    db.query(
        `DELETE FROM review WHERE review_number = '${request.body.reviewNumber}';`
    );

    // 결과값 전송 일단은 무조건 GOOD
    response.json({
        success: true,
        message: "GOOD",
    });
});

module.exports = router;
