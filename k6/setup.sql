-- 1단계 부하 테스트 사전 초기화
-- 실행: docker exec -i akibaroom-mysql mysql -uakibaroom -pakibaroom akibaroom < k6/setup.sql
-- 충전은 각 시나리오 k6 setup() 함수에서 API로 처리

UPDATE money_account SET balance = 0;
UPDATE goods_stock SET remaining_quantity = total_quantity;
DELETE FROM money_ledger;
DELETE FROM purchase_order;
