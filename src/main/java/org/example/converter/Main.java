package org.example.converter;

import org.example.converter.image.TextGraphicsConverter;
import org.example.converter.image.TextGraphicsConverterImp;
import org.example.converter.server.ConverterServer;

public class Main {
    public static void main(String[] args) throws Exception {

        TextGraphicsConverter converter = new TextGraphicsConverterImp();

        ConverterServer server = new ConverterServer(converter);

        server.start();
    }
}