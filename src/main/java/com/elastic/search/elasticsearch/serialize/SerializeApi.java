package com.elastic.search.elasticsearch.serialize;

/**
 * @author niuzhiwei
 */
public interface SerializeApi<R> {
    /**
     * 序列化API
     *
     * @param t
     * @return
     */
    public R encode(Object t);

    /**
     * 反序列化API
     *
     * @param data
     * @param type
     * @return
     */
    public <T> T decode(R data, Class<T> type);
}
