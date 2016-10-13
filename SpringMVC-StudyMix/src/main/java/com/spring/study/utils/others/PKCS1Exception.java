package com.spring.study.utils.others;

public class PKCS1Exception extends Exception {
    public PKCS1Exception(int errorcode) {
        super();
        this.errorcode = errorcode;
    }

    public int getErrorCode() {
        return this.errorcode;
    }

    public static final int ERROR_DATACOVERSION_INTEGER_TOO_LONG = 1;

    public static final int ERROR_CRYPTOGRAPHIC_MESSAGE_OUT_RANGE = 10;
    public static final int ERROR_CRYPTOGRAPHIC_CIPHER_OUT_RANGE = 11;
    public static final int ERROR_CRYPTOGRAPHIC_SIGNATURE_OUT_RANGE = 12;

    public static final int ERROR_ENCODING_MESSAGE_TOO_LONG = 20;
    public static final int ERROR_ENCODING_ERROR = 21;
    public static final int ERROR_ENCODING_EM_TOO_SHORT = 22;

    public static final int ERROR_EME_MESSAGE_TOO_LONG = 30;
    public static final int ERROR_EME_LABEL_TOO_LONG = 31;
    public static final int ERROR_EME_DECODE = 32;

    public static final int ERROR_RSAES_DECRYPTION = 40;

    /**
     *
     */
    private static final long serialVersionUID = -1400355979065597090L;

    int errorcode;
}
