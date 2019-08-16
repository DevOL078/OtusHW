package ru.otus.webserver.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.otus.hibernate.dao.AddressDataSet;

import java.io.IOException;

public class AddressAdapter extends TypeAdapter<AddressDataSet> {
    @Override
    public void write(JsonWriter jsonWriter, AddressDataSet addressDataSet) throws IOException {
        jsonWriter.value(addressDataSet != null ? addressDataSet.getStreet() : "null");
    }

    @Override
    public AddressDataSet read(JsonReader jsonReader) {
        return null;
    }
}
