package com.kane.practice.program.logicexecutor.core.domain.metadata;

import com.kane.practice.program.logicexecutor.meta.Meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public abstract class MultiMetaData<T extends Meta> extends BaseMetaData<T> {
    private List<MetaData<T>> childMetaDatas;

    protected MultiMetaData(MetaData<T> a, MetaData<T> b) {
        super(null, null);
        childMetaDatas = new ArrayList<>(Arrays.asList(a, b));
    }

    protected MultiMetaData(List<MetaData<T>> list) {
        super(null, null);
        if (!(list instanceof ArrayList)) {
            list = new ArrayList<>(list);
        }
        childMetaDatas = list;
    }

    public List<MetaData<T>> getChildMetaDatas() {
        return Collections.unmodifiableList(childMetaDatas);
    }

    protected void add(MetaData<T> meta) {
        childMetaDatas.add(meta);
    }
}
