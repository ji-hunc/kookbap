/*
메뉴 db 업데이드 코드.

*/

const request = require("request");
var db = require("./dbConnector"); //db 연결

//날짜 설정
//db데이터 처음부터 다시 받아오고 싶으면 이부분 수정하면 됨.

var PROGRASS_DATE = 7;
var today = new Date();
var daysAgo = new Date();
daysAgo.setDate(today.getDate() + PROGRASS_DATE); //오늘부터 7일 뒤
var S_DATE = today.toISOString().split("T")[0]; //데이터 탑제 시작 날짜 (update 함수를 돌리는 날짜. 일요일) //"2022-01-03"
var E_DATE = daysAgo.toISOString().split("T")[0]; // 오늘부터 7일 뒤로 설정 "2020-02-02" 형식으로 저장

//가공한 데이터를 class에 담아 관리
class menuData {
    constructor(restaurant, menuName = "", price = 0, date, subMenu = "") {
        this.restaurant = restaurant;
        this.menuName = menuName;
        this.price = price;
        this.date = date;
        this.subMenu = subMenu;
    }
}

var menuAndPrice = ["메뉴", "가격"]; //json에 있는 메뉴/가격

//국민대 학식 api 가져오기.
var menuJsonObject;
const options = {
    uri: `https://kmucoop.kookmin.ac.kr/menu/menujson.php?sdate=${S_DATE}&edate=${E_DATE}`,
};

//학식 api에 요청
request(options, async function (error, response, body) {
    if (error) {
        console.log(error);
    }
    menuJsonObject = JSON.parse(body);
    // console.log(menuJsonObject);
    // console.log("json loading 완료");
    totalUpdate();
    deleteOverlap();
    // console.log("완료");
});

//2022- 01-01 부터 현재까지 일수 계산, 전체업데이트 시 사용
// var prograssDate = Math.ceil(
//     (new Date(E_DATE) - new Date(S_DATE)) / (1000 * 60 * 60 * 24)
// );

// 업데이트 메서드
function totalUpdate() {
    var day = new Date(S_DATE);
    for (var j = 0; j < PROGRASS_DATE + 1; j++) {
        //교직원 식당 등록
        var goToSqlstaff = staffRest(
            menuJsonObject,
            day.toISOString().split("T")[0]
        );
        for (var i = 0; i < goToSqlstaff.length; i++) {
            updateSql(goToSqlstaff[i]);
        }

        //한울식당 등록
        var goToSqlhanwool = hanwoolRest(
            menuJsonObject,
            day.toISOString().split("T")[0]
        );
        for (var i = 0; i < goToSqlhanwool.length; i++) {
            updateSql(goToSqlhanwool[i]);
        }
        //학생식당 등록
        var goToSqlstudent = studentRest(
            menuJsonObject,
            day.toISOString().split("T")[0]
        );
        for (var i = 0; i < goToSqlstudent.length; i++) {
            updateSql(goToSqlstudent[i]);
        }
        //청향 등록
        var goToSqlchunghyangKo = chunghyangKoRest(
            menuJsonObject,
            day.toISOString().split("T")[0]
        );
        for (var i = 0; i < goToSqlchunghyangKo.length; i++) {
            updateSql(goToSqlchunghyangKo[i]);
        }

        //청향 양식당 등록
        var goToSqlchunghyangWest = chunghyangWestRest(
            menuJsonObject,
            day.toISOString().split("T")[0]
        );
        for (var i = 0; i < goToSqlchunghyangWest.length; i++) {
            updateSql(goToSqlchunghyangWest[i]);
        }

        //K-bob등록 2022-07-01 부터 하는걸 추천 7.1이후 리모댈링 및 개업 데이터가 있음.
        var goToSqlKbobRest = KbobRest(
            menuJsonObject,
            day.toISOString().split("T")[0]
        );
        for (var i = 0; i < goToSqlKbobRest.length; i++) {
            updateSql(goToSqlKbobRest[i]);
        }

        day.setDate(day.getDate() + 1);
    }
}

