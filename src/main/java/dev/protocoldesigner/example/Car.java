package dev.protocoldesigner.example;

import java.util.UUID;

/**
 * Car
 */
public class Car {
    private String name;
    public Car(){
        this.name = UUID.randomUUID().toString();
    }
    public String toString(){
        return "Car{"+name+"}";
    }
}
