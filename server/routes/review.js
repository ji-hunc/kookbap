const express = require("express");
var db = require("../dbConnector");
var router = express.Router();

var bodyParser = require("body-parser");
var parser = bodyParser.urlencoded({ extended: false });

const multer = require("multer");
const upload = multer({ dest: "images/" }); //dest : 저장 위치
var fs = require("fs");

// 메뉴 이름으로 리뷰들 조회. 정렬기준도 있음
router.get("/:menuName", function (request, response) {
    //request에서 받아온 query로 어떻게 정렬할지 지정.
    var orderBy = request.query.orderBy;
    var userId = request.query.userId;
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
        //todo : 이거 menuName이 아니라 menu id 여야함.
        `SELECT *, if (Rlike_user_id = "${userId}", true,false) as reviewLikeTrueFalse , (select nickname from user where review_user_id = user_id) as nickname \
        FROM Kookbob.review R left join (select * from review_like where Rlike_user_id = "${userId}")L on \
        R.review_number = L.Rlike_review_no where menu_name = "${request.params.menuName}" order by ${orderBy};`,
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

    // 대한민국 시간 구하는 코드들....
    // 1. 현재 시간(Locale)
    const curr = new Date();
    console.log("현재시간(Locale) : " + curr + "<br>"); // 현재시간(Locale) : Tue May 31 2022 09:00:30

    // 2. UTC 시간 계산
    const utc = curr.getTime() + curr.getTimezoneOffset() * 60 * 1000;

    // 3. UTC to KST (UTC + 9시간)
    const KR_TIME_DIFF = 9 * 60 * 60 * 1000; //한국 시간(KST)은 UTC시간보다 9시간 더 빠르므로 9시간을 밀리초 단위로 변환.
    const kr_curr = new Date(utc + KR_TIME_DIFF); //UTC 시간을 한국 시간으로 변환하기 위해 utc 밀리초 값에 9시간을 더함.
    // console.log("한국시간 : " + kr_curr); // 한국시간 : Tue May 31 2022 09:00:30 GMT+0900 (한국 표준시)

    var year = kr_curr.getFullYear();
    var month = ("0" + (kr_curr.getMonth() + 1)).slice(-2);
    var day = ("0" + kr_curr.getDate()).slice(-2);
    var dateString = year + "-" + month + "-" + day;
    var hours = ("0" + kr_curr.getHours()).slice(-2);
    var minutes = ("0" + kr_curr.getMinutes()).slice(-2);
    var seconds = ("0" + kr_curr.getSeconds()).slice(-2);
    var timeString = hours + ":" + minutes + ":" + seconds;
    const resultTime = dateString + " " + timeString;
    console.log(new Date());
    console.log(resultTime);

    // 이미지 이름 변경하는 부분
    request.files.image.name = `${request.body.menuName}_${request.body.reviewUserId}_${resultTime}.png`;
    const { image } = request.files;
    image.mv(__dirname + "/../public/images/" + image.name);
    var imageUrl = request.files.image.name;

    const menuId = request.body.menuId;
    if (menuId == 0) {
        // GENERAL_WRITE시에는 menu_id를 서버에서 찾아야함
        // TODO menuId 찾아오는 부분
        // insert 쿼리까지 코드 중복해서 써야할 듯, 동기 비동기 때문에
        db.query(
            `SELECT menu_Id FROM menu WHERE menu_name = '${request.body.menuName}';`,
            function (error, result) {
                const menuId2 = result[0]["menu_Id"];

                // 리뷰 내용 DB에 INSERT
                db.query(`INSERT INTO review (review_user_id, review_menu_id_reviewd, menu_name, write_date, star, review_like, description, image) 
			VALUES (
				'${request.body.reviewUserId}', 
				'${menuId2}', 
				'${request.body.menuName}', 
				'${resultTime}',
				'${request.body.star}', 
				'${request.body.reviewLike}', 
				'${request.body.description}', 
				'${imageUrl}'
			);`);

                // 메뉴 테이블에 review_count을 1 올리고, 평점 평균을 새로 반영하여 계산후 바꿔줌
                db.query(
                    `SELECT count_review, star_avg FROM menu WHERE menu_id = ${menuId2}`,
                    function (error, results) {
                        if (error) {
                            console.log(error);
                        }
                        var count_review = results[0]["count_review"];
                        var star_avg = results[0]["star_avg"];

                        if (count_review == null) {
                            count_review = 0;
                        }
                        if (star_avg == null) {
                            star_avg = 0;
                        }

                        star = request.body.star;
                        star *= 1; // string을 number로 바꾸기 위해
                        console.log(
                            (star_avg * count_review + star) /
                                (count_review + 1)
                        );
                        db.query(
                            `UPDATE menu SET count_review = ${
                                count_review + 1
                            }, star_avg = ${
                                (star_avg * count_review + star) /
                                (count_review + 1)
                            } WHERE menu_id = ${menuId2}`,
                            function (error, results3) {
                                if (error) {
                                    console.log(error);
                                }
                            }
                        );
                    }
                );

                // 결과값 전송 일단은 무조건 GOOD
                response.json({
                    success: true,
                    message: "GOOD",
                });
            }
        );
    } else {
        // 리뷰 내용 DB에 INSERT
        db.query(`INSERT INTO review (review_user_id, review_menu_id_reviewd, menu_name, write_date, star, review_like, description, image) 
		VALUES (
			'${request.body.reviewUserId}', 
			'${menuId}', 
			'${request.body.menuName}', 
			'${resultTime}',
			'${request.body.star}', 
			'${request.body.reviewLike}', 
			'${request.body.description}', 
			'${imageUrl}'
		);`);

        // 메뉴 테이블에 review_count을 1 올리고, 평점 평균을 새로 반영하여 계산후 바꿔줌
        db.query(
            `SELECT count_review, star_avg FROM menu WHERE menu_id = ${menuId}`,
            function (error, results) {
                if (error) {
                    console.log(error);
                }
                var count_review = results[0]["count_review"];
                var star_avg = results[0]["star_avg"];

                if (count_review == null) {
                    count_review = 0;
                }
                if (star_avg == null) {
                    star_avg = 0;
                }

                star = request.body.star;
                star *= 1; // string을 number로 바꾸기 위해
                console.log(
                    (star_avg * count_review + star) / (count_review + 1)
                );
                db.query(
                    `UPDATE menu SET count_review = ${
                        count_review + 1
                    }, star_avg = ${
                        (star_avg * count_review + star) / (count_review + 1)
                    } WHERE menu_id = ${menuId}`,
                    function (error, results3) {
                        if (error) {
                            console.log(error);
                        }
                    }
                );
            }
        );

        // 결과값 전송 일단은 무조건 GOOD
        response.json({
            success: true,
            message: "GOOD",
        });
    }
});

