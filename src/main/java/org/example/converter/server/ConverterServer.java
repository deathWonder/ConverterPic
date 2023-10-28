package org.example.converter.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.example.converter.image.TextGraphicsConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConverterServer {

    public static final int PORT = 8888;

    private final HttpServer server;
    private final TextGraphicsConverter converter;

    public ConverterServer(TextGraphicsConverter converter) throws Exception {
        if (converter == null) {
            throw new IllegalArgumentException("Серверу нужно передать в конструктор объект-конвертер, а было передано null.");
        }
        this.converter = converter;
        this.converter.setMaxHeight(300);
        this.converter.setMaxWidth(300);
        this.converter.setMaxRatio(4);

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/", this::serveHtml);
        server.createContext("/convert", this::serveConvertion);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:8888/");
        server.start();
    }

    protected void serveHtml(HttpExchange h) throws IOException {
        System.out.println("Serving html..");
        Path htmlPath = Path.of("assets/index.html");
        String htmlContent = Files.readString(htmlPath);
        Path jsPath = Path.of("assets/my.js");
        String jsContent = Files.readString(jsPath);
        htmlContent = htmlContent.replace("{{{JS}}}", jsContent);
        byte[] htmlBytes = htmlContent.getBytes();
        h.sendResponseHeaders(200, htmlBytes.length);
        h.getResponseBody().write(htmlBytes);
        h.close();
    }

    protected void serveConvertion(HttpExchange h) throws IOException {
        System.out.println("Convert request..");
        String url = new BufferedReader(new InputStreamReader(h.getRequestBody())).readLine();
        try {
            System.out.println("Converting image: " + url);
            Files.write(Path.of("assets/img.txt"), converter.convert(url).getBytes());
            byte[] img = converter.convert(url).getBytes();
            System.out.println("...converted!");
            h.sendResponseHeaders(200, img.length);
            h.getResponseBody().write(img);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            if (msg.isEmpty()) {
                msg = "Произошла ошибка конвертации :'(";
            }
            byte[] msgBytes = msg.getBytes();
            h.sendResponseHeaders(500, msgBytes.length);
            h.getResponseBody().write(msgBytes);
        } finally {
            h.close();
        }
    }
}

