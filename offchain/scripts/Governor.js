import { createRequire } from "module";
import { PROVIDER } from "./types.js";
const require = createRequire(import.meta.url);

const Web3 = require("web3");
const contract = require("@truffle/contract");
const sourceWeb3 = new Web3.providers.HttpProvider(PROVIDER);

const BCPediaGovernor = require("../../onchain/build/contracts/BCPediaGovernor.json");

const GovernorContract = contract(BCPediaGovernor);
GovernorContract.setProvider(sourceWeb3);

const Pending = 0;
const Active = 1;
const Canceled = 2;
const Defeated = 3;
const Succeeded = 4;
const Queued = 5;
const Expired = 6;
const Executed = 7;

class Governor {
  constructor(_addr) {
    this.addr = _addr;
    this.governor = null;
  }

  init = async () => {
    this.governor = await GovernorContract.at(this.addr);
  };

  state = async (pid) => {
    const curState = (await this.governor.state(pid)).toNumber();
    let str;
    switch (curState) {
      case Pending:
        str = "Pending";
        break;
      case Active:
        str = "Active";
        break;
      case Canceled:
        str = "Canceled";
        break;
      case Defeated:
        str = "Defeated";
        break;
      case Succeeded:
        str = "Succeeded";
        break;
      case Queued:
        str = "Queued";
        break;
      case Expired:
        str = "Expired";
        break;
      case Executed:
        str = "Executed";
        break;
      default:
        str = "Undefined";
    }
    return `Proposal ${pid} state: ${str}`;
  };

  castVote = async (account, pid, vote) => {
    let weight = await this.governor.castVote.call(pid, vote, {
      from: account,
    });
    await this.governor.castVote(pid, vote, { from: account });
    return `Account ${account} casted ${weight} of type ${vote} vote to proposal ${pid}`;
  };

  proposalSnapshot = async (pid) => {
    const block = await this.governor.proposalSnapshot(pid);
    return `Proposal ${pid} voting period will start at block ${block}`;
  };

  proposalDeadline = async (pid) => {
    const block = await this.governor.proposalDeadline(pid);
    return `Proposal ${pid} voting period will end at block ${block}`;
  };
}

export default Governor;
