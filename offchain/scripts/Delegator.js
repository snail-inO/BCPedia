import { createRequire } from "module";
import { PROVIDER } from "./types.js";
const require = createRequire(import.meta.url);

const Web3 = require("web3");
const contract = require("@truffle/contract");
const sourceWeb3 = new Web3.providers.HttpProvider(PROVIDER);

const BCPediaDelegator = require("../../onchain/build/contracts/BCPediaDelegator.json");

const DelegatorContract = contract(BCPediaDelegator);
DelegatorContract.setProvider(sourceWeb3);

class Delegator {
  constructor(_addr) {
    this.addr = _addr;
    this.delegator = null;
  }

  init = async () => {
    this.delegator = await DelegatorContract.at(this.addr);
  };

  requestPromote = async (account) => {
    await this.delegator.requestPromote({ from: account });
  };

  resign = async (account) => {
    await this.delegator.resign({ from: account });
  };

  existenceCheck = async (account, id, category, keyword) => {
    const res = await this.delegator.existenceCheck.call(id, category, keyword, {from: account});
    return `Existence: ${res}`;
  };

  batchUpdateLikes = async (account, ids, increment) => {
    await this.delegator.batchUpdateLikes(ids, increment, {from: account});
  };

  hashes = async (id) => {
    const hash = await this.delegator.hashes(id);
    return `The hash of entry ${id} is: ${hash}`;
  }
}

export default Delegator;
