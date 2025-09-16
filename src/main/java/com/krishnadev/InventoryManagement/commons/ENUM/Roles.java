package com.krishnadev.Inventory.commons.ENUM;

public enum Roles {

    CUSTOMER("CUSTOMER"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN"),
    UPDATE("UPDATE"),
    CREATE("CREATE"),
    READ("READ"),
    REMOVE("REMOVE"),
    DELETE("DELETE");

    private String name;
    private Roles(String name) {
        this.name = name;
    }
}
