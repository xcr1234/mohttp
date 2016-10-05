package com.mo.mohttp.misc;


import java.io.IOException;
import java.io.InputStream;

public class ClosedInputStream extends InputStream{


    //public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();

    @Override
    public int read() throws IOException {
        return IOUtils.EOF;
    }
}
