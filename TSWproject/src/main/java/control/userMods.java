package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.UserBean;
import model.ModelUserDAO;

public class userMods extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newText = request.getParameter("newText");
        String field = request.getParameter("parameter");
        String utente = (String) request.getSession().getAttribute("un");
        // Validate the newText parameter
        if (newText == null || newText.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        ModelUserDAO mdu = new ModelUserDAO();
        try {
			UserBean ub = mdu.modifyUser(newText, field, utente);
			request.getSession().setAttribute("un", ub.getUsername());
			request.getSession().setAttribute("user-details", mdu.doRetrieveByUsr(ub.getUsername()));
		} catch (SQLException e) {
			if(field.equalsIgnoreCase("email"))
			   response.sendError(401, "Email gi� esistente");
			if(field.equalsIgnoreCase("CF"))
			   response.sendError(401, "CF gi� esistente");
			if(field.equalsIgnoreCase("username"))
			   response.sendError(401, "Username gi� utilizzato");
		}
    }
}