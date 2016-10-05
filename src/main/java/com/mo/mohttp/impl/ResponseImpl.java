package com.mo.mohttp.impl;

import com.mo.mohttp.Response;
import com.mo.mohttp.misc.ByteArrayOutputStream;
import com.mo.mohttp.misc.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class ResponseImpl implements Response {

    public String string() throws IOException {
        return IOUtils.toString(stream(), Charset.defaultCharset());
    }

    public String string(String encoding) throws IOException {
        return IOUtils.toString(stream(),encoding);
    }

    public BufferedImage image() throws IOException {
        return ImageIO.read(stream());
    }

    public byte[] bytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(stream(),byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
