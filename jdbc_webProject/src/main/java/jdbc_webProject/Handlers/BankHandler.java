//$Id$
package jdbc_webProject.Handlers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc_webProject.Banker;
import jdbc_webProject.Monopoly;
import jdbc_webProject.Player;

public class BankHandler extends HttpServlet {
	
	private Monopoly monopoly;
	private Banker banker;
	
	public BankHandler() {
		monopoly = Monopoly.getInstance();
		banker = Banker.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder strResponse = new StringBuilder();
		Player curPlayer = monopoly.getCurrentPlayer();
		
		if (request.getParameter("borrowAmount") != null) {
			double borrowAmount = 0;
			try {
				borrowAmount = Double.parseDouble(request.getParameter("borrowAmount"));
			}
			catch(Exception e) {
				e.printStackTrace();
				strResponse.append(e.toString());
				
				request.setAttribute("responseFromBankHandler", strResponse.toString());
				RequestDispatcher rd = request.getRequestDispatcher("/getGame");
				rd.forward(request,response);
			}
			
			strResponse.append(Banker.getMoneyFromBank(curPlayer, borrowAmount));
			if (!monopoly.getReqStatus()) {
				request.setAttribute("hideDice", "true");
			}
			
			request.setAttribute("responseFromBankHandler", strResponse.toString());
			RequestDispatcher rd = request.getRequestDispatcher("/getGame");
			rd.forward(request,response);
		}
		else {
			double payAmount = 0;
			try {
				payAmount = Double.parseDouble(request.getParameter("payAmount"));
			}
			catch(Exception e) {
				e.printStackTrace();
				strResponse.append(e.toString());
				
				request.setAttribute("responseFromBankHandler", strResponse.toString());
				RequestDispatcher rd = request.getRequestDispatcher("/getGame");
				rd.forward(request,response);
			}
			
			strResponse.append(banker.payBorrowedMoney(payAmount, curPlayer));
			
			if (!monopoly.getReqStatus()) {
				request.setAttribute("hideDice", "true");
			}
			
			request.setAttribute("responseFromBankHandler", strResponse.toString());
			RequestDispatcher rd = request.getRequestDispatcher("/getGame");
			rd.forward(request,response);
		}
	}
}
