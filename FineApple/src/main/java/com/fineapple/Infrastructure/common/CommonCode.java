package com.fineapple.Infrastructure.common;


import lombok.Getter;

@Getter
public class CommonCode {
    private final String code;
    private final String name;
    private final String description;

    public CommonCode(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "CommonCode{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
