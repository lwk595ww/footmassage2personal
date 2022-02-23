package com.seetech.footmassage2.core.configration.jacksonConfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class LongWithStringJacksonSerializer extends StdSerializer<LongWithStringConvert> {

    private static final long serialVersionUID = -4507747325609301322L;

    public LongWithStringJacksonSerializer(Class<LongWithStringConvert> t) {
        super(t);
    }

    @Override
    public void serialize(LongWithStringConvert value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(String.valueOf(value.getVal()));
    }
}
