-- 0단계 시드 데이터 (생성 API 없음 — member 시드 시 money_account 반드시 함께 생성)
-- 데모 시나리오: 충전 → 한정 굿즈 선착순 구매

INSERT INTO member (id, created_at)
VALUES (UUID_TO_BIN('019ebc6d-2cb7-759d-854f-2728b598628e'), UTC_TIMESTAMP(6)),
       (UUID_TO_BIN('019ebc6d-2cb7-7943-891f-493d485e00de'), UTC_TIMESTAMP(6));

INSERT INTO money_account (id, member_id, balance, created_at, updated_at)
VALUES (UUID_TO_BIN('019ebc6d-2cb7-7d23-8a58-12fc9e8b95d1'),
        UUID_TO_BIN('019ebc6d-2cb7-759d-854f-2728b598628e'), 0, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
       (UUID_TO_BIN('019ebc6d-2cb7-7277-a43e-69f334929770'),
        UUID_TO_BIN('019ebc6d-2cb7-7943-891f-493d485e00de'), 0, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6));

INSERT INTO goods_stock (id, name, price, total_quantity, remaining_quantity, created_at, updated_at)
VALUES (UUID_TO_BIN('019ebc6d-2cb7-740f-b7fa-1acf0863cbcf'), '한정판 아크릴 스탠드', 15000, 100, 100, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
       (UUID_TO_BIN('019ebc6d-2cb7-70c4-9267-e481277d374f'), '드롭 한정 키링', 8000, 50, 50, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
       (UUID_TO_BIN('019ebc6d-2cb7-7082-98cc-4ede65e85a39'), '콜라보 포토카드 세트', 25000, 30, 30, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6));
