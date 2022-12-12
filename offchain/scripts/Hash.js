
import { createRequire } from "module";
const require = createRequire(import.meta.url);
const SHA256 = require("crypto-js/sha256");

console.log("0x" + SHA256(process.argv[2]).toString());