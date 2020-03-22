package endpoints;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import entities.Order;
import entities.serialisers.OrderSerialiser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/*
*
* */
public class CancelOrder extends HttpServlet {
    private ObjectMapper om = new ObjectMapper();

    /**
     * Configures the {@link ObjectMapper} before being used to serialise the {@link Order} for the frontend, uses the {@link OrderSerialiser}
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module =
                new SimpleModule("OrderSerialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Order.class, new OrderSerialiser());
        om.registerModule(module);
    }


}
