import { Page } from "puppeteer";
import { Item } from "./item_interface";

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

export { getItems };