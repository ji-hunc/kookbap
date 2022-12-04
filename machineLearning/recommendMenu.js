const express = require('express');
const router = express.Router();

// db ����
var db = require("../dbConnector");


// recommendMenu.js ���� latentFactorMachineLearning.py�� �����Ͽ� ������� �޾ƿ� �����ϱ�
// child-process����� spawn ���
const spawn = require('child_process').spawn;

// spawn�� ���� "python latentFactorMachineLearning.py" ��ɾ� ����
const result = spawn('python3', ['/workspace/Kookbap_server/recommendMenu/latentFactorMachineLearning.py']);

// stdout�� 'data'�̺�Ʈ�����ʷ� �������� �޾� recommendMenuId�� String�������� ���� ���� recommendMenuIdArr�� recommendMenuId�� split�Ͽ� 2���� �迭�� ����
var recommendMenuId;
var recommendMenuIdArr = [];
result.stdout.on('data', function(data) {
	recommendMenuId = data.toString().substring(2, data.toString().length-3);
	recommendMenuIdArr = recommendMenuId.split("], [");
});

// ���� �߻� ��, stderr�� 'data'�̺�Ʈ�����ʷ� �������� �޴´�.
result.stderr.on('data', function(data) {
    console.log(data.toString());
});

// ���ѹα� �ð� ���ϴ� �ڵ��� ���� �ð��� �ѱ� �ð��� ���� �ʾ� ������
// 1. ���� �ð�(Locale)
const curr = new Date();
console.log("����ð�(Locale) : " + curr + '<br>');

// 2. UTC �ð� ���
const utc = 
	  curr.getTime() + 
	  (curr.getTimezoneOffset() * 60 * 1000);

// 3. UTC to KST (UTC + 9�ð�)
const KR_TIME_DIFF = 9 * 60 * 60 * 1000;  //�ѱ� �ð�(KST)�� UTC�ð����� 9�ð� �� �����Ƿ� 9�ð��� �и��� ������ ��ȯ.
const kr_curr = new Date(utc + (KR_TIME_DIFF));  //UTC �ð��� �ѱ� �ð����� ��ȯ�ϱ� ���� utc �и��� ���� 9�ð��� ����.

var year = kr_curr.getFullYear();
var month = ('0' + (kr_curr.getMonth() + 1)).slice(-2);
var day = ('0' + kr_curr.getDate()).slice(-2);
var dateString = year + '-' + month  + '-' + day;


// Response
router.get('/:userName', function(request, response) {
    try{
		// userName�� �޾ƿ� DB�� user ���̺��� userId�� ������
        db.query(`SELECT user_num FROM user WHERE user_id = '${request.params.userName}';`, function(error, results1){
			// userName�� kookbap�� ���Ե��� �ʴٸ� null���� ����
            if(results1[0]== undefined){
                response.json();
                return;
            }
            var userRecommendMenuArr = [];
			// ������ ������ ��õ �޴��� arr�� ����
            userRecommendMenuArr = (recommendMenuIdArr[results1[0]['user_num']].split(","));
			// ������ ��õ �޴� �� ���� 5���� �ҷ���
            db.query(`SELECT * FROM menu INNER JOIN menu_appearance ON menu_appearance.menu_id = menu.menu_id 
            WHERE date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[0])}'
            OR date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[1])}'
            OR date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[2])}'
            OR date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[3])}'
            OR date = '${dateString}' AND menu.menu_id='${(userRecommendMenuArr[4])}';`, function(error, results) {
                if (error){
                    console.log(error);
                }
				// ����� ����
				console.log(results);
                response.json(results);
            });
        });

    }
    catch(e){
        response.json("error");
    }
});

module.exports = router;

