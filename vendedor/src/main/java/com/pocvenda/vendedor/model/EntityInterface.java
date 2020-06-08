package com.pocvenda.vendedor.model;

import java.io.Serializable;

public interface EntityInterface<T, ID extends Serializable> extends Serializable {

    public ID getId();
}
