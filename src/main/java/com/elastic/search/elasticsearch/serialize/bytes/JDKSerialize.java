package com.elastic.search.elasticsearch.serialize.bytes;

import java.io.*;

/**
 * @author niuzhiwei
 */
public class JDKSerialize implements SerializeByteApi {

    public static JDKSerialize INSTANCE = new JDKSerialize();

    @Override
    public byte[] encode(Object t) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            oos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                if (null != oos) {
                    oos.close();
                }
                bos.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        ObjectInputStream ois = null;
        if (null != bytes) {
            try {
                ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                return (T) ois.readObject();
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            } finally {
                try {
                    if (null != ois) {
                        ois.close();
                    }
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
