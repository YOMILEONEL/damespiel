package com.leonel.damespielServer.model;

import com.leonel.damespielServer.model.enumeration.Color;
import com.leonel.damespielServer.model.enumeration.TokenType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    TokenType tokenType;
    boolean isKing;
    boolean isDelete;



    public String toString(){
        return tokenType.toString();
    }

    public Token(TokenType  tokenType, Boolean isKing, boolean isDelete){
        this.tokenType= tokenType;
        this.isKing = isKing;
        this.isDelete= isDelete;
    }

    /**
     * Liefert die Farbe des Tokens basierend auf dem TokenType zurück.
     * @return die Farbe des Tokens (WHITE oder BLACK).
     */
    public Color getColor() {
        return switch (tokenType) {
            case TW, KW -> Color.WHITE; // Weiße Token
            case TB, KB -> Color.BLACK; // Schwarze Token
        };
    }
}