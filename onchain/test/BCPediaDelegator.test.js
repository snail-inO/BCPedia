const Web3 = require("Web3");
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

const INIT_NFT_ID = 4;
const OWNABLE_REVERT = "Ownable: caller is not the owner";

const BCPediaTokens = artifacts.require("./BCPediaTokens");
const BCPediaDelegator = artifacts.require("./BCPediaDelegator");

contract("BCPediaDelegator Tests", async (accounts) => {
  let sourceWeb3;
  let tokens;
  let delegator;
  const owner = accounts[0];
  const alice = accounts[1];
  const bob = accounts[2];

  before(async () => {
    sourceWeb3 = new Web3(LOCAL_ENDPOINT);
  });

  beforeEach(async () => {
    tokens = await BCPediaTokens.new();
    delegator = await BCPediaDelegator.new(tokens.address);
    await tokens.transferOwnership(delegator.address);
  });

  describe("createEntry", () => {
    const category = "test";
    const keyword = "test1";
    const hash = "0x" + SHA256("test message").toString();
    it("should successfully create entry", async () => {
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        0
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(
        Web3.utils.bytesToHex(Buffer.alloc(32))
      );

      await delegator.createEntry(alice, hash, category, keyword);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash);
    });

    it("should not create existed entry", async () => {
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        0
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(
        Web3.utils.bytesToHex(Buffer.alloc(32))
      );

      await delegator.createEntry(alice, hash, category, keyword);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash);

      await expectRevert.unspecified(
        delegator.createEntry(alice, hash, category, keyword)
      );
    });

    it("should not create entry when called by unauthorized operator", async () => {
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        0
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(
        Web3.utils.bytesToHex(Buffer.alloc(32))
      );

      await expectRevert(
        delegator.createEntry(alice, hash, category, keyword, { from: alice }),
        OWNABLE_REVERT
      );
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        0
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(
        Web3.utils.bytesToHex(Buffer.alloc(32))
      );
    });
  });

  describe("updateEntry", () => {
    const category = "test";
    const keyword = "test2";
    const hash1 = "0x" + SHA256("test1 message").toString();
    const hash2 = "0x" + SHA256("test2 message").toString();
    it("should successfully update entry", async () => {
      await delegator.createEntry(alice, hash1, category, keyword);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash1);

      await delegator.updateEntry(hash2, category, keyword);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash2);
    });

    it("should not update entry with identical hash", async () => {
      await delegator.createEntry(alice, hash1, category, keyword);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash1);

      await expectRevert.unspecified(
        delegator.updateEntry(hash1, category, keyword)
      );
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash1);
    });

    it("should not update inexistent entry", async () => {
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        0
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(
        Web3.utils.bytesToHex(Buffer.alloc(32))
      );

      await expectRevert.unspecified(
        delegator.updateEntry(hash2, category, keyword)
      );
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        0
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(
        Web3.utils.bytesToHex(Buffer.alloc(32))
      );
    });
    it("should not update entry when called by unauthorized operator", async () => {
      await delegator.createEntry(alice, hash1, category, keyword);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash1);

      await expectRevert(
        delegator.updateEntry(hash2, category, keyword, { from: alice }),
        OWNABLE_REVERT
      );
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash1);
    });
  });

  describe("batchUpdateLikes", () => {
    const category = "test";
    const keywords = ["test1", "test2", "test3"];
    const hashes = [
      "0x" + SHA256("test message1").toString(),
      "0x" + SHA256("test message2").toString(),
      "0x" + SHA256("test message3").toString(),
    ];

    it("should successfully update likes", async () => {
      const count = 3;
      const ids = [INIT_NFT_ID, INIT_NFT_ID + 1, INIT_NFT_ID + 2];
      const increment = [5, 10, 2];

      for (let i = 0; i < count; i++) {
        await delegator.createEntry(alice, hashes[i], category, keywords[i]);
        expect(
          (await delegator.entries(category, keywords[i])).toNumber()
        ).to.equal(INIT_NFT_ID + i);
        expect(await delegator.hashes(INIT_NFT_ID + i)).to.equal(hashes[i]);
        expect((await delegator.likes(INIT_NFT_ID + i)).toNumber()).to.equal(0);
      }

      await delegator.batchUpdateLikes(ids, increment);
      for (let i = 0; i < count; i++) {
        expect((await delegator.likes(INIT_NFT_ID + i)).toNumber()).to.equal(
          increment[i]
        );
      }
      expect(
        (await tokens.balanceOf(alice, await tokens.LIKE())).toNumber()
      ).to.equal(increment.reduce((a, b) => a + b));
    });
    it("should not update likes of invalid token ids", async () => {
      const count = 3;
      const ids = [INIT_NFT_ID, INIT_NFT_ID + 1, 0];
      const increment = [5, 10, 2];

      for (let i = 0; i < count; i++) {
        await delegator.createEntry(alice, hashes[i], category, keywords[i]);
        expect(
          (await delegator.entries(category, keywords[i])).toNumber()
        ).to.equal(INIT_NFT_ID + i);
        expect(await delegator.hashes(INIT_NFT_ID + i)).to.equal(hashes[i]);
        expect((await delegator.likes(INIT_NFT_ID + i)).toNumber()).to.equal(0);
      }

      await expectRevert.unspecified(
        delegator.batchUpdateLikes(ids, increment)
      );
      for (let i = 0; i < count; i++) {
        expect((await delegator.likes(INIT_NFT_ID + i)).toNumber()).to.equal(0);
      }
    });
    it("should not update likes when length of call data doesn't match", async () => {
      const count = 3;
      const ids = [INIT_NFT_ID, INIT_NFT_ID + 1, INIT_NFT_ID + 2];
      const increment = [5, 10, 2, 9];

      for (let i = 0; i < count; i++) {
        await delegator.createEntry(alice, hashes[i], category, keywords[i]);
        expect(
          (await delegator.entries(category, keywords[i])).toNumber()
        ).to.equal(INIT_NFT_ID + i);
        expect(await delegator.hashes(INIT_NFT_ID + i)).to.equal(hashes[i]);
        expect((await delegator.likes(INIT_NFT_ID + i)).toNumber()).to.equal(0);
      }

      await expectRevert.unspecified(
        delegator.batchUpdateLikes(ids, increment)
      );
      for (let i = 0; i < count; i++) {
        expect((await delegator.likes(INIT_NFT_ID + i)).toNumber()).to.equal(0);
      }
    });
    it("should not update likes when called by unauthorized operator", async () => {
      const count = 3;
      const ids = [INIT_NFT_ID, INIT_NFT_ID + 1, INIT_NFT_ID + 2];
      const increment = [5, 10, 2];

      for (let i = 0; i < count; i++) {
        await delegator.createEntry(alice, hashes[i], category, keywords[i]);
        expect(
          (await delegator.entries(category, keywords[i])).toNumber()
        ).to.equal(INIT_NFT_ID + i);
        expect(await delegator.hashes(INIT_NFT_ID + i)).to.equal(hashes[i]);
        expect((await delegator.likes(INIT_NFT_ID + i)).toNumber()).to.equal(0);
      }

      await expectRevert.unspecified(
        delegator.batchUpdateLikes(ids, increment, { from: alice })
      );
      for (let i = 0; i < count; i++) {
        expect((await delegator.likes(INIT_NFT_ID + i)).toNumber()).to.equal(0);
      }
    });
  });

  describe("removeEntry", () => {
    const category = "test";
    const keyword = "test4";
    const hash = "0x" + SHA256("test message").toString();
    it("should successfully dispute entry", async () => {
      await delegator.createEntry(alice, hash, category, keyword);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash);

      await tokens.setApprovalForAll(delegator.address, true, { from: alice });
      await delegator.removeEntry(INIT_NFT_ID);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(
        Web3.utils.bytesToHex(Buffer.alloc(32))
      );
    });
    it("should not dispute entry when called by unauthorized operator", async () => {
      await delegator.createEntry(alice, hash, category, keyword);
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash);

      await expectRevert(
        delegator.removeEntry(INIT_NFT_ID, { from: alice }),
        OWNABLE_REVERT
      );
      expect((await delegator.entries(category, keyword)).toNumber()).to.equal(
        INIT_NFT_ID
      );
      expect(await delegator.hashes(INIT_NFT_ID)).to.equal(hash);
    });
  });
});
