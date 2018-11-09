package com.networknt.taiji.crypto;

import com.networknt.config.Config;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class TransactionManagerTest {

    @Test
    public void testVerify() throws Exception {
        String s = "{\"currency\":\"taiji\",\"d\":[{\"0000344d315050CA9B0938B6511EC342705a1f9c\":\"+F6UAABQ8TS4sNs9nMP2yyYtTURF7iiEBfXhAIAboHMG81J1ViyyG55TpucTZ8SN7O2UJ4dTyyWZF2NGGkCwoHVixWu0T1GByN8E4YPDkKiCj7ubnUFBjKNFUJgVjUYm\"}],\"c\":[{\"000050f134b8B0Db3D9Cc3F6cB262D4d4445Ee28\":\"+F6UAABQ8TS4sNs9nMP2yyYtTURF7iiEBfXhAIAboHMG81J1ViyyG55TpucTZ8SN7O2UJ4dTyyWZF2NGGkCwoHVixWu0T1GByN8E4YPDkKiCj7ubnUFBjKNFUJgVjUYm\"}]}";
        SignedTransaction stx = Config.getInstance().getMapper().readValue(s, SignedTransaction.class);

        TxVerifyResult result = TransactionManager.verifyTransaction(stx, "0000");
        Assert.assertNull(result.getError());
    }

    @Test
    public void testWrongBankId() throws Exception {
        String s = "{\"currency\":\"taiji\",\"d\":[{\"0000344d315050CA9B0938B6511EC342705a1f9c\":\"+F6UAABQ8TS4sNs9nMP2yyYtTURF7iiEBfXhAIAboHMG81J1ViyyG55TpucTZ8SN7O2UJ4dTyyWZF2NGGkCwoHVixWu0T1GByN8E4YPDkKiCj7ubnUFBjKNFUJgVjUYm\"}],\"c\":[{\"000050f134b8B0Db3D9Cc3F6cB262D4d4445Ee28\":\"+F6UAABQ8TS4sNs9nMP2yyYtTURF7iiEBfXhAIAboHMG81J1ViyyG55TpucTZ8SN7O2UJ4dTyyWZF2NGGkCwoHVixWu0T1GByN8E4YPDkKiCj7ubnUFBjKNFUJgVjUYm\"}]}";
        SignedTransaction stx = Config.getInstance().getMapper().readValue(s, SignedTransaction.class);

        TxVerifyResult result = TransactionManager.verifyTransaction(stx, "0001");
        Assert.assertNotNull(result.getError());
    }

}
