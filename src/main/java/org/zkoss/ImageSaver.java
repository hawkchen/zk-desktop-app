package org.zkoss;

import java.io.*;
import java.util.Base64;

public class ImageSaver {

    final String PNG_DATA_URL_PREFIX = "data:image/png;base64,";

    public void save(String imageDataUrl) throws IOException {
        byte[] image = Base64.getDecoder().decode(sanctify(imageDataUrl));
        new FileOutputStream(new File("qrcode.png")).write(image);
    }
    /**
     * a data URL of a PNG starts with "data:image/png;base64,", the text after the prefix is the PNG content itself.
     * The data URL sends from webview webkit contains "\r\n" which will fail decoding.
     * @param dataUrl
     * @return
     */
    private String sanctify(String dataUrl) {
        return dataUrl.replace(PNG_DATA_URL_PREFIX, "").replace("\r\n", "");
    }
}