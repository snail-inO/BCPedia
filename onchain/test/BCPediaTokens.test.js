const Web3 = require("web3");
const {
  expectRevert,
  expectEvent,
  time,
  balance,
} = require("@openzeppelin/test-helpers");
const { expect } = require("chai");
const BN = Web3.utils.BN;

const LOCAL_ENDPOINT = "http://127.0.0.1:8545";

const INIT_FT_ID = 0;
const INIT_NFT_ID = 4;

const OWNABLE_REVERT = "Ownable: caller is not the owner";
const TOKEN_EXIST_REVERT = "NFT doesn't exist";
const TOKEN_ID_INVALID_REVERT = "Invalid FT id";

const META_DATA_URI_PREFIX = "https://portfolio-of.me/entries/";
const META_DATA_URI_POSTFIX = ".json";

const BCPediaTokens = artifacts.require("./BCPediaTokens");

contract("BCPediaTokens Tests", async (accounts) => {
  let sourceWeb3;
  let tokens;
  const alice = accounts[1];
  const bob = accounts[2];

  before(async () => {
    sourceWeb3 = new Web3(LOCAL_ENDPOINT);
  });

  beforeEach(async () => {
    tokens = await BCPediaTokens.new();
  });

  describe("mintNFT", () => {
    it("should successfully mint NFT", async () => {
      expectEvent(await tokens.mintNFT(alice, "0x0"), "TransferSingle", {
        from: new BN(0),
        to: alice,
        id: new BN(INIT_NFT_ID),
        value: new BN(1),
      });
      expect((await tokens.balanceOf(alice, INIT_NFT_ID)).toNumber()).to.equal(
        1
      );
    });

    it("should successfully mint NFTs", async () => {
      for (let tid = INIT_NFT_ID; tid < INIT_NFT_ID + 3; tid++) {
        expectEvent(await tokens.mintNFT(alice, "0x0"), "TransferSingle", {
          from: new BN(0),
          to: alice,
          id: new BN(tid),
          value: new BN(1),
        });
        expect((await tokens.balanceOf(alice, tid)).toNumber()).to.equal(1);
      }
    });

    it("should not successfully mint NFT with unauthorized operator", async () => {
      await expectRevert(
        tokens.mintNFT(alice, "0x0", { from: alice }),
        OWNABLE_REVERT
      );
    });
  });

  describe("uri", () => {
    it("should retrieve token's URI", async () => {
      await tokens.mintNFT(alice, "0x0");
      expect((await tokens.uri(INIT_NFT_ID)).toString()).to.equal(
        getMetaURI(INIT_NFT_ID)
      );
    });

    it("should not retrieve fungible token's URI", async () => {
      await expectRevert(tokens.uri(INIT_FT_ID), TOKEN_EXIST_REVERT);
    });

    it("should not retrieve inexistent token's URI", async () => {
      await expectRevert(tokens.uri(INIT_NFT_ID), TOKEN_EXIST_REVERT);
    });
  });

  describe("mintFT", () => {
    const amount = 5;
    it("should successfully mint fungible token", async () => {
      for (let tid = INIT_FT_ID; tid < INIT_NFT_ID - 1; tid++) {
        expectEvent(
          await tokens.mintFT(alice, tid, amount + tid, "0x0"),
          "TransferSingle",
          {
            from: new BN(0),
            to: alice,
            id: new BN(tid),
            value: new BN(amount + tid),
          }
        );
        expect((await tokens.balanceOf(alice, tid)).toNumber()).to.equal(
          amount + tid
        );
      }
    });

    it("should not successfully mint fungible token with incorrect tid", async () => {
      await expectRevert(
        tokens.mintFT(alice, INIT_NFT_ID, amount, "0x0"),
        TOKEN_ID_INVALID_REVERT
      );
    });

    it("should not successfully mint fungible token with unauthorized operator", async () => {
      await expectRevert(
        tokens.mintFT(alice, INIT_FT_ID, amount, "0x0", {
          from: alice,
        }),
        OWNABLE_REVERT
      );
    });
  });

  describe("supply functions", () => {
    const amount = 10;
    it("should successfully get total supply", async () => {
      expect(
        (await tokens.totalSupply(await tokens.ADMIN())).toNumber()
      ).to.equal(1);

      await tokens.mintFT(alice, INIT_FT_ID, amount, "0x0");
      await tokens.mintFT(bob, INIT_FT_ID, amount + 1, "0x0");
      expect((await tokens.totalSupply(INIT_FT_ID)).toNumber()).to.equal(
        amount * 2 + 1 + 1
      );

      await tokens.mintNFT(alice, "0x0");
      expect((await tokens.totalSupply(INIT_NFT_ID)).toNumber()).to.equal(1);
    });

    it("should successfully check token existence", async () => {
      expect(await tokens.exists(await tokens.ADMIN())).to.be.true;

      expect(await tokens.exists(INIT_FT_ID + 1)).to.be.false;

      await tokens.mintFT(alice, INIT_FT_ID + 1, amount, "0x0");
      expect(await tokens.exists(INIT_FT_ID + 1)).to.be.true;

      expect(await tokens.exists(INIT_NFT_ID)).to.be.false;

      await tokens.mintNFT(alice, "0x0");
      expect(await tokens.exists(INIT_NFT_ID)).to.be.true;
    });
  });

  const getMetaURI = (tid) => {
    return (
      META_DATA_URI_PREFIX +
      new BN(tid).toString(16, 64) +
      META_DATA_URI_POSTFIX
    );
  };
});