//data를 넣으면 db에 업로드
async function updateSql(data) {
    try {
        if (data.menuName.includes("미운영") || data.menuName == "") {
            return;
        }
        if (data.price == "") {
            data.price = 0;
        }
        db.query(
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
        db.query(
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
                                `insert into menu_appearance (menu_id, date, price, subMenu) values ("${menu_id}", "${data.date}", "${data.price}" , "${data.subMenu}");`,
                                function (error, result) {
                                    // console.log(data.date, data.menuName);
                                    if (error) {
                                        console.log(error);
                                    }
                                }
                            );
                        }
                    );
                }
            }
        );
    } catch (e) {
        // console.log(data);
    }
}

// 교직원식당 json 재가공
function staffRest(data, date) {
    var name = "교직원식당(복지관 1층)";
    var connerList = ["키친1", "키친2", "석식"];
    var returnArray = [];
    try {
        for (var i = 0; i < connerList.length; i++) {
            var tempSplit =
                data[name][date][connerList[i]][menuAndPrice[0]].split("\r\n");
            var dayMenuName = "";
            var daySubMenu = "";
            var dayMenuPrice = data[name][date][connerList[i]][
                menuAndPrice[1]
            ].replace("★", "");
            //필요없는 데이터 삭제
            for (var j = tempSplit.length - 1; j >= 0; j--) {
                if (
                    tempSplit[j].includes("운영") ||
                    tempSplit[j].includes("※") ||
                    tempSplit[j].includes("<") ||
                    tempSplit[j].includes("*") ||
                    tempSplit[j].includes('"') ||
                    tempSplit[j].includes("~") ||
                    tempSplit[j].includes("휴점") ||
                    tempSplit[j].includes("휴 점") ||
                    tempSplit[j].includes("휴일") ||
                    tempSplit[j].includes("[") ||
                    tempSplit[j].includes("주말") ||
                    tempSplit[j].includes("없음") ||
                    tempSplit[j].includes("연휴") ||
                    tempSplit[j].includes("식당") ||
                    tempSplit[j].includes("개천절") ||
                    tempSplit[j].includes("평일") ||
                    tempSplit[j].includes("광복절") ||
                    tempSplit[j].includes("현충일") ||
                    tempSplit[j].includes("선거일") ||
                    tempSplit[j].includes("데일리밥") ||
                    tempSplit[j].includes("까지") ||
                    tempSplit[j] == ""
                ) {
                    tempSplit.splice(j, 1);
                }
            }
            dayMenuName = tempSplit.shift();
            daySubMenu = tempSplit.join();
            aa = new menuData(
                "교직원식당",
                dayMenuName,
                dayMenuPrice,
                date,
                daySubMenu
            );
            if (aa.menuName != "") {
                // console.log(aa);
                returnArray.push(aa);
            }
        }
    } catch (e) {
        // console.log("데이터분류 완료");
    }
    return returnArray;
}

