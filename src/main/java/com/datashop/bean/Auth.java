package com.datashop.bean;

/**
 * Created by rasir on 2018/5/25.
 */
public enum Auth {
    NORMAL("normal",1),ADMIN("admin",5);

    private String name;

    private int role;

    private Auth(String name,int role){
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
