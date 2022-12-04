use Kookbob;

-- select restaurant_name, menu_name, count(*) from Kookbob.menu group by restaurant_name, menu_name having count(menu_name)>1; -- // 한개이상 존재하는 중복데이터 검색

set SQL_SAFE_updates= 0;
delete t1 from menu t1 join menu t2 on t1.restaurant_name = t2.restaurant_name and t1.menu_name = t2.menu_name where t1.menu_id>t2.menu_id;
set SQL_SAFE_updates= 1;
 