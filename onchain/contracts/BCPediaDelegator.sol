// SPDX-License-Identifier: MIT

pragma solidity ^0.8.12;

import "@openzeppelin/contracts/access/Ownable.sol";
import "./BCPediaTokens.sol";

contract BCPediaDelegator is Ownable {
    BCPediaTokens public immutable tokens;
    
    address private periphery;
    uint32 public constant VALIDATOR_THRESHOLD = 1000;
    mapping(string => mapping(string => uint256)) public entries;
    mapping(uint256 => bytes32) public hashes;
    mapping(uint256 => uint256) public likes;

    constructor(address _tokens) {
        periphery = address(0);
        tokens = BCPediaTokens(_tokens);
    }

    function createEntry(
        address owner,
        bytes32 h,
        string calldata category,
        string calldata keyword
    ) external onlyOwner {
        require(!existenceCheck(0, category, keyword));
        uint256 tid = tokens.mintNFT(owner, hex"00");
        entries[category][keyword] = tid;
        hashes[tid] = h;
    }

    function updateEntry(
        bytes32 h,
        string calldata category,
        string calldata keyword
    ) external onlyOwner {
        uint256 tid = entries[category][keyword];
        require(existenceCheck(tid, category, keyword));
        require(hashes[tid] != h);
        hashes[tid] = h;
    }

    function removeEntry(uint256 id) external onlyOwner {
        delete hashes[id];
        tokens.burnNFT(tokens.ownerOf(id), id);
    }

    function batchUpdateLikes(
        uint256[] calldata ids,
        uint256[] calldata increment
    ) external {
        require(tokens.balanceOf(msg.sender, tokens.ADMIN()) > 0);
        require(ids.length == increment.length);
        for (uint256 i = 0; i < ids.length; i++) {
            require(hashes[ids[i]] != bytes32(0));
            likes[ids[i]] += increment[i];
            address account = tokens.ownerOf(ids[i]);
            tokens.mintFT(account, tokens.LIKE(), increment[i], hex"00");
        }
    }

    function reward(address account, uint256 amount) external onlyOwner {
        tokens.mintFT(account, tokens.BCPC(), amount, hex"00");
    }

    function rewardBatch(address[] calldata accounts, uint256 amounts)
        external
    {
        require(msg.sender == periphery);
        for (uint256 i = 0; i < accounts.length; i++) {
            tokens.mintFT(accounts[i], tokens.BCPC(), amounts, hex"00");
        }
    }

    function requestPromote() external {
        require(
            tokens.balanceOf(msg.sender, tokens.LIKE()) > VALIDATOR_THRESHOLD
        );
        require(tokens.balanceOf(msg.sender, tokens.CERTIFICATE()) == 0);
        tokens.burn(msg.sender, tokens.BCPC(), 10);
        tokens.mintFT(msg.sender, tokens.CERTIFICATE(), 1, "");
    }

    function resign() external {
        tokens.burn(msg.sender, tokens.CERTIFICATE(), 1);
    }

    function setPeriphery(address _periphery) external onlyOwner {
        if (periphery == address(0)) {
            periphery = _periphery;
        }
    }

    function existenceCheck(
        uint256 id,
        string calldata category,
        string calldata keyword
    ) public returns (bool) {
        if (id == 0) {
            id = entries[category][keyword];
        }
        if (id == 0) {
            return false;
        }
        if (hashes[id] != bytes32(0)) {
            return true;
        }
        entries[category][keyword] = 0;

        return false;
    }
}
