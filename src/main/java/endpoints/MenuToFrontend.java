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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class MenuToFrontend extends HttpServlet {

    private ObjectMapper mapper;

    public MenuToFrontend() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module = new SimpleModule("Serialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Menu.class, new MenuSerialiser());
        mapper.registerModule(module);
    }

    public String menuToJSON() throws IOException, SQLException {

        Database db = new Database();
        Categories cat = db.CATEGORIES;
        Menu menu = cat.getMenu();

        return mapper.writeValueAsString(menu);
    }

    //NOT SURE IF THIS IS CORRECT
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        MenuToFrontend m = new MenuToFrontend();
        try {
            pw.println(m.menuToJSON());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pw.flush();
    }

}
