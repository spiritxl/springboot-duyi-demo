package com.test.packet;

public class TestMain {
    public static void main(String[] args) {
        for (int i = 0; i < 7; i++){
            new People("xl"+i+1).start();
        }
    }
}
