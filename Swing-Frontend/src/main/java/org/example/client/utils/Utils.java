package org.example.client.utils;

public class Utils {
    public static String columnToPosition(int row, int col){
        if (col < 0 || col > 26) {
            throw new RuntimeException("Somethings is wrong");
        }
        String column =  String.valueOf((char) ('A' + col));
        return column+(row+1);
    }
}
