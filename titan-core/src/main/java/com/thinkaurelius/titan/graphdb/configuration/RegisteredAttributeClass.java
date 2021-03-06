package com.thinkaurelius.titan.graphdb.configuration;

import com.google.common.base.Preconditions;
import com.thinkaurelius.titan.core.AttributeSerializer;
import com.thinkaurelius.titan.graphdb.database.serialize.Serializer;
import com.thinkaurelius.titan.graphdb.database.serialize.SerializerInitialization;

public class RegisteredAttributeClass<T> implements Comparable<RegisteredAttributeClass> {

    private final Class<T> type;
    private final AttributeSerializer<T> serializer;
    private final int position;

    public RegisteredAttributeClass(Class<T> type, int position) {
        this(type, null, position);
    }

    public RegisteredAttributeClass(Class<T> type, AttributeSerializer<T> serializer, int position) {
        Preconditions.checkArgument(position>=0,"Invalid position: %s",position);
        this.type = type;
        this.serializer = serializer;
        this.position = position;
    }

    private int getPosition() {
        return position+SerializerInitialization.RESERVED_ID_OFFSET;
    }

    void registerWith(Serializer s) {
        if (serializer == null) s.registerClass(type,getPosition());
        else s.registerClass(type, serializer, getPosition());
    }

    @Override
    public boolean equals(Object oth) {
        if (this == oth) return true;
        else if (!getClass().isInstance(oth)) return false;
        return type.equals(((RegisteredAttributeClass<?>) oth).type) || position == ((RegisteredAttributeClass) oth).position;
    }

    @Override
    public String toString() {
        return type.toString() + "#" + position;
    }

    @Override
    public int compareTo(RegisteredAttributeClass registeredAttributeClass) {
        return position - registeredAttributeClass.position;
    }
}
