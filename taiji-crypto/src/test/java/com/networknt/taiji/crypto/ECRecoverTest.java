package com.networknt.taiji.crypto;

import com.networknt.chain.utility.Hash;
import com.networknt.chain.utility.Numeric;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ECRecoverTest {

    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Taiji Signed Message:\n";

    @Disabled
    @Test
    public void testRecoverAddressFromSignature() {
        //CHECKSTYLE:OFF
        String signature = "0x2c6401216c9031b9a6fb8cbfccab4fcec6c951cdf40e2320108d1856eb532250576865fbcd452bcdc4c57321b619ed7a9cfd38bd973c3e1e0243ac2777fe9d5b1b";
        //CHECKSTYLE:ON
        String address = "31b26e43651e9371c88af3d36c14cfd938baf4fd";
        String message = "v0G9u7huK4mJb2K1";
                
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        byte[] msgHash = Hash.sha3((prefix + message).getBytes());

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) { 
            v += 27; 
        }
           
        Sign.SignatureData sd = new Sign.SignatureData(
                v, 
                (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32), 
                (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = null;
        boolean match = false;
        
        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i, 
                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())), 
                    msgHash);
               
            if (publicKey != null) {
                addressRecovered = Keys.getAddress(publicKey);
                System.out.println("address recovered = " + addressRecovered);
                System.out.println("address expected  = " + address);
                if (addressRecovered.equals(address)) {
                    match = true;
                    break;
                }
            }
        }
        
        assertThat(addressRecovered, is(address));
        assertTrue(match);
    }
}
