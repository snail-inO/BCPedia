import { createRequire } from "module";
import { PROVIDER } from "./types.js";
const require = createRequire(import.meta.url);

const Web3 = require("web3");
const contract = require("@truffle/contract");
const sourceWeb3 = new Web3.providers.HttpProvider(PROVIDER);

const BCPediaTokens = require("../../onchain/build/contracts/BCPediaTokens.json");

const TokensContract = contract(BCPediaTokens);
TokensContract.setProvider(sourceWeb3);

class Tokens {
  constructor(_addr) {
    this.addr = _addr;
    this.tokens = null;
  }

  init = async () => {
    this.tokens = await TokensContract.at(this.addr);
  }

  getBalance = async (account, id) => {
    const balance = (await this.tokens.balanceOf(account, id)).toNumber();
    return `Account ${account} token ${id} balance: ${balance}`;
  }

  buyCoin = async (account, amount) => {
    await this.tokens.buyCoin(amount, {from: account, value: amount * 5});
  }

  ownerOf = async (id) => {
    const owner = await this.tokens.ownerOf(id);
    return `Owner of token ${id} is ${owner}`;
  }

  uri = async (id) => {
    const link = await this.tokens.uri(id);
    return `URI of token ${id} is ${link}`;
  }

  setApprovalForAll = async (account, operator, authorized) => {
    await this.tokens.setApprovalForAll(operator, authorized, {from: account});
  }
}

export default Tokens;