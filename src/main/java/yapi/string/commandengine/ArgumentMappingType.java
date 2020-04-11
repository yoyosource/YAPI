// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.commandengine;

public enum ArgumentMappingType {

    // A whole Number between Integer.MIN_VALUE and Integer.MAX_VALUE
    INTEGER(),
    // A [INTEGER] just in HEX representation
    HEX_INTEGER(),

    // A floating point number between Double.MIN_VALUE and Double.MAX_VALUE
    DOUBLE(),

    // A whole Number between Long.MIN_VALUE and Long.MAX_VALUE
    LONG(),
    // A [LONG] just in HEX representation
    HEX_LONG(),

    // A BigInteger
    BIG_INTEGER(),
    // A [BIGINTEGER] just in HEX representation
    HEX_BIG_INTEGER(),

    // A BigDecimal
    BIG_DECIMAL(),


    // A Character between 0 and 65535
    CHARACTER(),
    // A [CHARACTER] just in HEX representation
    HEX_CHARACTER(),


    // A String
    STRING(),
    // A [STRING] just in HEX representation
    HEX_STRING(),
    // A [STRING] just in BASE64 representation
    BASE64_STRING(),


    // A JSON Object
    JSON(),

    // A YAPION Object
    YAPION(),


    // A Boolean Flag
    FLAG(),


    // Bytes just in HEX representation
    HEX_BYTES(),
    // Bytes just in BASE64 representation
    BASE64_BYTES(),


    // A Byte in HEX representation
    HEX_BYTE(),
    // A Hash in HEX representation
    HEX_HASH(),
    // A Color in HEX representation
    HEX_COLOR(),


    // A File
    FILE()

}