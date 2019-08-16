package ru.otus.webserver.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.hibernate.dao.AddressDataSet;
import ru.otus.hibernate.dao.User;
import ru.otus.hibernate.service.DBService;
import ru.otus.webserver.util.AddressAdapter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UsersController extends HttpServlet {

    private DBService<User> userService;
    private GsonBuilder builder;

    public UsersController(DBService<User> userService) {
        super();
        this.userService = userService;
        this.builder = new GsonBuilder().registerTypeAdapter(AddressDataSet.class, new AddressAdapter());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<User> users = userService.getAll();
        Gson gson = builder.create();
        String json = gson.toJson(users);

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.print(json);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String age = req.getParameter("age");

        User newUser = new User();
        newUser.setName(name);
        newUser.setAge(age != null ? Integer.parseInt(age) : 0);
        AddressDataSet addressDataSet = new AddressDataSet();
        addressDataSet.setStreet(address);
        addressDataSet.setUser(newUser);
        newUser.setAddress(addressDataSet);
        userService.save(newUser);

        resp.setStatus(HttpServletResponse.SC_OK);
    }

}
