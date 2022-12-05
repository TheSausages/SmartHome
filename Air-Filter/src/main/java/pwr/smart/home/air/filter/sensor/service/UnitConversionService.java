package pwr.smart.home.air.filter.sensor.service;

public class UnitConversionService {
    //based on: https://www.philips.co.in/c-f/XC000005753/what-do-my-philips-air-purifier%E2%80%99s-lights-mean

    public static int resolveIAI(int PM25value) {
        if (PM25value >= 1 && PM25value <= 12) {
            return 1;
        } else if (PM25value >= 13 && PM25value <= 23) {
            return 2;
        } else if (PM25value >= 24 && PM25value <= 35) {
            return 3;
        } else if (PM25value >= 36 && PM25value <= 48) {
            return 4;
        } else if (PM25value >= 49 && PM25value <= 61) {
            return 5;
        } else if (PM25value >= 62 && PM25value <= 75) {
            return 6;
        } else if (PM25value >= 76 && PM25value <= 87) {
            return 7;
        } else if (PM25value >= 88 && PM25value <= 101) {
            return 8;
        } else if (PM25value >= 102 && PM25value <= 115) {
            return 9;
        } else if (PM25value >= 116 && PM25value <= 132) {
            return 10;
        } else if (PM25value >= 133 && PM25value <= 150) {
            return 11;
        } else if (PM25value >= 151) {
            return 12;
        } else {
            return 0;
        }
    }

    public static int resolveGas(int PM25value) {
        if (PM25value >= 1 && PM25value <= 35) {
            return 1;
        } else if (PM25value >= 36 && PM25value <= 75) {
            return 2;
        } else if (PM25value >= 76 && PM25value <= 115) {
            return 3;
        } else if (PM25value >= 116 && PM25value <= 500) {
            return 4;
        } else {
            return 0;
        }
    }
}
