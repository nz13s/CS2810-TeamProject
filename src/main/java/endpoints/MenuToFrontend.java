package endpoints;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import databaseInit.Database;
import entities.Menu;
import entities.serialisers.MenuSerialiser;
import sql.Categories;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Class that converts a Menu from a database, to JSON and POSTs it
 * to the frontend.
 *
 * @author Jatin
 * @author Cameron
 */

@WebServlet("/menu")
public class MenuToFrontend extends HttpServlet {

    private ObjectMapper mapper;

    /**
     * Constructor that initialises the mapper attribute.
     */

    public MenuToFrontend() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module = new SimpleModule("Serialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Menu.class, new MenuSerialiser());
        mapper.registerModule(module);
    }

    /**
     * Method that converts the objects in Menu into JSON.
     *
     * @return String that represents the Menu in JSON.
     * @throws IOException
     * @throws SQLException
     */

    public String menuToJSON() throws IOException, SQLException {

        Categories cat = Database.CATEGORIES;
        Menu menu = cat.getMenu();

        return mapper.writeValueAsString(menu);
    }

    /**
     * Method that GETs the JSON objects.
     *
     * @param req server request.
     * @param resp server response.
     * @throws ServletException
     * @throws IOException
     */

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        MenuToFrontend m = new MenuToFrontend();
        try {
            pw.println(m.menuToJSON());
        } catch (SQLException e) {
            e.printStackTrace();
            pw.println(e.getMessage());
        }
        pw.flush();
    }

}
