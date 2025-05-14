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
import jdbc_webProject.Exceptions.RequestNotCompletedException;

public class GameHandler extends HttpServlet {
	
	public Monopoly monopoly;
	public static List<String> resultList = new ArrayList<>();
	
	public GameHandler() {
		monopoly = Monopoly.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getAttribute("responseFromBankHandler") != null) {
			resultList.add(0, String.valueOf(request.getAttribute("responseFromBankHandler")));
		}
		if (request.getAttribute("commandResponse") != null) {
			resultList.add(0, String.valueOf(request.getAttribute("commandResponse")));
		}
		if (!monopoly.getReqStatus()) {
			request.setAttribute("", response);
		}
		
		request.setAttribute("playerData", monopoly.getPlayerData());
		request.setAttribute("results", resultList);
		
		request.getRequestDispatcher("game.jsp").forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		monopoly.setReqStatus(false);
		
		resultList.add(0, monopoly.gameLogic());
		
		List<String> choices = monopoly.getCommands();
		StringBuilder commands = new StringBuilder();
		for (String s : choices) {
		    commands.append(s + "\t");
		}
		resultList.add(1, commands.toString());
		
		if (monopoly.getReqStatus()) {
			monopoly.movePlayer();
		}
		else {
			request.setAttribute("hideDice", "hide");
		}
		
		request.setAttribute("playerData", monopoly.getPlayerData());
		request.setAttribute("results", resultList);
		
		request.getRequestDispatcher("game.jsp").forward(request, response);
	}
}























