package com.leonel.damespielServer.model;


import com.leonel.damespielServer.model.enumeration.CellColor;

public class BlackCell extends Cell {
    Token  token ;
    CellColor color = CellColor.BC;


    public String toString(){
        if(token == null){
            return  color.toString();
        }
        return token.toString();
    }
}
