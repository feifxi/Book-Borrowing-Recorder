package org.example.domain;

import java.io.Serializable;

public class Customer implements Serializable{
    private final String id;
    private String name;
    private String email;

    public Customer(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return this.getId()+"["+this.getName()+"]";
    }
}

