package entities.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import entities.Menu;

import java.io.IOException;

public class MenuSerialiser extends StdSerializer<Menu> {

    public MenuSerialiser() {
        this(null);
    }

    public MenuSerialiser(Class<Menu> varName) {
        super(varName);
    }

    @Override
    public void serialize(Menu varName, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        int i=0;
        jsonGenerator.writeObjectField(varName.getCat(i).getCategoryName(), varName.getList());
        i += 1;
        jsonGenerator.writeEndObject();
    }
}