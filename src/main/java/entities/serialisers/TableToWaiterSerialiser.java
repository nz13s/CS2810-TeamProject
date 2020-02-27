package entities.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import entities.TableState;

import java.io.IOException;

public class TableToWaiterSerialiser extends StdSerializer<TableState> {
    public TableToWaiterSerialiser(Class<TableState> table_State) {
        super(table_State);
    }

    @Override
    public void serialize(TableState table_State, JsonGenerator jsonGenerator,
                          SerializerProvider serializer)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("table_state", TableState.getNeedWaiter());
        jsonGenerator.writeEndObject();
    }
}