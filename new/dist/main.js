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
Object.defineProperty(exports, "__esModule", { value: true });
const scrape_utils_1 = require("./scrape_utils");
const file_utils_1 = require("./file_utils");
function main() {
    return __awaiter(this, void 0, void 0, function* () {
        var _a;
        const file = './cutoff.txt';
        const url = 'https://research.hanbat.ac.kr/ko/projects';
        const outputFile = './post.txt';
        const cutoff = yield (0, file_utils_1.getCutoff)(file);
        const allItem = yield (0, scrape_utils_1.scrape)(url, cutoff);
        ((_a = allItem[0]) === null || _a === void 0 ? void 0 : _a.date) && (yield (0, file_utils_1.setCutoff)(file, allItem[0].date)); // 컷오프 갱신
        yield (0, file_utils_1.savePost)(outputFile, allItem); // 저장
    });
}
main().catch(console.error);
