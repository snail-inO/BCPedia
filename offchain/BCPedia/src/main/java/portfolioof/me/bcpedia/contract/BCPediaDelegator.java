package portfolioof.me.bcpedia.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.*;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class BCPediaDelegator extends Contract {
    public static final String BINARY = "0x60a060405234801561001057600080fd5b50604051611a47380380611a4783398101604081905261002f916100a9565b61003833610059565b600180546001600160a01b03191690556001600160a01b03166080526100d9565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b6000602082840312156100bb57600080fd5b81516001600160a01b03811681146100d257600080fd5b9392505050565b6080516118c06101876000396000818161024a015281816102f901528181610328015281816104d501528181610576015281816105a6015281816106b4015281816106e4015281816107dd0152818161083301528181610ab201528181610b8a01528181610bba01528181610cf501528181610d2401528181610e2001528181610e4f01528181610f4b01528181610f7b0152818161106501528181611095015261120801526118c06000f3fe608060405234801561001057600080fd5b50600436106101165760003560e01c80637a6eea37116100a2578063aeb2293411610071578063aeb229341461026c578063c94a858c1461027f578063d18136d9146102c7578063f2fde38b146102cf578063ffdab909146102e257600080fd5b80637a6eea37146101e25780637e9dffe6146102005780638da5cb5b146102205780639d63848a1461024557600080fd5b80634f92d3e6116100e95780634f92d3e6146101695780635018408614610191578063501895ae146101a457806369652fcf146101d2578063715018a6146101da57600080fd5b806308d4d9701461011b57806321670f221461013057806324a2a60a146101435780634863275914610156575b600080fd5b61012e6101293660046113d5565b6102f5565b005b61012e61013e366004611456565b6106aa565b61012e610151366004611482565b6107b9565b61012e610164366004611510565b61091d565b61017c610177366004611510565b6109b1565b60405190151581526020015b60405180910390f35b61012e61019f36600461158a565b610a78565b6101c46101b2366004611616565b60036020526000908152604090205481565b604051908152602001610188565b61012e610b88565b61012e610c9f565b6101eb61271081565b60405163ffffffff9091168152602001610188565b6101c461020e366004611616565b60046020526000908152604090205481565b6000546001600160a01b03165b6040516001600160a01b039091168152602001610188565b61022d7f000000000000000000000000000000000000000000000000000000000000000081565b61012e61027a36600461162f565b610cb3565b6101c461028d3660046116f6565b8151602081840181018051600282529282019482019490942091909352815180830184018051928152908401929093019190912091525481565b61012e610cea565b61012e6102dd36600461162f565b61115e565b61012e6102f0366004611616565b6111d9565b60007f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031662fdd58e337f00000000000000000000000000000000000000000000000000000000000000006001600160a01b0316632a0acc6a6040518163ffffffff1660e01b8152600401602060405180830381865afa158015610384573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906103a8919061175a565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260ff166024820152604401602060405180830381865afa1580156103f4573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610418919061177d565b1161042257600080fd5b82811461042e57600080fd5b60005b838110156106a357600060038187878581811061045057610450611796565b905060200201358152602001908152602001600020540361047057600080fd5b82828281811061048257610482611796565b905060200201356004600087878581811061049f5761049f611796565b90506020020135815260200190815260200160002060008282546104c391906117c2565b90915550600090506001600160a01b037f000000000000000000000000000000000000000000000000000000000000000016636352211e87878581811061050c5761050c611796565b905060200201356040518263ffffffff1660e01b815260040161053191815260200190565b602060405180830381865afa15801561054e573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061057291906117da565b90507f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663b05c4ef5827f00000000000000000000000000000000000000000000000000000000000000006001600160a01b0316637f49d9746040518163ffffffff1660e01b8152600401602060405180830381865afa158015610602573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610626919061175a565b87878781811061063857610638611796565b905060200201356040518463ffffffff1660e01b815260040161065d939291906117f7565b600060405180830381600087803b15801561067757600080fd5b505af115801561068b573d6000803e3d6000fd5b5050505050808061069b90611834565b915050610431565b5050505050565b6106b26112df565b7f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663b05c4ef5837f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663b6e4efa16040518163ffffffff1660e01b8152600401602060405180830381865afa158015610740573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610764919061175a565b846040518463ffffffff1660e01b8152600401610783939291906117f7565b600060405180830381600087803b15801561079d57600080fd5b505af11580156107b1573d6000803e3d6000fd5b505050505050565b6001546001600160a01b031633146107d057600080fd5b60005b82811015610917577f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663b05c4ef585858481811061081c5761081c611796565b9050602002016020810190610831919061162f565b7f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663b6e4efa16040518163ffffffff1660e01b8152600401602060405180830381865afa15801561088f573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906108b3919061175a565b856040518463ffffffff1660e01b81526004016108d2939291906117f7565b600060405180830381600087803b1580156108ec57600080fd5b505af1158015610900573d6000803e3d6000fd5b50505050808061090f90611834565b9150506107d3565b50505050565b6109256112df565b60006002858560405161093992919061184d565b9081526020016040518091039020838360405161095792919061184d565b908152602001604051809103902054905061097581868686866109b1565b61097e57600080fd5b60008181526003602052604090205486900361099957600080fd5b60009081526003602052604090209490945550505050565b6000856000036109fd57600285856040516109cd92919061184d565b908152602001604051809103902083836040516109eb92919061184d565b90815260200160405180910390205495505b85600003610a0d57506000610a6f565b60008681526003602052604090205415610a2957506001610a6f565b600060028686604051610a3d92919061184d565b90815260200160405180910390208484604051610a5b92919061184d565b908152604051908190036020019020555060005b95945050505050565b610a806112df565b610a8e6000858585856109b1565b15610a9857600080fd5b60405163e83b022760e01b81526000906001600160a01b037f0000000000000000000000000000000000000000000000000000000000000000169063e83b022790610ae7908a9060040161185d565b6020604051808303816000875af1158015610b06573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610b2a919061177d565b90508060028686604051610b3f92919061184d565b90815260200160405180910390208484604051610b5d92919061184d565b9081526040805160209281900383019020929092556000928352600390529020949094555050505050565b7f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663f5298aca337f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663245309186040518163ffffffff1660e01b8152600401602060405180830381865afa158015610c16573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610c3a919061175a565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260ff166024820152600160448201526064015b600060405180830381600087803b158015610c8b57600080fd5b505af1158015610917573d6000803e3d6000fd5b610ca76112df565b610cb16000611339565b565b610cbb6112df565b6001546001600160a01b0316610ce757600180546001600160a01b0319166001600160a01b0383161790555b50565b61271063ffffffff167f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031662fdd58e337f00000000000000000000000000000000000000000000000000000000000000006001600160a01b0316637f49d9746040518163ffffffff1660e01b8152600401602060405180830381865afa158015610d80573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610da4919061175a565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260ff166024820152604401602060405180830381865afa158015610df0573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610e14919061177d565b11610e1e57600080fd5b7f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031662fdd58e337f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663245309186040518163ffffffff1660e01b8152600401602060405180830381865afa158015610eab573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610ecf919061175a565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260ff166024820152604401602060405180830381865afa158015610f1b573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610f3f919061177d565b15610f4957600080fd5b7f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663f5298aca337f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663b6e4efa16040518163ffffffff1660e01b8152600401602060405180830381865afa158015610fd7573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610ffb919061175a565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260ff166024820152600a6044820152606401600060405180830381600087803b15801561104b57600080fd5b505af115801561105f573d6000803e3d6000fd5b505050507f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663b05c4ef5337f00000000000000000000000000000000000000000000000000000000000000006001600160a01b031663245309186040518163ffffffff1660e01b8152600401602060405180830381865afa1580156110f1573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190611115919061175a565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260ff16602482015260016044820152608060648201526000608482015260a401610c71565b6111666112df565b6001600160a01b0381166111d05760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b60648201526084015b60405180910390fd5b610ce781611339565b6111e16112df565b60008181526003602052604080822091909155516331a9108f60e11b8152600481018290527f00000000000000000000000000000000000000000000000000000000000000006001600160a01b0316906347fcf018908290636352211e90602401602060405180830381865afa15801561125f573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061128391906117da565b6040516001600160e01b031960e084901b1681526001600160a01b03909116600482015260248101849052604401600060405180830381600087803b1580156112cb57600080fd5b505af11580156106a3573d6000803e3d6000fd5b6000546001600160a01b03163314610cb15760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657260448201526064016111c7565b600080546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b60008083601f84011261139b57600080fd5b50813567ffffffffffffffff8111156113b357600080fd5b6020830191508360208260051b85010111156113ce57600080fd5b9250929050565b600080600080604085870312156113eb57600080fd5b843567ffffffffffffffff8082111561140357600080fd5b61140f88838901611389565b9096509450602087013591508082111561142857600080fd5b5061143587828801611389565b95989497509550505050565b6001600160a01b0381168114610ce757600080fd5b6000806040838503121561146957600080fd5b823561147481611441565b946020939093013593505050565b60008060006040848603121561149757600080fd5b833567ffffffffffffffff8111156114ae57600080fd5b6114ba86828701611389565b909790965060209590950135949350505050565b60008083601f8401126114e057600080fd5b50813567ffffffffffffffff8111156114f857600080fd5b6020830191508360208285010111156113ce57600080fd5b60008060008060006060868803121561152857600080fd5b85359450602086013567ffffffffffffffff8082111561154757600080fd5b61155389838a016114ce565b9096509450604088013591508082111561156c57600080fd5b50611579888289016114ce565b969995985093965092949392505050565b600080600080600080608087890312156115a357600080fd5b86356115ae81611441565b955060208701359450604087013567ffffffffffffffff808211156115d257600080fd5b6115de8a838b016114ce565b909650945060608901359150808211156115f757600080fd5b5061160489828a016114ce565b979a9699509497509295939492505050565b60006020828403121561162857600080fd5b5035919050565b60006020828403121561164157600080fd5b813561164c81611441565b9392505050565b634e487b7160e01b600052604160045260246000fd5b600082601f83011261167a57600080fd5b813567ffffffffffffffff8082111561169557611695611653565b604051601f8301601f19908116603f011681019082821181831017156116bd576116bd611653565b816040528381528660208588010111156116d657600080fd5b836020870160208301376000602085830101528094505050505092915050565b6000806040838503121561170957600080fd5b823567ffffffffffffffff8082111561172157600080fd5b61172d86838701611669565b9350602085013591508082111561174357600080fd5b5061175085828601611669565b9150509250929050565b60006020828403121561176c57600080fd5b815160ff8116811461164c57600080fd5b60006020828403121561178f57600080fd5b5051919050565b634e487b7160e01b600052603260045260246000fd5b634e487b7160e01b600052601160045260246000fd5b600082198211156117d5576117d56117ac565b500190565b6000602082840312156117ec57600080fd5b815161164c81611441565b6001600160a01b038416815260ff8316602082015260408101829052608060608201819052600190820152600060a0820181905260c08201610a6f565b600060018201611846576118466117ac565b5060010190565b8183823760009101908152919050565b6001600160a01b03821681526040602082018190526001908201526000606082018190526080820161164c56fea2646970667358221220f60344bdd71f48b27b92efe57185033e87aab05dc23ad32b0fa3c3f0e9498ced64736f6c634300080f0033";

    public static final String FUNC_VALIDATOR_THRESHOLD = "VALIDATOR_THRESHOLD";

    public static final String FUNC_ENTRIES = "entries";

    public static final String FUNC_HASHES = "hashes";

    public static final String FUNC_LIKES = "likes";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_TOKENS = "tokens";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_CREATEENTRY = "createEntry";

    public static final String FUNC_UPDATEENTRY = "updateEntry";

    public static final String FUNC_REMOVEENTRY = "removeEntry";

    public static final String FUNC_BATCHUPDATELIKES = "batchUpdateLikes";

    public static final String FUNC_REWARD = "reward";

    public static final String FUNC_REWARDBATCH = "rewardBatch";

    public static final String FUNC_REQUESTPROMOTE = "requestPromote";

    public static final String FUNC_RESIGN = "resign";

    public static final String FUNC_SETPERIPHERY = "setPeriphery";

    public static final String FUNC_EXISTENCECHECK = "existenceCheck";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("1670666541047", "0x541327312a98eF25d17b8A608966Ab9dE85bb436");
        _addresses.put("1670705727132", "0x4C85F947dc12A8EC8D736916E10a17B165b3e175");
        _addresses.put("1670731904404", "0x54Cdf16E95a7a5afE2AE313D13f7b39eA403Aee9");
        _addresses.put("1670666661659", "0xd863914BB1908bA954d70a87b458F3DCC447fDeC");
        _addresses.put("1670722417924", "0xD99DDE1749E0406C6110C053Be0D8d331f4bf816");
        _addresses.put("1670661131352", "0xD27dE8eB22539a190E8043B5D151551ACe187929");
        _addresses.put("1670705578449", "0x61fe57Fc65657ccf1938ab5CFA78672Bb422cd0a");
    }

    @Deprecated
    protected BCPediaDelegator(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BCPediaDelegator(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BCPediaDelegator(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BCPediaDelegator(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = new ArrayList<>();
        transactionReceipt.getLogs().forEach(log -> valueList.add(staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log)));
//        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    @Deprecated
    public static BCPediaDelegator load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BCPediaDelegator(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BCPediaDelegator load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BCPediaDelegator(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BCPediaDelegator load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new BCPediaDelegator(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BCPediaDelegator load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BCPediaDelegator(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BCPediaDelegator> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _tokens) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_tokens)));
        return deployRemoteCall(BCPediaDelegator.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<BCPediaDelegator> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _tokens) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_tokens)));
        return deployRemoteCall(BCPediaDelegator.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<BCPediaDelegator> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _tokens) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_tokens)));
        return deployRemoteCall(BCPediaDelegator.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<BCPediaDelegator> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _tokens) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_tokens)));
        return deployRemoteCall(BCPediaDelegator.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> VALIDATOR_THRESHOLD() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VALIDATOR_THRESHOLD,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> entries(String param0, String param1) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ENTRIES,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0),
                        new org.web3j.abi.datatypes.Utf8String(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<byte[]> hashes(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_HASHES,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<BigInteger> likes(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_LIKES,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> tokens() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOKENS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createEntry(String owner, byte[] h, String category, String keyword) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEENTRY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner),
                        new org.web3j.abi.datatypes.generated.Bytes32(h),
                        new org.web3j.abi.datatypes.Utf8String(category),
                        new org.web3j.abi.datatypes.Utf8String(keyword)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateEntry(byte[] h, String category, String keyword) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPDATEENTRY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(h),
                        new org.web3j.abi.datatypes.Utf8String(category),
                        new org.web3j.abi.datatypes.Utf8String(keyword)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeEntry(BigInteger id) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEENTRY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(id)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> batchUpdateLikes(List<BigInteger> ids, List<BigInteger> increment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BATCHUPDATELIKES,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(ids, org.web3j.abi.datatypes.generated.Uint256.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(increment, org.web3j.abi.datatypes.generated.Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> reward(String account, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REWARD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> rewardBatch(List<String> accounts, BigInteger amounts) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REWARDBATCH,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.datatypes.Address.class,
                                org.web3j.abi.Utils.typeMap(accounts, org.web3j.abi.datatypes.Address.class)),
                        new org.web3j.abi.datatypes.generated.Uint256(amounts)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> requestPromote() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REQUESTPROMOTE,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> resign() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RESIGN,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setPeriphery(String _periphery) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETPERIPHERY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_periphery)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> existenceCheck(BigInteger id, String category, String keyword) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_EXISTENCECHECK,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(id),
                        new org.web3j.abi.datatypes.Utf8String(category),
                        new org.web3j.abi.datatypes.Utf8String(keyword)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
