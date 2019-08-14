package ru.otus.webserver.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class AdminController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            System.out.println("Login");
            String respAsString = getPage();

            resp.setContentType("text/html");
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = resp.getWriter();
            writer.print(respAsString);
            writer.flush();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String getPage() throws URISyntaxException, IOException {
        URL pageUrl = AdminController.class.getClassLoader().getResource("static/admin.html");
        Optional<String> result = Files.lines(Paths.get(pageUrl.toURI())).reduce((a, b) -> a + b);
        if(result.isPresent()) return result.get();
        throw new NullPointerException();
    }

}
