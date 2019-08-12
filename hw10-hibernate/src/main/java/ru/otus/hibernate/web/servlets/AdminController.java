package ru.otus.hibernate.web.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Login");
        String userName = req.getUserPrincipal().getName();
        String respAsString = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Users</title>\n" +
                "    <style type=\"text/css\">\n" +
                "        TABLE {\n" +
                "            width: 500px;\n" +
                "            border: 1px solid black;\n" +
                "        }\n" +
                "        TD, TH {\n" +
                "            padding: 3px;\n" +
                "        }\n" +
                "        TH {\n" +
                "            text-align: left;\n" +
                "            background: black;\n" +
                "            color: white;\n" +
                "            border: 1px solid white;\n" +
                "        }\n" +
                "        TR {\n" +
                "            border-bottom: 1px solid black;\n" +
                "        }\n" +
                "    </style>\n" +
                "    <script>\n" +
                "        function onCreate() {\n" +
                "            var name = document.getElementById(\"nameInput\").value;\n" +
                "            var address = document.getElementById(\"addressInput\").value;\n" +
                "            var age = document.getElementById(\"ageInput\").value;\n" +
                "            var url = 'http://localhost:8081/users' +\n" +
                "                    '?name=' + name +\n" +
                "                    '&address=' + address +\n" +
                "                    '&age=' + age;\n" +
                "            fetch(url, {\n" +
                "                method: 'POST'\n" +
                "            }).then( function() {\n" +
                "                document.getElementById(\"message\").hidden = false;\n" +
                "                setTimeout(function() {\n" +
                "                    document.getElementById(\"message\").hidden = true;\n" +
                "                }, 5000);\n" +
                "            });\n" +
                "        }\n" +
                "\n" +
                "        function onUpdate() {\n" +
                "            fetch('http://localhost:8081/users', {\n" +
                "                method: 'GET'\n" +
                "            }).then(function(response) {\n" +
                "                return response.json();\n" +
                "            }).then(function(dataSrc) {\n" +
                "               var tableContent = document.getElementById(\"table-content\");\n" +
                "               var content = '';\n" +
                "               var idx = 0;\n" +
                "               dataSrc.forEach(function(el) {\n" +
                "                   console.log(idx);\n" +
                "                   console.log(el);\n" +
                "                   content += '<tr>' +\n" +
                "                       '<td>' + (++idx) + '</td>' +\n" +
                "                       '<td>' + el.name + '</td>' +\n" +
                "                       '<td>' + el.address + '</td>' +\n" +
                "                       '<td>' +  el.age + '</td>' +\n" +
                "                       '</tr>';\n" +
                "               });\n" +
                "               tableContent.innerHTML = content;\n" +
                "            });\n" +
                "        }\n" +
                "\n" +
                "        function onLoad() {\n" +
                "            document.getElementById(\"message\").hidden = true;\n" +
                "        }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload=\"onLoad()\">\n" +
                "    <div>Welcome, " + userName + "!</div>\n" +
                "    <div style=\"display: inline-block; width: 40%;\">\n" +
                "        <p style=\"background-color: yellow;\">New user</p>\n" +
                "        <p>Name <input id=\"nameInput\" type=\"text\" name=\"name\"></p>\n" +
                "        <p>Address <input id=\"addressInput\" type=\"text\" name=\"address\"></p>\n" +
                "        <p>Age <input id=\"ageInput\" type=\"text\" name=\"age\"></p>\n" +
                "        <p><input type=\"button\" value=\"Create\" onclick=\"onCreate()\"></p>\n" +
                "        <p id=\"message\">Created!</p>\n" +
                "    </div>\n" +
                "    <div style=\"display: inline-block; vertical-align: top; width: 40%; margin-left: 10%;\">\n" +
                "        <p style=\"background-color: orange;\">All users</p>\n" +
                "        <p><input type=\"button\" value=\"Update\" onclick=\"onUpdate()\"></p>\n" +
                "        <table>\n" +
                "            <thead>\n" +
                "            <tr>\n" +
                "                <th></th>\n" +
                "                <th>Name</th>\n" +
                "                <th>Address</th>\n" +
                "                <th>Age</th>\n" +
                "            </tr>\n" +
                "            </thead>\n" +
                "            <tbody id=\"table-content\">\n" +
                "            </tbody>\n" +
                "            </thead>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = resp.getWriter();
        writer.print(respAsString);
        writer.flush();
    }

}
