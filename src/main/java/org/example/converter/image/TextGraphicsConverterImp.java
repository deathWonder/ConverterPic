package org.example.converter.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImp implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;

    public TextGraphicsConverterImp() {
        schema = new TextColorSchemaImp();
    }


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        double imgRatio = imgWidth >= imgHeight ? (double) imgWidth / imgHeight : (double) imgHeight / imgWidth;


        if (maxRatio != 0) {
            if (imgRatio > maxRatio) {
                throw new BadImageSizeException(maxRatio, imgRatio);
            }
        }

        if (maxWidth != 0) {
            if (imgWidth > maxWidth) {
                imgHeight = maxWidth * imgHeight / imgWidth;
                imgWidth = maxWidth;
            }
        }

        if (maxHeight != 0) {
            if (imgHeight > maxHeight) {
                imgWidth = maxHeight * imgWidth / imgHeight;
                imgHeight = maxHeight;
            }
        }


        Image scaledImage = img.getScaledInstance(imgWidth, imgHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();

        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();


        char[][] pic = new char[imgHeight][imgWidth];

        for (int i = 0; i < imgHeight; i++) {
            for (int j = 0; j < imgWidth; j++) {
                int color = bwRaster.getPixel(j, i, new int[3])[0];
                char c = schema.convert(color);
                pic[i][j] = c;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < imgHeight; i++) {
            for (int j = 0; j < imgWidth; j++) {
                stringBuilder.append(pic[i][j]);
                stringBuilder.append(pic[i][j]);
            }

            stringBuilder.append("\n");
        }


        return stringBuilder.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
