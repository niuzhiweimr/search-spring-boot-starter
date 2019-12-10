package com.elastic.search.elasticsearch.serialize.bytes;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author niuzhiwei
 */
public class KryoSerialize implements SerializeByteApi {

    public static KryoSerialize INSTANCE = new KryoSerialize();

    @Override
    public byte[] encode(Object t) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(t.getClass(), new JavaSerializer());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        try {
            kryo.writeClassAndObject(output, t);
            output.flush();
            output.close();
            return baos.toByteArray();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(clazz, new JavaSerializer());

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        try {
            return (T) kryo.readClassAndObject(input);
        } finally {
            input.close();
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
            }
        }
    }
}

