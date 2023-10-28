package org.example.converter.image;

public class TextColorSchemaImp implements TextColorSchema {

    private final char[] chars = new char[]{'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};

    @Override
    public char convert(int color) {
        return chars[color / 31];
    }
}