// 한울 json 재가공
function hanwoolRest(data, date) {
    var name = "한울식당(법학관 지하1층)";
    // 코너이름이 바뀔 가능성을 위해??
    // var connerList = Object.keys(data);
    var connerList = [
        "1코너<br>SNACK2",
        "2코너<BR>NOODLE",
        "3코너<br>CUTLET",
        "4코너<br>RICE.Oven",
        "5코너<br>GUKBAP.Chef",
    ];
    var returnArray = [];

    try {
        for (var i = 0; i < connerList.length; i++) {
            var tempSplit =
                data[name][date][connerList[i]][menuAndPrice[0]].split("\r\n");
            var dayMenuName = "";
            var daySubMenu = "";
            var dayMenuPrice = data[name][date][connerList[i]][menuAndPrice[1]];
            //필요없는 데이터 삭제
            for (var j = tempSplit.length - 1; j >= 0; j--) {
                if (
                    tempSplit[j].includes("운영") ||
                    tempSplit[j].includes("※") ||
                    tempSplit[j].includes("<") ||
                    tempSplit[j].includes("*") ||
                    tempSplit[j].includes('"') ||
                    tempSplit[j].includes("~") ||
                    tempSplit[j].includes("휴점") ||
                    tempSplit[j].includes("휴 점") ||
                    tempSplit[j].includes("더진국") ||
                    tempSplit[j].includes("휴일") ||
                    tempSplit[j].includes("[") ||
                    tempSplit[j].includes("주말") ||
                    tempSplit[j].includes("없음") ||
                    tempSplit[j].includes("연휴") ||
                    tempSplit[j].includes("식당") ||
                    tempSplit[j].includes("개천절") ||
                    tempSplit[j].includes("평일") ||
                    tempSplit[j].includes("광복절") ||
                    tempSplit[j].includes("현충일") ||
                    tempSplit[j].includes("선거일") ||
                    tempSplit[j] == ""
                ) {
                    tempSplit.splice(j, 1);
                }
            }
            dayMenuName = tempSplit[0];
            // console.log(tempSplit);

            if (
                (tempSplit.length >= 2 && tempSplit[1].includes("￦")) ||
                (tempSplit.length > 2 && tempSplit[2].includes("￦"))
            ) {
                for (var j = 0; j < tempSplit.length; j++) {
                    if (!tempSplit[j].includes("￦")) {
                        dayMenuName = tempSplit[j];
                        continue;
                    } else if (tempSplit[j][0] == "￦") {
                        dayMenuPrice = tempSplit[j].replace("￦", "");
                    } else {
                        dayMenuName = tempSplit[j].split("￦")[0];
                        dayMenuPrice = tempSplit[j].split("￦")[1];
                    }
                    aa = new menuData(
                        "한울식당",
                        dayMenuName,
                        dayMenuPrice,
                        date,
                        daySubMenu.trim()
                    );
                    if (aa.menuName != "") {
                        // console.log(aa);
                        returnArray.push(aa);
                    }
                }
            } else {
                for (var j = 1; j < tempSplit.length; j++) {
                    if (tempSplit[j][0] == "&") {
                        daySubMenu += tempSplit[j].replace("&", " ");
                    } else {
                        dayMenuName += tempSplit[j];
                    }
                }
                aa = new menuData(
                    "한울식당",
                    dayMenuName,
                    dayMenuPrice,
                    date,
                    daySubMenu.trim()
                );
                if (aa.menuName != "") {
                    // console.log(aa);
                    returnArray.push(aa);
                }
            }
        }
    } catch (e) {
        console.log("데이터분류 완료");
    }
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
            var tempSplit =
                data[name][date][connerList[i]][menuAndPrice[0]].split("\r\n");
            var dayMenuName = "";
            var daySubMenu = "";
            var dayMenuPrice = data[name][date][connerList[i]][
                menuAndPrice[1]
            ].replace("★", "");
            //필요없는 데이터 삭제
            for (var j = tempSplit.length - 1; j >= 0; j--) {
                if (
                    tempSplit[j].includes("운영") ||
                    tempSplit[j].includes("※") ||
                    tempSplit[j].includes("<") ||
                    tempSplit[j].includes('"') ||
                    tempSplit[j].includes("~") ||
                    tempSplit[j].includes("휴점") ||
                    tempSplit[j].includes("휴 점") ||
                    tempSplit[j].includes("휴일") ||
                    tempSplit[j].includes("[") ||
                    tempSplit[j].includes("주말") ||
                    tempSplit[j].includes("없음") ||
                    tempSplit[j].includes("연휴") ||
                    tempSplit[j].includes("식당") ||
                    tempSplit[j].includes("개천절") ||
                    tempSplit[j].includes("평일") ||
                    tempSplit[j].includes("광복절") ||
                    tempSplit[j].includes("현충일") ||
                    tempSplit[j].includes("선거일") ||
                    tempSplit[j].includes("중식") ||
                    tempSplit[j].includes("석식") ||
                    tempSplit[j].includes("조식") ||
                    tempSplit[j].includes("참고") ||
                    tempSplit[j].includes("차이웨이") ||
                    tempSplit[j] == ""
                ) {
                    tempSplit.splice(j, 1);
                }
            }
            dayMenuName = tempSplit.shift();
            daySubMenu = tempSplit.join(" ");
            try {
                if (dayMenuName != undefined && dayMenuName.includes("NEW")) {
                    dayMenuName = dayMenuName.replace("NEW", "").trim();
                }
                if (dayMenuName != undefined && dayMenuName.includes("★")) {
                    dayMenuName = dayMenuName.replace("★", "").trim();
                }
                if (dayMenuName != undefined && dayMenuName.includes("&")) {
                    daySubMenu = daySubMenu + " " + dayMenuName.split("&")[1];
                    dayMenuName = dayMenuName.split("&")[0];
                }
                if (daySubMenu != undefined) {
                    daySubMenu = daySubMenu.replace("&", "");
                }
            } catch (e) {
                // console.log(dayMenuName, daySubMenu);
                console.log(e);
            }

            aa = new menuData(
                "학생식당",
                dayMenuName,
                dayMenuPrice,
                date,
                daySubMenu
            );
            if (aa.menuName != "") {
                // console.log(aa);
                returnArray.push(aa);
            }
        }
    } catch (e) {
        console.log(e);
    }
    return returnArray;
}

