// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string;

public enum HashType {

    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    private String type;

    HashType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}