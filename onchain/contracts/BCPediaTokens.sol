// SPDX-License-Identifier: MIT

pragma solidity ^0.8.12;

import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155Supply.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155Burnable.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract BCPediaTokens is ERC1155Supply, ERC1155Burnable, Ownable {
    using Counters for Counters.Counter;
    uint8 public constant LIKE = 0;
    uint8 public constant BCPC = 1;
    uint8 public constant CERTIFICATE = 2;
    uint8 public constant ADMIN = 3;
    Counters.Counter private entry;
    mapping(uint256 => address) _owners;

    constructor() ERC1155("https://portfolio-of.me/entries/") {
        while (entry.current() < ADMIN) {
            entry.increment();
        }
        _mint(msg.sender, ADMIN, 1, hex"00");
        _mint(msg.sender, CERTIFICATE, 1, hex"00");
        _mint(msg.sender, LIKE, 1, hex"00");
    }

    function buyCoin(uint256 amount) external payable {
        require(msg.value == 5 * amount);
        _mint(msg.sender, BCPC, amount, "");
    }

    function mintNFT(address account, bytes calldata data)
        external
        onlyOwner
        returns (uint256)
    {
        entry.increment();
        _mint(account, entry.current(), 1, data);

        return entry.current();
    }

    function burnNFT(address account, uint256 id) external onlyOwner {
        require(id > ADMIN);
        _burn(account, id, 1);
    }

    function ownerOf(uint256 tokenId) public view returns (address) {
        require(_owners[tokenId] != address(0));
        return _owners[tokenId];
    }

    function mintFT(
        address account,
        uint256 tokenId,
        uint256 amount,
        bytes calldata data
    ) external onlyOwner {
        require(tokenId <= CERTIFICATE, "Invalid FT id");
        _mint(account, tokenId, amount, data);
    }

    // TODO: improved id transform method
    function uri(uint256 id) public view override returns (string memory) {
        require(id > ADMIN && exists(id), "NFT doesn't exist");
        return string.concat(super.uri(id), _toHexString(id, 32), ".json");
    }

    function _beforeTokenTransfer(
        address operator,
        address from,
        address to,
        uint256[] memory ids,
        uint256[] memory amounts,
        bytes memory data
    ) internal override(ERC1155, ERC1155Supply) {
        super._beforeTokenTransfer(operator, from, to, ids, amounts, data);
        for (uint256 i = 0; i < ids.length; i++) {
            if (ids[i] < ADMIN) {
                continue;
            }
            _owners[ids[i]] = to;
        }
    }

    function _toHexString(uint256 value, uint256 length)
        internal
        pure
        returns (string memory)
    {
        bytes16 _SYMBOLS = "0123456789abcdef";
        bytes memory buffer = new bytes(2 * length);
        for (uint256 i = 2 * length; i > 0; --i) {
            buffer[i - 1] = _SYMBOLS[value & 0xf];
            value >>= 4;
        }
        require(value == 0, "Strings: hex length insufficient");
        return string(buffer);
    }
}
