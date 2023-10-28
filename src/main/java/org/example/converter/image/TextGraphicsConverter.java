package org.example.converter.image;

import java.io.IOException;

public interface TextGraphicsConverter {

    String convert(String url) throws IOException, BadImageSizeException;


    void setMaxWidth(int width);


    void setMaxHeight(int height);


    void setMaxRatio(double maxRatio);


    void setTextColorSchema(TextColorSchema schema);
}
