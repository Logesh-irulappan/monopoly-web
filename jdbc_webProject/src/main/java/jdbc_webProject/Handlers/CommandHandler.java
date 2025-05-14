//$Id$
package jdbc_webProject.Handlers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc_webProject.Monopoly;
import jdbc_webProject.Exceptions.RequestNotCompletedException;

public class CommandHandler extends HttpServlet {
	
	private Monopoly monopoly;
	public CommandHandler() {
		monopoly = Monopoly.getInstance();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = (String) request.getParameter("commandInput");

		List<String> availableCommands = monopoly.getCommands();
		
		if (availableCommands != null && availableCommands.contains(command)) {
			String strResponse = "";
			try {
				strResponse = monopoly.makeAction(command);
				monopoly.setReqStatus(true);
				monopoly.movePlayer();
			}
			catch(RequestNotCompletedException e) {
				strResponse = e.getDetails(); 
				monopoly.setReqStatus(false);
				request.setAttribute("hideDice", "true");
			}
			
			request.setAttribute("commandResponse", strResponse);
			request.getRequestDispatcher("/getGame").forward(request, response);
		}
		else {
			request.setAttribute("hideDice", "true");
			request.setAttribute("commandResponse", "Given command is Wrong or Invalid for this turn!..");
			request.getRequestDispatcher("/getGame").forward(request, response);
		}
	}
}







































