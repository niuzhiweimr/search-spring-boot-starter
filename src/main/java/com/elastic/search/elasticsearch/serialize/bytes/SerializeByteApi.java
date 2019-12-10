package com.elastic.search.elasticsearch.serialize.bytes;

import com.elastic.search.elasticsearch.serialize.SerializeApi;

/**
 * 序列化POJO->Byte->POJO类型API
 *
 * @author SHOUSHEN LUAN
 */
public interface SerializeByteApi extends SerializeApi<byte[]> {

    /**
     * 编码
     *
     * @param t
     * @return
     */
    @Override
    public byte[] encode(Object t);

    /**
     * 解码
     *
     * @param data
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T> T decode(byte[] data, Class<T> type);
}
