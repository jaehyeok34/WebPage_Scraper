import { Item } from "./interface/item";
import { getCutoff, savePost, setCutoff } from "./utils/file";
import { scrape } from "./utils/scrape";


async function main() {
    const file = './cutoff.txt';
    const url = 'https://research.hanbat.ac.kr/ko/projects';
    const outputFile = './post.txt';

    const cutoff = await getCutoff(file);
    const allItem: Item[] = await scrape(url, cutoff);
    
    allItem[0]?.date && await setCutoff(file, allItem[0].date); // 컷오프 갱신
    await savePost(outputFile, allItem); // 저장
}

main().catch(console.error);
