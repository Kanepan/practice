package com.kane.practice.program.logicexecutor.core.domain.metadata;

import com.kane.practice.program.logicexecutor.core.domain.param.Param;
import com.kane.practice.program.logicexecutor.meta.Meta;
import lombok.Data;

import java.util.List;

@Data
public abstract class BaseMetaData<T extends Meta> implements MetaData<T> {

    protected T metaDef;

    protected Param[] params;

    public BaseMetaData(T def, Param[] params) {
        this.metaDef = def;
        this.params = params;
    }

    public Param[] getParams() {
        return params;
    }

    public void setParams(Param[] params) {
        this.params = params;
    }

    public T getMetaDef() {
        return metaDef;
    }


    @Override
    public MetaData<T> and(MetaData<T> other) {
        checkMetaIsogeny(other);
        return new AndMetaData<>(this, other);
    }


    @Override
    public MetaData<T> or(MetaData<T> other) {
        //compare instances of AndMetaData, OrMetaData, NotMetaData
        checkMetaIsogeny(other);
        return new OrMetaData<>(this, other);
    }

    @Override
    public MetaData<T> not() {
        return new NotMetaData<>(this);
    }

    public List<Param> getAllParm() {
        List<Param> allParams = new java.util.ArrayList<>();
        if (params != null) {
            for (Param param : params) {
                allParams.add(param);
            }
        }

        if (this instanceof MultiMetaData) {
            List<MetaData<T>> childMetaDatas = ((MultiMetaData<T>) this).getChildMetaDatas();
            for (MetaData<T> metaData : childMetaDatas) {
                allParams.addAll(metaData.getAllParm());
            }
        }

        return allParams;
    }

//    private List<Param> getChildParms(){
//        List<Param> allParams = new java.util.ArrayList<>();
//        if (this instanceof MultiMetaData) {
//            List<MetaData<T>> childMetaDatas = ((MultiMetaData<T>) this).getChildMetaDatas();
//            for (MetaData<T> metaData : childMetaDatas) {
//                allParams.addAll(((BaseMetaData)metaData).getAllParm());
//            }
//        }
//        return allParams;
//    }

    private void checkMetaIsogeny(MetaData<T> other) {
        if (other instanceof SingleMetaData) {
            if (this instanceof SingleMetaData) {
                if (!this.getMetaDef().getClass().getSuperclass().equals((other).getMetaDef().getClass().getSuperclass())) {
                    throw new IllegalArgumentException("不支持相同类型的元数据进行或，与操作");
                }
            } else if (this instanceof MultiMetaData && other instanceof SingleMetaData) {
                for (MetaData<T> metaData : ((MultiMetaData<T>) this).getChildMetaDatas()) {
                    if (metaData.getMetaDef().getClass().getSuperclass().equals(other.getMetaDef().getClass().getSuperclass())) {
                        throw new IllegalArgumentException("不支持相同类型的元数据进行或，与操作");
                    }
                }
            }
        }
    }


}
