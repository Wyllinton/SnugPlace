package com.snugplace.demo.Model.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    USER,
    GUEST,
    HOST,
    ADMIN,
}
