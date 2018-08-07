package com.leonds.core.orm;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.Map;

/**
 * @author Leon
 */
public class MyMapWrapperFactory implements ObjectWrapperFactory {
    @Override
    public boolean hasWrapperFor(Object o) {
        return o instanceof Map;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object o) {
        return new MyMapWrapper(metaObject, (Map) o);
    }
}
