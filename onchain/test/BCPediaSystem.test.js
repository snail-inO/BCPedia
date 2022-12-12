const Web3 = require("web3");
const {
  expectRevert,
  expectEvent,
  time,
  balance,
} = require("@openzeppelin/test-helpers");
const { expect } = require("chai");
const SHA256 = require("crypto-js/sha256");
const BN = Web3.utils.BN;

const LOCAL_ENDPOINT = "http://127.0.0.1:8545";

const LIKE = 0;
const BCPC = 1;
const PENDING = 0;
const DEFEATED = 3;
const SUCCEEDED = 4;
const QUEUED = 5;
const EXECUTED = 7;
const INIT_NFT_ID = 4;

const BCPediaTokens = artifacts.require("./BCPediaTokens");
const BCPediaDelegator = artifacts.require("./BCPediaDelegator");
const BCPediaGovernor = artifacts.require("./BCPediaGovernor");
const BCPediaPeriphery = artifacts.require("./BCPediaPeriphery");

contract("BCPediaSystem Tests", async (accounts) => {
  let sourceWeb3;
  let tokens;
  let delegator;
  let governor;
  let periphery;
  let timelock;
  const owner = accounts[0];

  before(async () => {
    sourceWeb3 = new Web3(LOCAL_ENDPOINT);
  });

  beforeEach(async () => {
    tokens = await BCPediaTokens.new();
    delegator = await BCPediaDelegator.new(tokens.address);
    governor = await BCPediaGovernor.new(delegator.address);
    periphery = await BCPediaPeriphery.new(governor.address);

    await tokens.transferOwnership(delegator.address);
    await delegator.setPeriphery(periphery.address);
    await delegator.transferOwnership(await governor.timelock());
    await governor.transferOwnership(periphery.address);
  });

  describe("createEntry", () => {
    const category = "test";
    const keyword = "test1";
    const hash = "0x" + SHA256("test message").toString();

    it("should successfully create entry", async () => {
      const amount = 10;
      await tokens.buyCoin(amount, { value: 5 * amount });
      expect((await tokens.balanceOf(owner, BCPC)).toNumber()).to.equal(amount);
      await tokens.setApprovalForAll(periphery.address, true);
      let pid = (
        await periphery.createEntry.call(hash, category, keyword)
      ).toString();
      await periphery.createEntry(hash, category, keyword);

      expect((await governor.state(pid)).toNumber()).to.equal(PENDING);
    });
  });

  //   describe("castVote", () => {
  //     const category = "test";
  //     const keyword = "test1";
  //     const hash = "0x" + SHA256("test message").toString();
  //     const amount = 20;

  //     it("should successfully cast vote", async () => {
  //       await tokens.buyCoin(amount, { value: 5 * amount });
  //       expect((await tokens.balanceOf(owner, BCPC)).toNumber()).to.equal(amount);
  //       await tokens.setApprovalForAll(periphery.address, true);
  //       let pid = (
  //         await periphery.createEntry.call(hash, category, keyword)
  //       ).toString();
  //       await periphery.createEntry(hash, category, keyword);

  //       expect((await governor.state(pid)).toNumber()).to.equal(PENDING);
  //       let likes = (await tokens.balanceOf(owner, LIKE)).toNumber();
  //       let startBlock = (await governor.proposalSnapshot(pid)).toNumber();
  //       await time.advanceBlockTo(startBlock);
  //       await tokens.setApprovalForAll(governor.address, true);
  //       expect((await governor.castVote.call(pid, 1)).toNumber()).to.equal(likes);
  //       await governor.castVote(pid, 1);
  //     });
  //   });

  describe("proceedProposal", () => {
    const category = "test";
    const keyword = "test1";
    const hash = "0x" + SHA256("test message").toString();
    const hash2 = "0x" + SHA256("test message2").toString();
    const amount = 20;

    it("should successfully execute proposal", async () => {
      // create entry
      await tokens.buyCoin(amount, { value: 5 * amount });
      let balance = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance).to.equal(amount);
      await tokens.setApprovalForAll(periphery.address, true);
      let pid = (
        await periphery.createEntry.call(hash, category, keyword)
      ).toString();
      await periphery.createEntry(hash, category, keyword);
      await tokens.setApprovalForAll(periphery.address, false);
      expect((await governor.state(pid)).toNumber()).to.equal(PENDING);
      let balance2 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance2).to.equal(balance - 5);

      let likes = (await tokens.balanceOf(owner, LIKE)).toNumber();
      let startBlock = (await governor.proposalSnapshot(pid)).toNumber();
      await time.advanceBlockTo(startBlock);
      await tokens.setApprovalForAll(governor.address, true);
      expect((await governor.castVote.call(pid, 1)).toNumber()).to.equal(likes);
      await governor.castVote(pid, 1);
      await tokens.setApprovalForAll(governor.address, false);
      let balance3 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance3).to.equal(balance2 - 10);

      let endBlock = (await governor.proposalDeadline(pid)).toNumber();
      await time.advanceBlockTo(endBlock + 1);
      expect((await governor.state(pid)).toNumber()).to.equal(SUCCEEDED);

      await periphery.proceedProposal(pid, 0, hash, category, keyword, 0);
      expect((await governor.state(pid)).toNumber()).to.equal(QUEUED);

      await time.increase(time.duration.hours(2));
      await periphery.proceedProposal(pid, 0, hash, category, keyword, 0);
      expect((await governor.state(pid)).toNumber()).to.equal(EXECUTED);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash);
      expect((await tokens.balanceOf(owner, INIT_NFT_ID)).toNumber()).to.equal(
        1
      );
      let balance4 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance4).to.equal(balance3 + 17);

      // update entry
      await tokens.setApprovalForAll(periphery.address, true);
      pid = (
        await periphery.updateEntry.call(hash2, category, keyword)
      ).toString();
      await periphery.updateEntry(hash2, category, keyword);
      await tokens.setApprovalForAll(periphery.address, false);
      expect((await governor.state(pid)).toNumber()).to.equal(PENDING);
      balance2 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance2).to.equal(balance4 - 5);

      likes = (await tokens.balanceOf(owner, LIKE)).toNumber();
      startBlock = (await governor.proposalSnapshot(pid)).toNumber();
      await time.advanceBlockTo(startBlock);
      await tokens.setApprovalForAll(governor.address, true);
      expect((await governor.castVote.call(pid, 1)).toNumber()).to.equal(likes);
      await governor.castVote(pid, 1);
      await tokens.setApprovalForAll(governor.address, false);
      balance3 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance3).to.equal(balance2 - 10);

      endBlock = (await governor.proposalDeadline(pid)).toNumber();
      await time.advanceBlockTo(endBlock + 1);
      expect((await governor.state(pid)).toNumber()).to.equal(SUCCEEDED);

      await periphery.proceedProposal(pid, 1, hash2, category, keyword, 0);
      expect((await governor.state(pid)).toNumber()).to.equal(QUEUED);

      await time.increase(time.duration.hours(2));
      await periphery.proceedProposal(pid, 1, hash2, category, keyword, 0);
      expect((await governor.state(pid)).toNumber()).to.equal(EXECUTED);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash2);
      expect((await tokens.balanceOf(owner, INIT_NFT_ID)).toNumber()).to.equal(
        1
      );
      balance4 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance4).to.equal(balance3 + 17);

      // dispute entry
      await tokens.setApprovalForAll(periphery.address, true);
      pid = (await periphery.disputeEntry.call(INIT_NFT_ID)).toString();
      await periphery.disputeEntry(INIT_NFT_ID);
      await tokens.setApprovalForAll(periphery.address, false);
      expect((await governor.state(pid)).toNumber()).to.equal(PENDING);
      balance2 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance2).to.equal(balance4 - 5);

      likes = (await tokens.balanceOf(owner, LIKE)).toNumber();
      startBlock = (await governor.proposalSnapshot(pid)).toNumber();
      await time.advanceBlockTo(startBlock);
      await tokens.setApprovalForAll(governor.address, true);
      expect((await governor.castVote.call(pid, 1)).toNumber()).to.equal(likes);
      await governor.castVote(pid, 1);
      await tokens.setApprovalForAll(governor.address, false);
      balance3 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance3).to.equal(balance2 - 10);

      endBlock = (await governor.proposalDeadline(pid)).toNumber();
      await time.advanceBlockTo(endBlock + 1);
      expect((await governor.state(pid)).toNumber()).to.equal(SUCCEEDED);

      await periphery.proceedProposal(
        pid,
        2,
        hash2,
        category,
        keyword,
        INIT_NFT_ID
      );
      expect((await governor.state(pid)).toNumber()).to.equal(QUEUED);

      await time.increase(time.duration.hours(2));
      await periphery.proceedProposal(
        pid,
        2,
        hash2,
        category,
        keyword,
        INIT_NFT_ID
      );
      expect((await governor.state(pid)).toNumber()).to.equal(EXECUTED);
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(
        Web3.utils.bytesToHex(Buffer.alloc(32))
      );
      expect((await tokens.balanceOf(owner, INIT_NFT_ID)).toNumber()).to.equal(
        0
      );
      balance4 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance4).to.equal(balance3 + 17);
    });

    it("should not successfully execute proposal", async () => {
      await tokens.buyCoin(amount, { value: 5 * amount });
      let balance = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance).to.equal(amount);
      await tokens.setApprovalForAll(periphery.address, true);
      let pid = (
        await periphery.createEntry.call(hash, category, keyword)
      ).toString();
      await periphery.createEntry(hash, category, keyword);
      await tokens.setApprovalForAll(periphery.address, false);
      expect((await governor.state(pid)).toNumber()).to.equal(PENDING);
      let balance2 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance2).to.equal(balance - 5);

      let likes = (await tokens.balanceOf(owner, LIKE)).toNumber();
      let startBlock = (await governor.proposalSnapshot(pid)).toNumber();
      await time.advanceBlockTo(startBlock);
      await tokens.setApprovalForAll(governor.address, true);
      expect((await governor.castVote.call(pid, 0)).toNumber()).to.equal(likes);
      await governor.castVote(pid, 0);
      await tokens.setApprovalForAll(governor.address, false);
      let balance3 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance3).to.equal(balance2 - 10);

      let endBlock = (await governor.proposalDeadline(pid)).toNumber();
      await time.advanceBlockTo(endBlock + 1);
      expect((await governor.state(pid)).toNumber()).to.equal(DEFEATED);

      await periphery.proceedProposal(pid, 0, hash, category, keyword, 0);
      expect((await governor.state(pid)).toNumber()).to.equal(DEFEATED);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        0
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(
        Web3.utils.bytesToHex(Buffer.alloc(32))
      );
      expect((await tokens.balanceOf(owner, INIT_NFT_ID)).toNumber()).to.equal(
        0
      );
      let balance4 = (await tokens.balanceOf(owner, BCPC)).toNumber();
      expect(balance4).to.equal(balance3 + 11);
    });
  });
});
