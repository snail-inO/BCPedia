package portfolioof.me.bcpedia.Service;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import portfolioof.me.bcpedia.contract.BCPediaTokens;

import java.math.BigInteger;

public class TokensServiceImpl {
    private static BCPediaTokens tokens = null;

    private TokensServiceImpl(String address, String publicKey, String privateKey) {
        Web3j web3 = Web3j.build(new HttpService());
        Credentials credentials = Credentials.create(privateKey, publicKey);
        tokens = BCPediaTokens.load(address, web3, credentials, new DefaultGasProvider());
    }

    public static void init(String address, String publicKey, String privateKey) {
        new TokensServiceImpl(address, publicKey, privateKey);
    }

    public static String balanceOfBCPC(String account) throws Exception {
        BigInteger balance = tokens.balanceOf(account, BigInteger.ZERO).send();
        return balance.toString();
    }
}
