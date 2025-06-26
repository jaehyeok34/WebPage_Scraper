import { readFile, writeFile } from "fs/promises";
import { Item } from "../interface/item";
import authorDomain from "../author_domain";

async function getCutoff(path: string): Promise<string> {
    return await readFile(path, 'utf-8').catch(() => '');
}

async function setCutoff(path: string, cutoff: string) {
    await writeFile(path, cutoff, 'utf-8');
}

async function savePost(path: string, items: Item[]) {
    const buildText = (items: Item[]) => items.flatMap(item => {
        let text = authorDomain[item.author] || '';
        if (text === '') {
            console.log(`등록되지 않은 작성자: ${item.author}`);
            text = '5, 71, 77'; // 기본값
        }
        return [item.title.text, item.title.href, text, ''];
    }).join('\n');
    await writeFile(path, buildText(items), 'utf-8');
}

export { getCutoff, setCutoff, savePost };