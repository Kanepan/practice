package com.kane.practice.tools;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

public class UUidV7 {
    public static void main(String[] args) {
        UUID uuid = Generators.timeBasedEpochGenerator().generate(); // Version 7
// With JUG 5.0 added variation:
        UUID uuid2 = Generators.timeBasedEpochRandomGenerator().generate();

        System.out.println("UUIDv7: " + uuid);
        System.out.println("UUIDv7 with random: " + uuid2);
    }
}
