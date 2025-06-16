import puppeteer from "puppeteer";
import { getItems } from "./utils";
import { Item } from "./item_interface";
import fs from "fs/promises";

async function main() {
    const file = './cutoff.txt';
    let cutoff: string;
    try {
        cutoff = await fs.readFile(file, 'utf-8');
    } catch (_) {
        cutoff = ''; // 파일이 없거나 읽기 실패 시 빈 문자열로 초기화
    }
    const url = 'https://research.hanbat.ac.kr/ko/projects';
    const browser = await puppeteer.launch({headless: false});
    const page = await browser.newPage();
    
    const allItem: Item[] = [];
    for (let i = 1; i <= 10; i++) {
        await page.goto(url + `?pagination.pageIndex=${i}`, {waitUntil: 'domcontentloaded'});
        const { items, flag } = await getItems(page, cutoff);
        allItem.push(...items);

        if (flag) break; // 컷오프 날짜 이전의 게시글이 발견되면 중단
    }

    allItem[0]?.date && fs.writeFile(file, allItem[0].date, 'utf-8'); // 컷오프 갱신

    console.log(allItem.length);

    await browser.close();
}

main().catch(console.error);
