const request = require("request");

//db 연결
var mysql = require("mysql");
var db = mysql.createConnection({
    host: "127.0.0.1",
    user: "root",
    password: "12341234",
    database: "Kookbob",
    port: "3306",
});
db.connect();

//기본적으로 쓸 것들.
var menuAndPrice = ["메뉴", "가격"];
var S_DATE = "2022-01-01";
var E_DATE = new Date().toISOString().split("T")[0]; // 오늘로 설정

//데이터 뽑아낼때 쓸 객체
class menuData {
    constructor(restaurant, menuName, price, date) {
        this.restaurant = restaurant;
        this.menuName = menuName;
        this.price = price;
        this.date = date;
    }
}

var menuJsonObject;

const options = {
    uri: `https://kmucoop.kookmin.ac.kr/menu/menujson.php?sdate=${S_DATE}&edate=${E_DATE}`,
};
request(options, function (error, response, body) {
    if (error) {
        console.log(error);
    }
    menuJsonObject = JSON.parse(body);
});

//json 다 받아오고 실행.
setTimeout(() => {
    var day = new Date(S_DATE);
    for (var j = 0; j < 100; j++) {
        day.setDate(day.getDate() + 1);
        var goToSql = staffRest(
            menuJsonObject,
            day.toISOString().split("T")[0]
        );
        for (var i = 0; i < goToSql.length; i++) {
            updateSql(goToSql[i]);
        }
    }
}, 500);

async function updateSql(data) {
    if (data.menuName.includes("미운영") || data.menuName == "") {
        return;
    }
    await db.query(
        `SELECT count(*) existCheck FROM Kookbob.menu where restaurant_name = "${data.restaurant}" and menu_name ="${data.menuName}";`,
        function (error, results) {
            var existCheck = results[0].existCheck;
            if (existCheck == 0) {
                db.query(
                    `insert into menu ( restaurant_name, menu_name) values ('${data.restaurant}',"${data.menuName}");`
                );
            }
        }
    );
    await db.query(
        `SELECT count(*) appearCheck 
        FROM menu_appearance A join menu M on M.menu_id = A.menu_Id
        Where restaurant_name = '${data.restaurant}' and menu_name = "${data.menuName}" and date="${data.date}";`,
        function (error, results) {
            // var appearCheck = results[0].appearCheck;
            // if (appearCheck == 0) {
            // }
        }
    );
}

function staffRest(data, date) {
    var name = "교직원식당(복지관 1층)";
    var connerList = ["키친1", "키친2", "석식"];
    var returnArray = [];

    try {
        for (var i = 0; i < connerList.length; i++) {
            var dayMenuName;
            var dayMenuPrice;
            for (var j = 0; j < 2; j++) {
                var temp = data[name][date][connerList[i]][menuAndPrice[j]];
                if (
                    temp.includes("미운영") ||
                    temp.includes("※") ||
                    temp.includes("<") ||
                    temp.includes("[") ||
                    temp.includes("*")
                ) {
                    temp = "미운영";
                } else {
                    temp = temp.split("\r")[0];
                }

                if (j == 0) {
                    dayMenuName = temp;
                } else {
                    dayMenuPrice = temp;
                }
            }
            returnArray.push(
                new menuData("교직원식당", dayMenuName, dayMenuPrice, date)
            );
        }
    } catch (e) {}
    return returnArray;
}
