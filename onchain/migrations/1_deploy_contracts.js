const BCPediaTokens = artifacts.require("./BCPediaTokens");
const BCPediaDelegator = artifacts.require("./BCPediaDelegator");
const BCPediaGovernor = artifacts.require("./BCPediaGovernor");
const BCPediaPeriphery = artifacts.require("./BCPediaPeriphery");

module.exports = async function (deployer) {
    await deployer.deploy(BCPediaTokens);
    await deployer.deploy(BCPediaDelegator, BCPediaTokens.address);
    await deployer.deploy(BCPediaGovernor, BCPediaDelegator.address);
    await deployer.deploy(BCPediaPeriphery, BCPediaGovernor.address);
    
    let tokens = await BCPediaTokens.at(BCPediaTokens.address);
    let delegator = await BCPediaDelegator.at(BCPediaDelegator.address);
    let governor = await BCPediaGovernor.at(BCPediaGovernor.address);

    await tokens.transferOwnership(BCPediaDelegator.address);
    await delegator.setPeriphery(BCPediaPeriphery.address);
    await delegator.transferOwnership(await governor.timelock());
    await governor.transferOwnership(BCPediaPeriphery.address);
};