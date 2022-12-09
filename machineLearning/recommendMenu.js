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
result.stdout.on('data', function (data) {
	recommendMenuId = data.toString().substring(2, data.toString().length - 3);
	recommendMenuIdArr = recommendMenuId.split("], [");
});

// ���� �߻� ��, stderr�� 'data'�̺�Ʈ�����ʷ� �������� �޴´�.
result.stderr.on('data', function (data) {
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
var dateString = year + '-' + month + '-' + day;
var DATE = 4;


// Response
router.get('/:userName', function (request, response) {
	try {
		// userName�� �޾ƿ� DB�� user ���̺��� userId�� ������
		db.query(`SELECT user_num, user_id FROM user WHERE user_id = '${request.params.userName}';`, function (error, results1) {
			// userName�� kookbap�� ���Ե��� �ʴٸ� null���� ����
			if (results1[0] == undefined) {
				response.json();
				return;
			}
			var userId = results1[0]['user_id'];
			// �ð� ���ΰ�ħ
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
			var dateString = year + '-' + month + '-' + day;


			// �Ϸ翡 �� ���� latentFactorMachineLearning.py�� ������Ͽ� ������Ʈ����
			if (DATE != day) {
				DATE = day;
				// recommendMenu.js ���� latentFactorMachineLearning.py�� �����Ͽ� ������� �޾ƿ� �����ϱ�
				// child-process����� spawn ���
				const spawn = require('child_process').spawn;

				// spawn�� ���� "python latentFactorMachineLearning.py" ��ɾ� ����
				const result = spawn('python3', ['/workspace/Kookbap_server/recommendMenu/latentFactorMachineLearning.py']);

				// stdout�� 'data'�̺�Ʈ�����ʷ� �������� �޾� recommendMenuId�� String�������� ���� ���� recommendMenuIdArr�� recommendMenuId�� split�Ͽ� 2���� �迭�� ����
				result.stdout.on('data', function (data) {
					recommendMenuId = data.toString().substring(2, data.toString().length - 3);
					recommendMenuIdArr = recommendMenuId.split("], [");
				});

				// ���� �߻� ��, stderr�� 'data'�̺�Ʈ�����ʷ� �������� �޴´�.
				result.stderr.on('data', function (data) {
					console.log(data.toString());
				});
			}
			var userRecommendMenuArr = [];

			// ������ ������ ��õ �޴��� arr�� ����
			if (recommendMenuIdArr[Number(results1[0]['user_num'])] != undefined) {
				userRecommendMenuArr = (recommendMenuIdArr[Number(results1[0]['user_num'])].split(","));
			}
			/**/
			// ������ ��õ �޴� �� ���� 5���� �ҷ���
			db.query(`SELECT m.menu_id as menu_id, restaurant_name, menu_name, count_review, star_avg, total_like, date, price, \
		subMenu, menu_like_id, image, Mliked_user_id, if (Mliked_user_id = '${userId}', true, false) as userLikeTrueFalse \
		FROM (menu m INNER JOIN (select menu_id, date, price, subMenu from menu_appearance where date = '${dateString}') a ON m.menu_id = a.menu_id) \
		left join (select * from menu_like where Mliked_user_id= '${userId}') l on m.menu_id = l.Mliked_menu_id \
		left join (SELECT review_menu_id_reviewd, image FROM Kookbob.review R where (R.review_menu_id_reviewd, R.write_date) in (SELECT review_menu_id_reviewd, max(write_date) \
		FROM Kookbob.review group by review_menu_id_reviewd)) i on m.menu_id = i.review_menu_id_reviewd WHERE date = '${dateString}' AND m.menu_id='${(userRecommendMenuArr[0])}'
            OR date = '${dateString}' AND m.menu_id='${(userRecommendMenuArr[1])}'
            OR date = '${dateString}' AND m.menu_id='${(userRecommendMenuArr[2])}'
            OR date = '${dateString}' AND m.menu_id='${(userRecommendMenuArr[3])}'
            OR date = '${dateString}' AND m.menu_id='${(userRecommendMenuArr[4])}';`, function (error, results2) {
				if (error) {
					console.log(error);
				}
				// ����� ����
				console.log(results2);
				response.json(results2);
			});
		});

	}
	catch (e) {
		response.json("error");
	}
});

module.exports = router;

