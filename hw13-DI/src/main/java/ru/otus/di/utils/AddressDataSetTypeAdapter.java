package ru.otus.di.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.otus.hibernate.dao.AddressDataSet;

import java.io.IOException;

public class AddressDataSetTypeAdapter extends TypeAdapter<AddressDataSet> {
    @Override
    public void write(JsonWriter jsonWriter, AddressDataSet addressDataSet) throws IOException {
        if (addressDataSet != null) {
            jsonWriter.value(addressDataSet.getStreet());
        } else {
            jsonWriter.nullValue();

        }
    }

    @Override
    public AddressDataSet read(JsonReader jsonReader) throws IOException {
        String address = jsonReader.nextString();
        return new AddressDataSet(address);
    }
}
