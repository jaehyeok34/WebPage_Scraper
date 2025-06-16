import puppeteer, { executablePath, Page } from "puppeteer-core";
import { Item } from "./item_interface";

async function scrape(url: string, cutoff: string): Promise<Item[]> {
    const browser = await puppeteer.launch({
        headless: false, 
        executablePath: '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'
    });
    const page = await browser.newPage();
    
    const allItem: Item[] = [];
    for (let i = 1; i <= 10; i++) {
        await page.goto(url + `?pagination.pageIndex=${i}`, {waitUntil: 'domcontentloaded'});
        const { items, flag } = await getItems(page, cutoff);
        allItem.push(...items);

        if (flag) break; // 컷오프 날짜 이전의 게시글이 발견되면 중단
    }

    await browser.close();
    return allItem;
}

async function getItems(page: Page, cutoff: string): Promise<{items: Item[], flag: boolean}> {
    return await page.$$eval('#sub-contents > div.bbs-default > ul > li', (elements, cutoff) => {
        const items: Item[] = [];
        let flag = false;
        for (let element of elements) {
            const date = element.querySelector('div.col.col-date > span')?.textContent?.replace(/[\n\t\s]/g, '').trim() || '';
            if (date === '') throw new Error('게시글 날짜 추출 실패');
            if (date <= cutoff) { // 날짜가 컷오프보다 이전이면 중단
                flag = true;
                break;   
            }

            const titleDiv = element.querySelector('div.col.col-title > span');
            const authorDiv = element.querySelector('div.col.col-author > span');
            items.push({
                title: {
                    text: titleDiv?.textContent?.replace(/[\n\t\s]/g, '').trim() || '',
                    href: titleDiv?.querySelector('a')?.getAttribute('href') || ''
                },
                author: authorDiv?.textContent?.replace(/[\n\t\s]/g, '').trim() || '',
                date
            });
        }

        return { items, flag };
    }, cutoff);
}

export { scrape };