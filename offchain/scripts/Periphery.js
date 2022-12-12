import { createRequire } from "module";
import { PROVIDER } from "./types.js";
const require = createRequire(import.meta.url);

const Web3 = require("web3");
const contract = require("@truffle/contract");
const sourceWeb3 = new Web3.providers.HttpProvider(PROVIDER);

const BCPediaPeriphery = require("../../onchain/build/contracts/BCPediaPeriphery.json");

const PeripheryContract = contract(BCPediaPeriphery);
PeripheryContract.setProvider(sourceWeb3);

class Periphery {
  constructor(_addr) {
    this.addr = _addr;
    this.periphery = null;
  }

  init = async () => {
    this.periphery = await PeripheryContract.at(this.addr);
  }

  createEntry = async (account, hash, category, keyword) => {
    const pid = (await this.periphery.createEntry.call(hash, category, keyword, {from: account})).toString();
    await this.periphery.createEntry(hash, category, keyword, {from: account});
    return `Account ${account} createEntry proposal id: ${pid}`;
  };

  updateEntry = async (account, hash, category, keyword) => {
    const pid = (await this.periphery.updateEntry.call(hash, category, keyword, {from: account})).toString();
    await this.periphery.updateEntry(hash, category, keyword, {from: account});
    return `Account ${account} updateEntry proposal id: ${pid}`;
  };

  disputeEntry = async (account, tid) => {
    const pid = (await this.periphery.disputeEntry.call(tid, {from: account})).toString();
    await this.periphery.disputeEntry(tid, {from: account});
    return `Account ${account} diputeEntry proposal id: ${pid}`;
  };

  proceedProposal = async (account, pid, method, hash, category, keyword, tid) => {
    const success = await this.periphery.proceedProposal.call(pid, method, hash, category, keyword, tid, {from: account});
    await this.periphery.proceedProposal(pid, method, hash, category, keyword, tid, {from: account});
    return `Proposal ${pid} proceeded: ${success}`;
  }
}

export default Periphery;