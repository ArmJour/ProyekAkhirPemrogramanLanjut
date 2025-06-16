package com.ProyekAkhir.Main;

import java.util.UUID;

public class MainApp {
    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
    }
}
