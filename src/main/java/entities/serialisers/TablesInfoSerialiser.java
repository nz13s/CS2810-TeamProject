package entities.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import entities.TableState;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Class to serialise the table state, to pass to the Frontend.
 *
 * @author Jatin Khatra
 */
//TODO this can be removed?
public class TablesInfoSerialiser extends StdSerializer<TableState> {

  public TablesInfoSerialiser() {
    this(null);
  }

  public TablesInfoSerialiser(Class<TableState> tableState) {
    super(tableState);
  }

  @Override
  public void serialize(TableState tableState, JsonGenerator jsonGenerator, SerializerProvider serializer)
          throws IOException {
    jsonGenerator.writeStartObject();
    try {
      jsonGenerator.writeObjectField("table_state", TableState.getTableUnOccupied());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    jsonGenerator.writeEndObject();
  }
}