// 리뷰 수정
router.post("/modify", parser, function (request, response) {
    console.log(request.body);
    var oldStar;
    var recentStar = request.body.star;
    var menuId = request.body.menuId;
    // string을 Number로 만들기 위해
    menuId *= 1;
    recentStar *= 1;
    db.query(
        `SELECT star FROM review WHERE review_number = '${request.body.reviewNumber}';`,
        function (error, result) {
            if (error) {
                console.log(error);
            }
            oldStar = result[0]["star"];
            oldStar *= 1;
        }
    );

    if (request.body.isUploadNewImage == "false") {
        // 이미지를 수정하지 않은 경우
        db.query(
            `UPDATE review SET star = '${recentStar}', description = '${request.body.description}' WHERE review_number = '${request.body.reviewNumber}';`
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

    // 별점이 바뀌었으면 menu 테이블의 평균 별점에도 반영
    if (oldStar != recentStar) {
        db.query(
            `SELECT count_review, star_avg FROM menu WHERE menu_id = '${menuId}';`,
            function (error, results) {
                if (error) {
                    console.log(error);
                }
                var count_review = results[0]["count_review"];
                var star_avg = results[0]["star_avg"];
                db.query(
                    `UPDATE menu SET star_avg = ${
                        (star_avg * count_review - oldStar + recentStar) /
                        count_review
                    } WHERE menu_id = ${menuId};`,
                    function (error, result) {
                        if (error) {
                            console.log(error);
                        }
                    }
                );
            }
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

    // review table에서 삭제하기 전에 menu table에서 star_avg와 review_count 변경
    var menuId = request.body.menuId;
    var star = request.body.star;
    db.query(
        `SELECT count_review, star_avg FROM menu WHERE menu_id = '${menuId}';`,
        function (error, results) {
            if (error) {
                console.log(error);
            }
            var count_review = results[0]["count_review"];
            var star_avg = results[0]["star_avg"];
            var future_star =
                (star_avg * count_review - star) / (count_review - 1);
            if (count_review == 1) {
                // 0으로 나눠지는 것을 막기 위해
                future_star = 0;
            }
            db.query(
                `UPDATE menu SET star_avg = ${future_star}, count_review = ${
                    count_review - 1
                } WHERE menu_id = ${menuId};`,
                function (error, results) {
                    if (error) {
                        console.log(error);
                    }
                }
            );
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
