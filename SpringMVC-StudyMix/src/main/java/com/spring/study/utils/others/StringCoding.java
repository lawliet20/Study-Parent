package com.spring.study.utils.others;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;


/**
 * �ַ�������Ĺ�����
 * Ŀ��:Ϊ��ʵ�ָ���ַ���ַ����ת��
 * <br/><br/> �ַ�ת��˵����
 * <blockquote>
 * "�й�" ��UTF-8�����ʮ����Ʊ�ʾΪ �� E4 B8 AD E5 9B BD  <br/><br/>
 * ��"�й�" ��GB2312�е�ʮ����Ʊ�ʾΪ�� D6 D0 B9 FA   <br/><br/>
 * </blockquote>
 * ����˵��ת������ͬ������֣���ݲ�ͬ�ı��룬ת�ɲ�ͬ�Ķ�������ݡ�
 * �����䶼��"�й�" ���ڲ�ͬ�ַ��У����������ݲ�ͬ��<br/><br/>
 * ���� JDK API�У�ֻ�ܸ�ݲ�ͬ�ı��룬�Զ�������ݽ��ͳɲ�ͬ��
 * ����,���������ݱ��?�䡣 ��String�Ĺ��췽��String(byte[] bytes, Charset charset)
 *
 * @author zyplanke
 */

public class StringCoding {

    private StringCoding() {
    }

    /*
     * The cached coders for each thread
     */
    private static ThreadLocal decoder = new ThreadLocal();
    private static ThreadLocal encoder = new ThreadLocal();

    private static boolean warnUnsupportedCharset = true;

    private static Object deref(ThreadLocal tl) {
        SoftReference sr = (SoftReference) tl.get();
        if (sr == null)
            return null;
        return sr.get();
    }

    private static void set(ThreadLocal tl, Object ob) {
        tl.set(new SoftReference(ob));
    }

    // Trim the given byte array to the given length
    //
    private static byte[] safeTrim(byte[] ba, int len, Charset cs) {
        if (len == ba.length
                && (System.getSecurityManager() == null || cs.getClass()
                .getClassLoader() == null))
            return ba;
        else
            return Arrays.copyOf(ba, len);
    }

    // Trim the given char array to the given length
    //
    private static char[] safeTrim(char[] ca, int len, Charset cs) {
        if (len == ca.length
                && (System.getSecurityManager() == null || cs.getClass()
                .getClassLoader() == null))
            return ca;
        else
            return Arrays.copyOf(ca, len);
    }

    private static int scale(int len, float expansionFactor) {
        // We need to perform double, not float, arithmetic; otherwise
        // we lose low order bits when len is larger than 2**24.
        return (int) (len * (double) expansionFactor);
    }

    private static Charset lookupCharset(String csn) {
        if (Charset.isSupported(csn)) {
            try {
                return Charset.forName(csn);
            } catch (UnsupportedCharsetException x) {
                throw new Error(x);
            }
        }
        return null;
    }

    private static void warnUnsupportedCharset(String csn) {
        if (warnUnsupportedCharset) {
            // Use sun.misc.MessageUtils rather than the Logging API or
            // System.err since this method may be called during VM
            // initialization before either is available.
            System.out.println("WARNING: Default charset " + csn
                    + " not supported, using ISO-8859-1 instead");
            warnUnsupportedCharset = false;
        }
    }

    // -- Decoding --
    private static class StringDecoder {
        private final String requestedCharsetName;
        private final Charset cs;
        private final CharsetDecoder cd;

        private StringDecoder(Charset cs, String rcn) {
            this.requestedCharsetName = rcn;
            this.cs = cs;
            this.cd = cs.newDecoder().onMalformedInput(
                    CodingErrorAction.REPLACE).onUnmappableCharacter(
                    CodingErrorAction.REPLACE);
        }

        String charsetName() {
            return cs.name();
        }

        final String requestedCharsetName() {
            return requestedCharsetName;
        }

