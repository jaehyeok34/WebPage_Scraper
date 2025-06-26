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
exports.getCutoff = getCutoff;
exports.setCutoff = setCutoff;
exports.savePost = savePost;
const promises_1 = require("fs/promises");
const author_table_1 = __importDefault(require("./author_table"));
function getCutoff(path) {
    return __awaiter(this, void 0, void 0, function* () {
        return yield (0, promises_1.readFile)(path, 'utf-8').catch(() => '');
    });
}
function setCutoff(path, cutoff) {
    return __awaiter(this, void 0, void 0, function* () {
        yield (0, promises_1.writeFile)(path, cutoff, 'utf-8');
    });
}
function savePost(path, items) {
    return __awaiter(this, void 0, void 0, function* () {
        const buildText = (items) => items.flatMap(item => {
            let text = author_table_1.default[item.author] || '';
            if (text === '') {
                console.log(`등록되지 않은 작성자: ${item.author}`);
                text = '5, 71, 77'; // 기본값
            }
            return [item.title.text, item.title.href, text, ''];
        }).join('\n');
        yield (0, promises_1.writeFile)(path, buildText(items), 'utf-8');
    });
}
