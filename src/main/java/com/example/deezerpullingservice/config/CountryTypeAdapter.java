package com.example.deezerpullingservice.config;

import com.example.deezerpullingservice.model.Country;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CountryTypeAdapter extends TypeAdapter<Country> {

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Country.class ? (TypeAdapter<T>) new CountryTypeAdapter() : null;
        }
    };

    @Override
    public void write(JsonWriter out, Country value) throws IOException {
        out.value(value.getCode());
    }

    @Override
    public Country read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String code = in.nextString();
        return new Country(code);
    }
}
