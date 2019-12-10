package com.elastic.search.elasticsearch.serialize.api.xml;

import com.elastic.search.common.domain.SearchBaseResult;
import com.thoughtworks.xstream.XStream;

/**
 * @author niuzhiwei
 */
public class SerializeXml {

    XStream XSTREAM = new XStream();

    private XStreamAlias xStreamAlias = XStreamAlias.create();

    {
        alias(SearchBaseResult.class);
    }

    public SerializeXml() {
    }

    public SerializeXml alias(String name, Class<?> type) {
        XSTREAM.alias(name, type);
        return this;
    }

    public SerializeXml alias(Object root) {
        if (this.xStreamAlias != null) {
            this.xStreamAlias.initAlias(XSTREAM, root);
        }
        return this;
    }

    public SerializeXml alias(Class<?> type) {
        XSTREAM.alias(type.getSimpleName(), type);
        return this;
    }

    public SerializeXml alias(Class<?>... types) {
        for (Class<?> type : types) {
            alias(type);
        }
        return this;
    }

    public String encode(Object obj) {
        xStreamAlias.initAlias(XSTREAM, obj);
        return XSTREAM.toXML(obj);
    }

    @SuppressWarnings("unchecked")
    public <T> T decode(String xml, Object root) {
        xStreamAlias.initAlias(XSTREAM, root);
        return (T) XSTREAM.fromXML(xml, root);
    }

}
