package com.networknt.taiji.console;

import com.networknt.chain.utility.Console;
import com.networknt.monad.Result;
import com.networknt.status.Status;
import com.networknt.taiji.avro.AvroSerializer;
import com.networknt.taiji.client.TaijiClient;
import com.networknt.taiji.crypto.*;
import com.networknt.taiji.event.EventId;
import com.networknt.taiji.token.TokenApprovedEvent;
import com.networknt.taiji.token.TokenWithdrewEvent;
import com.networknt.taiji.utility.Converter;

import java.util.List;
import java.util.Map;

import static com.networknt.chain.utility.Console.exitError;

public class TokenWithdrawal extends TokenManager {
    public TokenWithdrawal() {
    }

    public TokenWithdrawal(IODevice console) {
        super(console);
    }

    public static void main(String[] args) {
        new TokenWithdrawal().run();
    }

    static void main(IODevice console) {
        new TokenWithdrawal(console).run();
    }

    private void run() {
        String currency = getCurrency();
        String ownerAddress = getOwnerAddress();
        Credentials credentials = loadWalletFromAddress(ownerAddress);
        console.printf("Wallet for address " + credentials.getAddress() + " loaded\n");
        String tokenAddress = getTokenAddress();
        String withdrawFromAddress = getWithdrawFromAddress();

        Long l = getWithdrawnAmount();
        Result<Map<String, Object>> tokenInfoResult = TaijiClient.getTokenInfoByAddress(tokenAddress);
        Map<String, Object> tokenInfo = null;
        if(tokenInfoResult.isSuccess()) {
            tokenInfo = tokenInfoResult.getResult();
        } else {
            exitError(tokenInfoResult.getError().toString());
        }
        int decimals = (Integer)tokenInfo.get("decimals");
        long factor = Converter.power(10, decimals);
        long total = l * factor;

        // get number of transactions from the chain-reader to generate eventId.
        long nonce = 0;
        Result<List<SignedLedgerEntry>> result = TaijiClient.getTransaction(ownerAddress, currency);
        if(result.isSuccess()) {
            nonce = result.getResult().size();
        } else {
            exitError(result.getError().toString());
        }

        EventId eventId = EventId.newBuilder()
                .setAddress(ownerAddress)
                .setNonce(nonce)
                .build();

        TokenWithdrewEvent tokenWithdrewEvent = new TokenWithdrewEvent(eventId, tokenAddress, withdrawFromAddress, total);

        AvroSerializer serializer = new AvroSerializer();
        byte[] bytes = serializer.serialize(tokenWithdrewEvent);

        // here we just create a credit entry only on with the toAddress the token address and value 0
        LedgerEntry creditEntry = new LedgerEntry(tokenAddress, 0, bytes);
        RawTransaction rtx = new RawTransaction(currency);
        rtx.addCreditEntry(tokenAddress, creditEntry);
        // calculate the fee for the debit entry
        Result<Fee> feeResult = TaijiClient.getFee(ownerAddress, currency);
        Fee fee = null;
        if(feeResult.isSuccess()) {
            fee = feeResult.getResult();
        } else {
            exitError(feeResult.getError().toString());
        }
        LedgerEntry feeEntry = new LedgerEntry(fee.getBankAddress(), fee.getApplication());
        rtx.addCreditEntry(fee.getBankAddress(), feeEntry);
        rtx.addDebitEntry(ownerAddress, feeEntry);
        SignedTransaction stx = TransactionManager.signTransaction(rtx, credentials);

        Status status = TaijiClient.postTx(credentials.getAddress().substring(0, 4), stx);
        if(status != null && status.getStatusCode() == 200) {
            Console.exitSuccess((String.format("Token has been withdrawn successfully for owner address %s, token address %s from withdrawn address %s with status %s%n",
                    ownerAddress,
                    tokenAddress,
                    withdrawFromAddress,
                    status.toString())));
        } else {
            if(status == null) {
                Console.exitError("Nothing returned from the API call, check connectivity");
            } else {
                Console.exitError(status.toString());
            }
        }
    }
}
