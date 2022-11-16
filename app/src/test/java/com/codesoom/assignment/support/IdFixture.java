package com.codesoom.assignment.support;

public enum IdFixture {
    ID_MIN(1L),
    ID_MAX(Long.MAX_VALUE),
    ;

    private final Long id;

    IdFixture(Long id) {
        this.id = id;
    }

    public Long value() {
        return id;
    }
}
