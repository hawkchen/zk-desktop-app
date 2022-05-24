package org.zkoss;

import java.io.*;
import java.util.Base64;
import java.util.function.Consumer;

public class ImageSaver {

    final String PNG_DATA_URL_PREFIX = "data:image/png;base64,";
    private Consumer<String> show;

    public ImageSaver(Consumer<String> show) {
        this.show = show;
    }

    public void save(String imageDataUrl) throws IOException {
        byte[] image = Base64.getDecoder().decode(sanctify(imageDataUrl));
        new FileOutputStream("qrcode.png").write(image);
        show.accept("QR code saved");
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