        char[] decode(byte[] ba, int off, int len) {
            int en = scale(len, cd.maxCharsPerByte());
            char[] ca = new char[en];
            if (len == 0)
                return ca;
            cd.reset();
            ByteBuffer bb = ByteBuffer.wrap(ba, off, len);
            CharBuffer cb = CharBuffer.wrap(ca);
            try {
                CoderResult cr = cd.decode(bb, cb, true);
                if (!cr.isUnderflow())
                    cr.throwException();
                cr = cd.flush(cb);
                if (!cr.isUnderflow())
                    cr.throwException();
            } catch (CharacterCodingException x) {
                // Substitution is always enabled,
                // so this shouldn't happen
                throw new Error(x);
            }
            return safeTrim(ca, cb.position(), cs);
        }

    }

    /**
     * ���ֽ��������ݸ��Ĭ�ϵ��ַ���н��루���ֽ����а��ַ������ַ�
     *
     * @param charsetName �ַ������Ϊnull����Ϊ:ISO-8859-1
     * @param ba          �ֽ���������
     * @param off         �ֽ��������ʼλ��
     * @param len         �ֽڳ��ȣ�����
     * @return �������ַ�
     * @throws UnsupportedEncodingException
     */
    public static char[] decode(String charsetName, byte[] ba, int off, int len)
            throws UnsupportedEncodingException {
        StringDecoder sd = (StringDecoder) deref(decoder);
        String csn = (charsetName == null) ? "ISO-8859-1" : charsetName;
        if ((sd == null)
                || !(csn.equals(sd.requestedCharsetName()) || csn.equals(sd
                .charsetName()))) {
            sd = null;
            try {
                Charset cs = lookupCharset(csn);
                if (cs != null)
                    sd = new StringDecoder(cs, csn);
            } catch (IllegalCharsetNameException x) {
            }
            if (sd == null)
                throw new UnsupportedEncodingException(csn);
            set(decoder, sd);
        }
        return sd.decode(ba, off, len);
    }

    /**
     * ���ֽ��������ݸ��Ĭ�ϵ��ַ���н��루���ֽ����а��ַ������ַ�
     *
     * @param cs  �ַ�
     * @param ba  �ֽ���������
     * @param off �ֽ��������ʼλ��
     * @param len �ֽڳ��ȣ�����
     * @return �������ַ�
     */
    public static char[] decode(Charset cs, byte[] ba, int off, int len) {
        StringDecoder sd = new StringDecoder(cs, cs.name());
        byte[] b = Arrays.copyOf(ba, ba.length);
        return sd.decode(b, off, len);
    }

    /**
     * ���ֽ��������ݸ��Ĭ�ϵ��ַ���н��루���ֽ�����Ĭ���ַ������ַ�
     *
     * @param ba  �ֽ���������
     * @param off �ֽ��������ʼλ��
     * @param len �ֽڳ��ȣ�����
     * @return �������ַ�
     */
    public static char[] decode(byte[] ba, int off, int len) {
        String csn = Charset.defaultCharset().name();
        try {
            return decode(csn, ba, off, len);
        } catch (UnsupportedEncodingException x) {
            warnUnsupportedCharset(csn);
        }
        try {
            return decode("ISO-8859-1", ba, off, len);
        } catch (UnsupportedEncodingException x) {
            // If this code is hit during VM initialization, MessageUtils is
            // the only way we will be able to get any kind of error message.
            System.out.println("ISO-8859-1 charset not available: "
                    + x.toString());
            // If we can not find ISO-8859-1 (a required encoding) then things
            // are seriously wrong with the installation.
            System.exit(1);
            return null;
        }
    }

    // -- Encoding --
    private static class StringEncoder {
        private Charset cs;
        private CharsetEncoder ce;
        private final String requestedCharsetName;

