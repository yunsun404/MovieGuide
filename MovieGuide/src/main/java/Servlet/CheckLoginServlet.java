package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/checkLogin")
public class CheckLoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req,
                        HttpServletResponse resp)
            throws IOException {

        HttpSession s = req.getSession(false);

        boolean login =
            (s != null && s.getAttribute("loginUser") != null);

        resp.setContentType("application/json");
        resp.getWriter().write("{\"login\":" + login + "}");
    }
}

