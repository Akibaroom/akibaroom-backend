/**
 * 시나리오 2 — 잔액 오버드로우
 *
 * 잔액 15,000원(1회 구매분)인 계정 1개로 10개 VU가 동시에 구매 요청을 보낸다.
 * 락이 없으면 같은 잔액을 동시에 읽어 1건분 잔액으로 10건이 성공한다.
 *
 * 사전 준비 (k6 실행 전 DB 초기화):
 *   docker exec -i akibaroom-mysql mysql -uakibaroom -pakibaroom akibaroom < k6/setup.sql
 */

import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = 'http://localhost:8080';

const MEMBER_ID = '019ebc6d-2cb7-759d-854f-2728b598628e';
const GOODS_ID = '019ebc6d-2cb7-740f-b7fa-1acf0863cbcf';

export function setup() {
    // 딱 1회 구매분만 충전
    http.post(
        `${BASE_URL}/api/v1/charges`,
        JSON.stringify({ memberId: MEMBER_ID, amount: 15000 }),
        { headers: { 'Content-Type': 'application/json' } },
    );
}

export const options = {
    vus: 10,
    iterations: 10,
};

export default function () {
    const res = http.post(
        `${BASE_URL}/api/v1/orders`,
        JSON.stringify({ memberId: MEMBER_ID, goodsId: GOODS_ID, quantity: 1 }),
        { headers: { 'Content-Type': 'application/json' } },
    );

    check(res, {
        '201 or 409': (r) => r.status === 201 || r.status === 409,
    });
}
