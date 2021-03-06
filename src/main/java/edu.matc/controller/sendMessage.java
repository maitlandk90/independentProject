package edu.matc.controller;

import edu.matc.entity.Messages;
import edu.matc.entity.User;
import edu.matc.persistence.EntityDAO;
import edu.matc.persistence.MessagesDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(
        urlPatterns = {"/sendMessage"}
)
public class sendMessage extends HttpServlet {

    //EntityDAO messageDAO = new EntityDAO(Messages.class);
    MessagesDAO messageDAO = new MessagesDAO();
    EntityDAO userDAO = new EntityDAO(User.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getUserPrincipal().getName();
        String message = req.getParameter("message");
        if (req.getParameter("id") != null && userName != null) {
            if (message != null && !message.isEmpty() && !message.equals("")) {

                List<User> findFromUsers = userDAO.getByPropertyEqual("username", userName);
                User fromUser = findFromUsers.get(0);
                Integer fromUserId = fromUser.getId();

                Messages newMessage = new Messages(Integer.parseInt(req.getParameter("id")),
                                                    fromUserId,
                                                    message);

//                newMessage.setToUser(Integer.parseInt(req.getParameter("id")));
//                newMessage.setFromUser(fromUserId);
//                newMessage.setMessage(message);

                int ineserted = messageDAO.insert(newMessage);

                RequestDispatcher dispatcher = req.getRequestDispatcher("/profile?id=" + req.getParameter("id"));
                dispatcher.forward(req, resp);
            } else {
                req.setAttribute("errorMessage", "Please enter a message to send to this user.");
                RequestDispatcher dispatcher = req.getRequestDispatcher("/profile?id=" + req.getParameter("id"));
                dispatcher.forward(req, resp);
            }
        } else {
            req.setAttribute("errorMessage", "Please sign in to send a message.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/profile?id=" + req.getParameter("id"));
            dispatcher.forward(req, resp);
        }
    }
}
