//$Id$
package jdbc_webProject.Handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc_webProject.Monopoly;
import jdbc_webProject.Exceptions.AlreadyExistsException;

public class InitialHandler extends HttpServlet {
	
	private Monopoly monopoly;
	public InitialHandler() {
		monopoly = Monopoly.getInstance();
	}											
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("Lobby.jsp").forward(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String username = request.getParameter("username");
		
		try {
			monopoly.createPlayer(username);
		}
		catch(AlreadyExistsException e) {
			request.setAttribute("showAlert", e.getDetails());
		}
		
		int playerCount = monopoly.getPlayerCount();
		List<String> playerNames = monopoly.getPlayerNames(); 
		
		request.setAttribute("playerNames", playerNames);	
		request.setAttribute("playerCount", playerCount);
		
		request.getRequestDispatcher("Lobby.jsp").forward(request, response);
	}
}
