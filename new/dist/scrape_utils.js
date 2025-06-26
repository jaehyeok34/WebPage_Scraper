"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.scrape = scrape;
const puppeteer_core_1 = __importDefault(require("puppeteer-core"));
function scrape(url, cutoff) {
    return __awaiter(this, void 0, void 0, function* () {
        const browser = yield puppeteer_core_1.default.launch({
            headless: false,
            executablePath: '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'
        });
        const page = yield browser.newPage();
        const allItem = [];
        for (let i = 1; i <= 10; i++) {
            yield page.goto(url + `?pagination.pageIndex=${i}`, { waitUntil: 'domcontentloaded' });
            const { items, flag } = yield getItems(page, cutoff);
            allItem.push(...items);
            if (flag)
                break; // 컷오프 날짜 이전의 게시글이 발견되면 중단
        }
        yield browser.close();
        return allItem;
    });
}
function getItems(page, cutoff) {
    return __awaiter(this, void 0, void 0, function* () {
        return yield page.$$eval('#sub-contents > div.bbs-default > ul > li', (elements, cutoff) => {
            var _a, _b, _c, _d, _e;
            const items = [];
            let flag = false;
            for (let element of elements) {
                const date = ((_b = (_a = element.querySelector('div.col.col-date > span')) === null || _a === void 0 ? void 0 : _a.textContent) === null || _b === void 0 ? void 0 : _b.replace(/[\n\t\s]/g, '').trim()) || '';
                if (date === '')
                    throw new Error('게시글 날짜 추출 실패');
                if (date <= cutoff) { // 날짜가 컷오프보다 이전이면 중단
                    flag = true;
                    break;
                }
                const titleDiv = element.querySelector('div.col.col-title > span');
                const authorDiv = element.querySelector('div.col.col-author > span');
                items.push({
                    title: {
                        text: ((_c = titleDiv === null || titleDiv === void 0 ? void 0 : titleDiv.textContent) === null || _c === void 0 ? void 0 : _c.replace(/[\n\t\s]/g, '').trim()) || '',
                        href: ((_d = titleDiv === null || titleDiv === void 0 ? void 0 : titleDiv.querySelector('a')) === null || _d === void 0 ? void 0 : _d.getAttribute('href')) || ''
                    },
                    author: ((_e = authorDiv === null || authorDiv === void 0 ? void 0 : authorDiv.textContent) === null || _e === void 0 ? void 0 : _e.replace(/[\n\t\s]/g, '').trim()) || '',
                    date
                });
            }
            return { items, flag };
        }, cutoff);
    });
}
