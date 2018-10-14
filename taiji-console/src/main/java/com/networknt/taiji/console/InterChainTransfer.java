package com.networknt.taiji.console;

import com.networknt.config.Config;
import com.networknt.taiji.client.TaijiClient;
import com.networknt.taiji.crypto.*;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.web3j.codegen.Console.exitError;

public class InterChainTransfer extends WalletManager {
    public static String password = "123456";
    public static String wallet1 = "01b928ccc352743b98aba5ef919e0f8731db47d2.json";
    public static String wallet2 = "02fb109a4d091056d811ae022cb8c2a78f050c33.json";
    public static String wallet3 = "0359c5ff6b1f816e8f7ff9e90c8e5abd34927058.json";

    private static final String USAGE = "transfer <1-1|1-N> <times>";

    public static void main(String[] args) {
        if (args.length != 2) {
            exitError(USAGE);
        } else {
            new InterChainTransfer().run(args[0], args[1]);
        }
    }

    private void run(String mode, String times) {
        // init credentials for three accounts.
        List<Credentials> credentialsList = new ArrayList<>();
        credentialsList.add(getCredentials(password, Config.getInstance().getInputStreamFromFile(wallet1)));
        credentialsList.add(getCredentials(password, Config.getInstance().getInputStreamFromFile(wallet2)));
        credentialsList.add(getCredentials(password, Config.getInstance().getInputStreamFromFile(wallet3)));
        Integer i = Integer.valueOf(times);
        switch(mode) {
            case "1-1":
                oneToOne(credentialsList, Convert.toWei("1", Convert.Unit.ETHER).toBigIntegerExact(), i);
                break;
            case "1-N":
                oneToN(credentialsList, Convert.toWei("2", Convert.Unit.ETHER).toBigIntegerExact(), i);
                break;
            default:
                exitError("Invalid transfer mode. Only 1-1 or 1-N or N-1 is supported");

        }
    }

    private void oneToOne(List<Credentials> list, BigInteger value, int times) {
        for(int i = 0; i < times; i++) {
            Collections.shuffle(list);
            // transfer from first account to the second account in the list.
            LedgerEntry ledgerEntry = new LedgerEntry(list.get(1).getAddress(), value);
            RawTransaction rtx = new RawTransaction();
            rtx.addCreditEntry(list.get(1).getAddress(), ledgerEntry);
            rtx.addDebitEntry(list.get(0).getAddress(), ledgerEntry);
            SignedTransaction stx = TransactionManager.signTransaction(rtx, list.get(0));
            TransactionReceipt transactionReceipt = TaijiClient.postTx(stx);

        }
    }

    private void oneToN(List<Credentials> list, BigInteger value, int times) {
        for(int i = 0; i < times; i++) {
            Collections.shuffle(list);
            // transfer from the first account to second and third accounts.
            LedgerEntry debit = new LedgerEntry(list.get(1).getAddress(), value);

            LedgerEntry credit1 = new LedgerEntry(list.get(1).getAddress(), value.divide(new BigInteger("2")));
            LedgerEntry credit2 = new LedgerEntry(list.get(2).getAddress(), value.divide(new BigInteger("2")));

            RawTransaction rtx = new RawTransaction();
            rtx.addCreditEntry(list.get(1).getAddress(), credit1);
            rtx.addCreditEntry(list.get(2).getAddress(), credit2);
            rtx.addDebitEntry(list.get(0).getAddress(), debit);
            SignedTransaction stx = TransactionManager.signTransaction(rtx, list.get(0));
            TransactionReceipt transactionReceipt = TaijiClient.postTx(stx);
        }
    }
}
