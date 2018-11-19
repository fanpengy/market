package com.market.vo;

import java.io.Serializable;

public class LoginResult implements Serializable {

    private Integer enum1;

    private Long id;

    public Integer getEnum1() {
        return enum1;
    }

    public void setEnum1(Integer enum1) {
        this.enum1 = enum1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
