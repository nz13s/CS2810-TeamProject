package entities.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import entities.Menu;

import java.io.IOException;

/**
 * Class that serialises the Menu objects.
 *
 * @author Jatin
 * @author Cameron
 */

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
        jsonGenerator.writeObjectField("categories", varName.getList());
        jsonGenerator.writeEndObject();
    }
}
