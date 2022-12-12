// SPDX-License-Identifier: MIT

pragma solidity ^0.8.12;

import "@openzeppelin/contracts/governance/extensions/GovernorTimelockControl.sol";
import "@openzeppelin/contracts/governance/extensions/GovernorCountingSimple.sol";
import "@openzeppelin/contracts/governance/extensions/GovernorSettings.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "./BCPediaDelegator.sol";

contract BCPediaGovernor is
    GovernorSettings,
    GovernorTimelockControl,
    GovernorCountingSimple,
    Ownable
{
    BCPediaDelegator public immutable delegator;
    BCPediaTokens public immutable tokens;

    uint256 public constant QUORUM = 5;
    mapping(uint256 => address[]) public forVoters;
    mapping(uint256 => address[]) public againstVoters;

    constructor(address _delegator)
        GovernorSettings(10 minutes / 12, 1 hours / 12, ~uint256(0))
        GovernorTimelockControl(
            new TimelockController(
                1 hours,
                new address[](0),
                new address[](0),
                address(this)
            )
        )
        Governor("BCPediaGovernor")
    {
        delegator = BCPediaDelegator(_delegator);
        tokens = delegator.tokens();

        TimelockController temp = TimelockController(payable(timelock()));
        temp.grantRole(temp.PROPOSER_ROLE(), address(this));
        temp.grantRole(temp.EXECUTOR_ROLE(), address(this));
        temp.renounceRole(temp.TIMELOCK_ADMIN_ROLE(), address(this));
    }

    function getVoters(uint256 pid, bool forVoter)
        external
        view
        returns (address[] memory)
    {
        if (forVoter) {
            return forVoters[pid];
        } else {
            return againstVoters[pid];
        }
    }

    function quorum(
        uint256 // blockNumber
    ) public view override returns (uint256) {
        uint256 totalValidators = tokens.totalSupply(tokens.CERTIFICATE());
        if (totalValidators < QUORUM * 10) {
            return totalValidators / 10 + 1;
        }
        return QUORUM;
    }

    function _getVotes(
        address account,
        uint256, // blockNumber
        bytes memory // params
    ) internal view override returns (uint256) {
        if (tokens.balanceOf(account, tokens.CERTIFICATE()) == 0) {
            return 0;
        }
        return tokens.balanceOf(account, tokens.LIKE());
    }

    function _cancel(
        address[] memory targets,
        uint256[] memory values,
        bytes[] memory calldatas,
        bytes32 descriptionHash
    ) internal override(GovernorTimelockControl, Governor) returns (uint256) {
        return super._cancel(targets, values, calldatas, descriptionHash);
    }

    function _execute(
        uint256 proposalId,
        address[] memory targets,
        uint256[] memory values,
        bytes[] memory calldatas,
        bytes32 descriptionHash
    ) internal override(GovernorTimelockControl, Governor) {
        // address[] memory actualTargets = new address[](targets.length + 1);
        // uint256[] memory actualValues = new uint256[](values.length + 1);
        // bytes[] memory actualCalldatas = new bytes[](calldatas.length + 1);

        // for (uint256 i = 0; i < targets.length; i++) {
        //     actualTargets[i] = targets[i];
        //     actualValues[i] = values[i];
        //     actualCalldatas[i] = calldatas[i];
        // }

        // for (uint256 i = targets.length; i < actualTargets.length; i++) {
        //     actualTargets[i] = address(delegator);
        //     values[i] = 0;
        // }
        // actualCalldatas[targets.length] = abi.encodeWithSelector(
        //     delegator.rewardBatch.selector,
        //     forVoters[proposalId],
        //     1
        // );

        // super._execute(
        //     proposalId,
        //     actualTargets,
        //     actualValues,
        //     actualCalldatas,
        //     descriptionHash
        // );
        super._execute(proposalId, targets, values, calldatas, descriptionHash);
    }

    function _countVote(
        uint256 proposalId,
        address account,
        uint8 support,
        uint256 weight,
        bytes memory // params
    ) internal override(GovernorCountingSimple, Governor) {
        if (
            support == uint8(VoteType.Against) || support == uint8(VoteType.For)
        ) {
            tokens.burn(account, tokens.BCPC(), 10);
        }

        super._countVote(
            proposalId,
            account,
            support,
            weight,
            _defaultParams()
        );

        if (support == uint8(VoteType.Against)) {
            againstVoters[proposalId].push(account);
        } else if (support == uint8(VoteType.For)) {
            forVoters[proposalId].push(account);
        }
    }

    function proposalThreshold()
        public
        view
        override(GovernorSettings, Governor)
        returns (uint256)
    {
        if (msg.sender == owner()) {
            return 0;
        }

        return ~uint256(0);
    }

    function _executor()
        internal
        view
        override(GovernorTimelockControl, Governor)
        returns (address)
    {
        return super._executor();
    }

    function state(uint256 proposalId)
        public
        view
        override(GovernorTimelockControl, Governor)
        returns (ProposalState)
    {
        return super.state(proposalId);
    }

    function supportsInterface(bytes4 interfaceId)
        public
        view
        override(GovernorTimelockControl, Governor)
        returns (bool)
    {
        return super.supportsInterface(interfaceId);
    }
}
