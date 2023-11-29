package com.korant.youya.workplace.enums.role;

/**
 * @ClassName RoleEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/29 17:18
 * @Version 1.0
 */
public enum RoleEnum {

    HR("hr"),
    ADMIN("admin");

    private String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
