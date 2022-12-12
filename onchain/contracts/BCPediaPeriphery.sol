// SPDX-License-Identifier: MIT

pragma solidity ^0.8.12;

import "./BCPediaGovernor.sol";

contract BCPediaPeriphery {
    BCPediaTokens public immutable tokens;
    BCPediaGovernor public immutable governor;
    BCPediaDelegator public immutable delegator;

    // mapping(uint256 => bytes) governorCalldata;

    constructor(address payable _governor) {
        governor = BCPediaGovernor(_governor);
        delegator = governor.delegator();
        tokens = governor.tokens();
    }

    modifier isQualified() {
        require(tokens.balanceOf(msg.sender, tokens.BCPC()) > 5);
        _;
    }

    function createEntry(
        bytes32 h,
        string calldata category,
        string calldata keyword
    ) external isQualified returns (uint256) {
        require(!delegator.existenceCheck(0, category, keyword));

        return
            _batchAndCall(
                5,
                abi.encodeWithSelector(
                    delegator.createEntry.selector,
                    msg.sender,
                    h,
                    category,
                    keyword
                ),
                1,
                0
            );
    }

    function updateEntry(
        bytes32 h,
        string calldata category,
        string calldata keyword
    ) external isQualified returns (uint256) {
        uint256 tid = delegator.entries(category, keyword);
        require(delegator.existenceCheck(0, category, keyword));
        require(delegator.hashes(tid) != h);

        return
            _batchAndCall(
                5,
                abi.encodeWithSelector(
                    delegator.updateEntry.selector,
                    h,
                    category,
                    keyword
                ),
                1,
                0
            );
    }

    function disputeEntry(uint256 id) external isQualified returns (uint256) {
        return
            _batchAndCall(
                5,
                abi.encodeWithSelector(delegator.removeEntry.selector, id),
                1,
                0
            );
    }

    function proceedProposal(
        uint256 pid,
        uint8 method,
        bytes32 h,
        string calldata category,
        string calldata keyword,
        uint8 id
    ) external returns (bool) {
        IGovernor.ProposalState state = governor.state(pid);
        if (state == IGovernor.ProposalState.Succeeded) {
            _batchAndCall(
                5,
                _getAbiEncode(method, h, category, keyword, id),
                1,
                1
            );
            // require(governorCalldata[pid].length > 0);
            // address(governor).call{value: 0}(governorCalldata[pid]);
            // require(success, "Execution failed");
            return true;
        } else if (state == IGovernor.ProposalState.Defeated) {
            _rewardVoters(pid, false);
            return true;
        } else if (state == IGovernor.ProposalState.Queued) {
            _batchAndCall(
                5,
                _getAbiEncode(method, h, category, keyword, id),
                1,
                2
            );
            _rewardVoters(pid, true);
            return true;
        }

        return false;
    }

    function _rewardVoters(uint256 pid, bool forVoter) internal {
        delegator.rewardBatch(governor.getVoters(pid, forVoter), 11);
    }

    function _getAbiEncode(
        uint8 method,
        bytes32 h,
        string calldata category,
        string calldata keyword,
        uint8 id
    ) view internal returns (bytes memory) {
        if (method == 0) {
            return
                abi.encodeWithSelector(
                    delegator.createEntry.selector,
                    msg.sender,
                    h,
                    category,
                    keyword
                );
        } else if (method == 1) {
            return
                abi.encodeWithSelector(
                    delegator.updateEntry.selector,
                    h,
                    category,
                    keyword
                );
        } else {
            return abi.encodeWithSelector(delegator.removeEntry.selector, id);
        }
    }

    event PID(uint256 id);

    function _batchAndCall(
        uint256 stake,
        bytes memory data,
        uint256 reward,
        uint8 method
    ) internal returns (uint256) {
        if (method == 0) {
            tokens.burn(msg.sender, tokens.BCPC(), stake);
        }

        address[] memory targets = new address[](2);
        uint256[] memory values = new uint256[](2);
        bytes[] memory calldatas = new bytes[](2);
        for (uint256 i = 0; i < 2; i++) {
            targets[i] = address(delegator);
            values[i] = 0;
        }
        calldatas[0] = data;
        calldatas[1] = abi.encodeWithSelector(
            delegator.reward.selector,
            msg.sender,
            reward + stake
        );
        uint256 pid = 0;
        if (method == 0) {
            pid = governor.propose(targets, values, calldatas, "");
        } else if (method == 1) {
            pid = governor.queue(
                targets,
                values,
                calldatas,
                keccak256(bytes(""))
            );
        } else {
            pid = governor.execute(
                targets,
                values,
                calldatas,
                keccak256(bytes(""))
            );
        }
        // governorCalldata[pid] = abi.encodeWithSelector(
        //     governor.queue.selector,
        //     targets,
        //     values,
        //     calldatas,
        //     keccak256(bytes(""))
        // );
        return pid;
    }
}
