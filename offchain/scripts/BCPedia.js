import Delegator from "./Delegator.js";
import Governor from "./Governor.js";
import Periphery from "./periphery.js";
import Tokens from "./Tokens.js";
import contractAddr from "./contract_addr.json" assert {type: 'json'};

const INVALID_ARGS = "Invalid arguments";
const INVALID_FUNC = "Invalid function";
const INVALID_CONTRACT = "Invalid contract";

async function main() {
  const args = process.argv;
  let contract;
  let res;
  if (args.length > 4) {
    contract = args[2].toLowerCase();
  } else {
    console.log(
      "usage: node BCPeida.js [contract name] [function name] [parameters]"
    );
    return;
  }
  switch (contract) {
    case "tokens":
      const tokens = new Tokens(contractAddr.tokens);
      await tokens.init();
      res = await tokenFunction(tokens, args);
      break;
    case "delegator":
      const delegator = new Delegator(contractAddr.delegator);
      await delegator.init();
      res = await delegatorFunction(delegator, args);
      break;
    case "governor":
      const governor = new Governor(contractAddr.governor);
      await governor.init();
      res = await governorFunction(governor, args);
      break;
    case "periphery":
      const periphery = new Periphery(contractAddr.periphery);
      await periphery.init();
      res = await peripheryFunction(periphery, args);
      break;
    default:
      res = INVALID_CONTRACT;
  }
  console.log(res);

  return;
}

async function tokenFunction(tokens, args) {
  const functionName = args[3].toLowerCase();

  switch (functionName) {
    case "getbalance":
      if (args.length > 5) {
        return await tokens.getBalance(args[4], args[5]);
      } else {
        return INVALID_ARGS;
      }
    case "buycoin":
      if (args.length > 5) {
        await tokens.buyCoin(args[4], args[5]);
        return await tokens.getBalance(args[4], 1);
      } else {
        return INVALID_ARGS;
      }
    case "ownerof":
      return await tokens.ownerOf(args[4]);
    case "uri":
      return await tokens.uri(args[4]);
    case "setapprovalforall":
      if (args.length > 6) {
        await tokens.setApprovalForAll(args[4], args[5], args[6]);
        return "Done";
      } else {
        return INVALID_ARGS;
      }
    default:
      return INVALID_FUNC;
  }
}

async function delegatorFunction(delegator, args) {
  const functionName = args[3].toLowerCase();

  switch (functionName) {
    case "requestpromote":
      await delegator.requestPromote(args[4]);
      return "Done";
    case "resign":
      await delegator.resign(args[4]);
      return "Done";
    case "existencecheck":
      if (args.length > 7) {
        return await delegator.existenceCheck(
          args[4],
          args[5],
          args[6],
          args[7]
        );
      } else {
        return INVALID_ARGS;
      }
    case "batchupdatelikes":
      if (args.length > 6) {
        const paramLen = args.length - 5;
        if (paramLen % 2 != 0) {
          return INVALID_ARGS;
        }
        let ids = args.slice(5, 5 + paramLen / 2);
        let increment = args.slice(5 + paramLen / 2, args.length);
        await delegator.batchUpdateLikes(args[4], ids, increment);
        return "Done";
      } else {
        return INVALID_ARGS;
      }
    case "hashes":
      return await delegator.hashes(args[4]);
    default:
      return INVALID_FUNC;
  }
}

async function governorFunction(governor, args) {
  const functionName = args[3].toLowerCase();

  switch (functionName) {
    case "state":
      return await governor.state(args[4]);
    case "castvote":
      if (args.length > 6) {
        return await governor.castVote(args[4], args[5], args[6]);
      } else {
        return INVALID_ARGS;
      }
    case "proposalsnapshot":
      return await governor.proposalSnapshot(args[4]);
    case "proposaldeadline":
      return await governor.proposalDeadline(args[4]);
    default:
      return INVALID_FUNC;
  }
}

async function peripheryFunction(periphery, args) {
  const functionName = args[3].toLowerCase();

  switch (functionName) {
    case "createentry":
      if (args.length > 7) {
        return await periphery.createEntry(args[4], args[5], args[6], args[7]);
      } else {
        return INVALID_ARGS;
      }
    case "updateentry":
      if (args.length > 7) {
        return await periphery.updateEntry(args[4], args[5], args[6], args[7]);
      } else {
        return INVALID_ARGS;
      }
    case "disputeentry":
      if (args.length > 5) {
        return await periphery.disputeEntry(args[4], args[5]);
      } else {
        return INVALID_ARGS;
      }
    case "proceedproposal":
      if (args.length > 10) {
        return await periphery.proceedProposal(
          args[4],
          args[5],
          args[6],
          args[7],
          args[8],
          args[9],
          args[10]
        );
      } else {
        return INVALID_ARGS;
      }
    default:
      return INVALID_FUNC;
  }
}

main();
