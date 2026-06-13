/**
 * 시나리오 1 — 재고 오버셀
 *
 * 재고 100개짜리 굿즈에 200명이 동시에 구매 요청을 보낸다.
 * 락이 없으면 remaining_quantity < 0 또는 100개 초과 판매가 발생한다.
 *
 * 사전 준비 (k6 실행 전 DB 초기화):
 *   docker exec -i akibaroom-mysql mysql -uakibaroom -pakibaroom akibaroom < k6/setup.sql
 */

import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = 'http://localhost:8080';

// 시드 회원 2명 (회원 생성 API 없음)
const MEMBER_IDS = [
    '019ebc6d-2cb7-759d-854f-2728b598628e',
    '019ebc6d-2cb7-7943-891f-493d485e00de',
];

// 재고 100개, 가격 15,000원
const GOODS_ID = '019ebc6d-2cb7-740f-b7fa-1acf0863cbcf';

export function setup() {
    // 각 회원에게 200건분 충전 (200 × 15,000 = 3,000,000)
    for (const memberId of MEMBER_IDS) {
        http.post(
            `${BASE_URL}/api/v1/charges`,
            JSON.stringify({ memberId, amount: 3000000 }),
            { headers: { 'Content-Type': 'application/json' } },
        );
    }
}

export const options = {
    vus: 200,
    iterations: 200,
};

export default function () {
    const memberId = MEMBER_IDS[__VU % MEMBER_IDS.length];

    const res = http.post(
        `${BASE_URL}/api/v1/orders`,
        JSON.stringify({ memberId, goodsId: GOODS_ID, quantity: 1 }),
        { headers: { 'Content-Type': 'application/json' } },
    );

    check(res, {
        '201 or 409': (r) => r.status === 201 || r.status === 409,
    });
}
