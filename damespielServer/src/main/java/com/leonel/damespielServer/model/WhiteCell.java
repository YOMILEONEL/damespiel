package com.leonel.damespielServer.model;


import com.leonel.damespielServer.model.enumeration.CellColor;

public class WhiteCell extends Cell {
    CellColor color = CellColor.WC;

    public String toString(){
        return color.toString();
    }
}
