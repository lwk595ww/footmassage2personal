package com.seetech.footmassage2.core.configration.jacksonConfig;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class LongWithStringJacksonDeserializer extends StdDeserializer<LongWithStringConvert> {
    private static final long serialVersionUID = -4553122378393126795L;

    public LongWithStringJacksonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LongWithStringConvert deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return new LongWithStringConvert(p.getValueAsLong());
    }
}


