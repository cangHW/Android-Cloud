package com.proxy.service.network.converter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by cangHX
 * on 2019/03/06  14:26
 */
public class StringRequestBodyConverter implements Converter<String, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    public RequestBody convert(@NonNull String value) throws IOException {
        Buffer buffer=new Buffer();
        Writer writer=new OutputStreamWriter(buffer.outputStream(),UTF_8);
        writer.write(value);
        writer.flush();
        writer.close();
        return RequestBody.create(MEDIA_TYPE,buffer.readByteString());
    }
}
