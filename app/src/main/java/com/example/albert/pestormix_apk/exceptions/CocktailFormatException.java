package com.example.albert.pestormix_apk.exceptions;

/**
 * Created by Albert on 14/02/2016.
 */
public class CocktailFormatException extends RuntimeException {
    public CocktailFormatException() {
    }

    public CocktailFormatException(String detailMessage) {
        super(detailMessage);
    }
}