        private StringEncoder(Charset cs, String rcn) {
            this.requestedCharsetName = rcn;
            this.cs = cs;
            this.ce = cs.newEncoder().onMalformedInput(
                    CodingErrorAction.REPLACE).onUnmappableCharacter(
                    CodingErrorAction.REPLACE);
        }

        String charsetName() {
            return cs.name();
        }

        final String requestedCharsetName() {
            return requestedCharsetName;
        }

        byte[] encode(char[] ca, int off, int len) {
            int en = scale(len, ce.maxBytesPerChar());
            byte[] ba = new byte[en];
            if (len == 0)
                return ba;

            ce.reset();
            ByteBuffer bb = ByteBuffer.wrap(ba);
            CharBuffer cb = CharBuffer.wrap(ca, off, len);
            try {
                CoderResult cr = ce.encode(cb, bb, true);
                if (!cr.isUnderflow())
                    cr.throwException();
                cr = ce.flush(bb);
                if (!cr.isUnderflow())
                    cr.throwException();
            } catch (CharacterCodingException x) {
                // Substitution is always enabled,
                // so this shouldn't happen
                throw new Error(x);
            }
            return safeTrim(ba, bb.position(), cs);
        }
    }

    /**
     * ���ָ�����ַ����ַ����ֽ��б��루���ַ���ָ���ַ���ֽ����У�
     *
     * @param charsetName �ַ������Ϊnull����Ϊ:ISO-8859-1
     * @param ca          �ַ�����
     * @param off         �ַ������е���ʼλ��
     * @param len         �ַ�ȣ�����
     * @return �������ֽ�����
     * @throws UnsupportedEncodingException
     */
    public static byte[] encode(String charsetName, char[] ca, int off, int len)
            throws UnsupportedEncodingException {
        StringEncoder se = (StringEncoder) deref(encoder);
        String csn = (charsetName == null) ? "ISO-8859-1" : charsetName;
        if ((se == null)
                || !(csn.equals(se.requestedCharsetName()) || csn.equals(se
                .charsetName()))) {
            se = null;
            try {
                Charset cs = lookupCharset(csn);
                if (cs != null)
                    se = new StringEncoder(cs, csn);
            } catch (IllegalCharsetNameException x) {
            }
            if (se == null)
                throw new UnsupportedEncodingException(csn);
            set(encoder, se);
        }
        return se.encode(ca, off, len);
    }

    /**
     * ���ָ�����ַ����ַ����ֽ��б��루���ַ���ָ���ַ���ֽ����У�
     *
     * @param cs  �ַ�
     * @param ca  �ַ�����
     * @param off �ַ������е���ʼλ��
     * @param len �ַ�ȣ�����
     * @return �������ֽ�����
     */
    public static byte[] encode(Charset cs, char[] ca, int off, int len) {
        StringEncoder se = new StringEncoder(cs, cs.name());
        char[] c = Arrays.copyOf(ca, ca.length);
        return se.encode(c, off, len);
    }

    /**
     * ���Ĭ���ַ����ַ����ֽ��б��루���ַ���Ĭ���ַ���ֽ����У�
     *
     * @param ca  �ַ�����
     * @param off �ַ������е���ʼλ��
     * @param len �ַ�ȣ�����
     * @return �������ֽ�����
     */
    public static byte[] encode(char[] ca, int off, int len) {
        String csn = Charset.defaultCharset().name();
        try {
            return encode(csn, ca, off, len);
        } catch (UnsupportedEncodingException x) {
            warnUnsupportedCharset(csn);
        }
        try {
            return encode("ISO-8859-1", ca, off, len);
        } catch (UnsupportedEncodingException x) {
            // If this code is hit during VM initialization, MessageUtils is
            // the only way we will be able to get any kind of error message.
            System.err.println("ISO-8859-1 charset not available: "
                    + x.toString());
            // If we can not find ISO-8859-1 (a required encoding) then things
            // are seriously wrong with the installation.
            System.exit(1);
            return null;
        }
    }
}
