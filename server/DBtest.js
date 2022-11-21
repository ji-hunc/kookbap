const request = require("request");

//db 연결
var mysql = require("mysql");
var db = mysql.createConnection({
    host: "13.209.133.64",
    user: "root",
    password: "1234",
    database: "Kookbob",
    port: "50609",
});
db.connect();

//기본적으로 쓸 것들.
var menuAndPrice = ["메뉴", "가격"];
var S_DATE = "2022-01-03"; //월요일
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

var prograssDate = Math.ceil(
    (new Date(E_DATE) - new Date(S_DATE)) / (1000 * 60 * 60 * 24)
); //2022- 01-01 부터 현재까지 일수 계싼

//json 다 받아오고 실행.
setTimeout(() => {
    var day = new Date(S_DATE);
    for (var j = 0; j < prograssDate; j++) {
        day.setDate(day.getDate() + 1); //청향의경우 7일(+7)로 설정
        var goToSql = staffRest(
            // 함수명 밑에있는걸로 바꿔가며 쓰면 됨
            menuJsonObject,
            day.toISOString().split("T")[0]
        );

        for (var i = 0; i < goToSql.length; i++) {
            updateSql(goToSql[i]);
        }
    }
}, 500);

//sql 서버에 탑제
async function updateSql(data) {
    try {
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
                console.log(data, error);
            }
        );
        await db.query(
            `SELECT count(*) appearCheck 
            FROM menu_appearance A join menu M on M.menu_id = A.menu_Id
            Where restaurant_name = '${data.restaurant}' and menu_name = "${data.menuName}" and date="${data.date}";`,
            function (error, results) {
                var appearCheck = results[0].appearCheck;
                if (appearCheck == 0) {
                    db.query(
                        `SELECT menu_id FROM Kookbob.menu
                        where menu_name = '${data.menuName}' and restaurant_name = "${data.restaurant}";`,
                        function (error, results) {
                            var menu_id = results[0].menu_id;
                            db.query(
                                `insert into menu_appearance (menu_id, date) values ("${menu_id}", "${data.date}");`,
                                function (error, results) {
                                    console.log(menu_id, data.date);
                                }
                            );
                        }
                    );
                }
            }
        );
    } catch (e) {
        console.log(data);
    }
}

// 교직원식당 json 재가공
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
                    temp.includes("*") ||
                    temp.includes('"')
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
// 한울 json 재가공
function hanwoolRest(data, date) {
    var name = "한울식당(법학관 지하1층)";
    var connerList = [
        "1코너<br>SNACK1",
        "1코너<br>SNACK2",
        "3코너<br>CUTLET",
        "4코너<br>RICE.Oven",
        "5코너<br>GUKBAP.Chef",
    ];
    var returnArray = [];

    try {
        for (var i = 0; i < connerList.length; i++) {
            var dayMenuName;
            var dayMenuPrice;
            for (var j = 0; j < 2; j++) {
                var temp = data[name][date][connerList[i]][menuAndPrice[j]];
                if (
                    temp.includes("운영") ||
                    temp.includes("※") ||
                    temp.includes("<") ||
                    temp.includes("*") ||
                    temp.includes('"') ||
                    temp.includes("~") ||
                    temp.includes("휴점") ||
                    temp.includes("휴 점") ||
                    temp.includes("더진국")
                ) {
                    temp = "";
                } else {
                    if (temp.split("\r\n")[0].includes("[")) {
                        temp = temp.split("\r\n")[1];
                    } else {
                        temp = temp.split("\r\n")[0];
                    }
                }

                if (j == 0) {
                    dayMenuName = temp;
                } else {
                    dayMenuPrice = temp;
                }
            }
            returnArray.push(
                new menuData("한울식당", dayMenuName, dayMenuPrice, date)
            );
        }
    } catch (e) {}
    return returnArray;
}
//학생식당 json 재가공
function studentRest(data, date) {
    var name = "학생식당(복지관 1층)";
    var connerList = [
        "착한아침",
        "가마<br>중식",
        "누들송(면)<br>중식",
        "누들송<br>(카페테리아)<br>중식",
        "인터쉐프<br>중식",
        "데일리밥<br>중식",
        "가마<br>석식",
        "인터쉐프<br>석식",
        "데일리밥<br>석식",
    ];
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
                    temp.includes('"')
                ) {
                    temp = "";
                } else {
                    if (temp.split("\r\n")[0].includes("[")) {
                        temp = temp.split("\r\n")[1];
                    } else {
                        temp = temp.split("\r\n")[0];
                    }
                    if (temp.includes("NEW")) {
                        temp = temp.replace("NEW", "");
                    }
                }

                if (j == 0) {
                    dayMenuName = temp;
                } else {
                    dayMenuPrice = temp;
                }
            }
            aa = new menuData("학생식당", dayMenuName, dayMenuPrice, date);
            returnArray.push(aa);
        }
    } catch (e) {}
    return returnArray;
}

//청향 json 재가공
function chunghyangKoRest(data, date) {
    var name = "청향 한식당(법학관 5층)";
    var connerList = [
        "메뉴1",
        "메뉴2",
        "메뉴3",
        "메뉴4",
        "메뉴5",
        "메뉴6",
        "메뉴7",
        "메뉴8",
    ];
    var returnArray = [];

    try {
        for (var i = 0; i < connerList.length; i++) {
            var dayMenuName;
            var dayMenuPrice;
            for (var j = 0; j < 2; j++) {
                var temp = data[name][date][connerList[i]][menuAndPrice[j]];
                temp = temp.replace("\r", "");
                temp = temp.replace("\n", " ");
                temp = temp.replace(" ", "");
                if (
                    temp.includes("운영") ||
                    temp.includes("※") ||
                    temp.includes("<") ||
                    temp.includes('"')
                ) {
                    temp = "";
                }

                if (j == 0) {
                    dayMenuName = temp;
                } else {
                    dayMenuPrice = temp;
                }
            }
            aa = new menuData("청향 한식당", dayMenuName, dayMenuPrice, date);
            if (aa.menuName != "") {
                console.log(aa);
            }
            returnArray.push(aa);
        }
    } catch (e) {}
    return returnArray;
}