//청향 json 재가공  sideMenu 없음
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
                // console.log(aa);
                returnArray.push(aa);
            }
        }
    } catch (e) {}
    return returnArray;
}

//청향 jsonWes 재가공  sideMenu 없음
function chunghyangWestRest(data, date) {
    var name = "청향 양식당(법학관 5층)";
    var connerList = ["PASTA", "RISOTTO", "STEAK"];
    var returnArray = [];

    try {
        for (var i = 0; i < connerList.length; i++) {
            var dayMenuName;
            var dayMenuPrice;

            var temp =
                data[name][date][connerList[i]][menuAndPrice[0]].split("\r\n");
            // console.log(temp);
            // temp.split("\r\n");
            // console.log(temp);
            // console.log({temp});

            for (var j = 0; j < temp.length; j += 2) {
                dayMenuName = temp[j].trim();
                dayMenuPrice = temp[j + 1].replace(",", "").replace("원", "");
                if (
                    dayMenuName.includes("콜키지") ||
                    dayMenuName.includes("미운영")
                ) {
                    continue;
                }
                if (dayMenuName.includes(" ")) {
                    var sep = dayMenuName.split(" ");
                    dayMenuName = sep[0];
                    dayMenuPrice = sep[1].replace(",", "").replace("원", "");
                    j -= 1;
                }
                aa = new menuData(
                    "청향 양식당",
                    dayMenuName,
                    dayMenuPrice,
                    date
                );
                if (aa.menuName != "") {
                    // console.log(aa);
                    returnArray.push(aa);
                }
            }
        }
    } catch (e) {}
    return returnArray;
}

//K-Bob jsonWes 재가공 sideMenu 없음
function KbobRest(data, date) {
    var name = "K-Bob<sup>+</sup>";
    var connerList = ["간편도시락", "김밥", "분식"];
    var returnArray = [];

    try {
        for (var i = 0; i < connerList.length; i++) {
            var dayMenuName;
            var dayMenuPrice;

            var temp =
                data[name][date][connerList[i]][menuAndPrice[0]].split("\r\n");
            temp = temp.filter(Boolean);

            for (var j = 0; j < temp.length / 2; j++) {
                dayMenuName = temp[2 * j];
                dayMenuPrice = temp[2 * j + 1]
                    .replace("￦", "")
                    .replace(",", "");

                if (
                    dayMenuName.includes("￦") ||
                    dayMenuName.includes("없음")
                ) {
                    continue;
                }
                aa = new menuData("K-BOB+", dayMenuName, dayMenuPrice, date);
                if (aa.menuName != "") {
                    // console.log(aa);
                    returnArray.push(aa);
                }
            }
        }
    } catch (e) {}
    return returnArray;
}

// 생활관식당 json 재가공 //정기식은 데이터에서 빼기로 (학식개념이 아님)
function DormitoryRest(data, date) {
    var name = "생활관식당 정기식(생활관 A동 1층)";
    var connerList = ["조식", "중식", "석식"];
    var returnArray = [];

    try {
        for (var i = 0; i < connerList.length; i++) {
            var dayMenuName;
            var dayMenuPrice;

            var temp =
                data[name][date][connerList[i]][menuAndPrice[0]].split("\r\n");
            var dayMenuName = temp[0];
            var dayMenuPrice = "정기식 신청자 한정";

            if (dayMenuName != "") {
                aa = new menuData(
                    "생활관식당 정기식",
                    dayMenuName,
                    dayMenuPrice,
                    date
                );
                // console.log(aa);
                returnArray.push(aa);
            }
        }
    } catch (e) {}
    return returnArray;
}

// 데이터 중복 제거
async function deleteOverlap() {
    db.query("set SQL_SAFE_updates= 0;");
    db.query(
        "delete t1 from menu t1 join menu t2 on t1.restaurant_name = t2.restaurant_name and t1.menu_name = t2.menu_name where t1.menu_id>t2.menu_id;"
    );
    db.query("set SQL_SAFE_updates= 1;");
    console.log("중복제거 완료");
}

module.exports = totalUpdate;
