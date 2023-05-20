package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.AcquistoBean;
import bean.ArticoloBean;
import bean.ArticoloCart;
import bean.ComposizioneBean;
import bean.ContoBean;
import bean.IndirizzoBean;
import bean.UserBean;
import dao.AcquistoDAO;
import dao.ArticoloDAO;
import dao.ComposizioneDAO;
import dao.ContoDAO;
import dao.UserDAO;
import model.ModelAcquistoDAO;
import model.ModelComposizioneDAO;
import model.ModelContoDAO;
import model.ModelUserDAO;
import model.MusicalModelArticoloBean;

public class AcquistoServlet extends HttpServlet {

	private static final long serialVersionUID = 5990937487736664990L;
	AcquistoDAO model = new ModelAcquistoDAO();
	ComposizioneDAO comp = new ModelComposizioneDAO();
	ComposizioneBean compbean = null;
	IndirizzoBean ind = null;
	UserBean user = null;
	UserDAO usermodel = new ModelUserDAO();
	HttpSession session = null;
	AcquistoBean acq = null;
	ContoDAO modelconto = new ModelContoDAO();
	ContoBean conto = null;
	double importo = 0;
	ArticoloDAO modelarticolo = new MusicalModelArticoloBean();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

   	    session = request.getSession();
   	    String username = (String) session.getAttribute("un");
   	    if(username==null) {
   	    	RequestDispatcher error = null;
   	        String header = "Client Error";
   	        String details = "utente non loggato...";
   	        response.setStatus(400);
   	        error = getServletContext().getRequestDispatcher("/error.jsp?errorMessageHeader=" + header + "&errorMessageDetails=" + details);
   	        error.forward(request, response);
   	    }
   	    String IBAN = (String) getServletContext().getAttribute("IBAN");
   	    if(IBAN==null) {
   	    	RequestDispatcher error = null;
   	        String header = "Client Error";
   	        String details = "acesso alla pagina non autorizzato...";
   	        response.setStatus(403);
   	        error = getServletContext().getRequestDispatcher("/error.jsp?errorMessageHeader=" + header + "&errorMessageDetails=" + details);
   	        error.forward(request, response);
   	    }
   	    @SuppressWarnings("unchecked")
		List<ArticoloCart>cart = (List<ArticoloCart>) session.getAttribute("cart");
   	    List<ComposizioneBean>complist = new ArrayList<ComposizioneBean>();
   	    
   	   if(cart!=null && cart.size()!=0){
   		   
   	     for(ArticoloCart ac : cart) {
   	       compbean = new ComposizioneBean();
   		   compbean.setArticolo(ac.getBean());
   		   compbean.setqAcquistate(ac.getQCorrente());
   		   compbean.setPrezzo(ac.getBean().getPrezzo());
   		   complist.add(compbean);
   	      }
   	     
     	  session.removeAttribute("cart");
   	      acq = new AcquistoBean();
   	      
   	      try {
			user = usermodel.doRetrieveByUsr(username);
		} catch (SQLException e) {
			 e.printStackTrace();
			 RequestDispatcher error = null;
		     String header = "Client Error";
		     String details = "errore nell'acquisto, salvataggio utente non effettuato...";
		     response.setStatus(500);
		     error = getServletContext().getRequestDispatcher("/error.jsp?errorMessageHeader=" + header + "&errorMessageDetails=" + details);
		     error.forward(request, response);
		}
   	      
   	      acq.setConsumer(user);
   	      
   	      try {
			conto = modelconto.doRetrieveByKey(IBAN);
		} catch (SQLException e) {
			e.printStackTrace();
			 RequestDispatcher error = null;
		     String header = "Client Error";
		     String details = "errore nell'acquisto, salvataggio conto non effettuato...";
		     response.setStatus(500);
		     error = getServletContext().getRequestDispatcher("/error.jsp?errorMessageHeader=" + header + "&errorMessageDetails=" + details);
		     error.forward(request, response);
		}
   	      
   	      acq.setConto(conto);
   	      
   	      for(ComposizioneBean c : complist) {
   	    	  importo += c.getPrezzo();
   	      }
   	      
   	      acq.setImporto(importo);
   	      try {
			model.doSave(acq);
		} catch (SQLException e) {
			 e.printStackTrace();
			 RequestDispatcher error = null;
		     String header = "Client Error";
		     String details = "errore nell'acquisto, salvataggio acquisto non effettuato...";
		     response.setStatus(500);
		     error = getServletContext().getRequestDispatcher("/error.jsp?errorMessageHeader=" + header + "&errorMessageDetails=" + details);
		     error.forward(request, response);
		}  
   	      
   	      AcquistoBean ab = null;
   	      try {
			   ab = model.doRetrieveBeanDesc();
		  } catch (SQLException e1) {
			    e1.printStackTrace();
		  }
   	      
   	      if(ab!=null) {
   	       for(ComposizioneBean c : complist) {
   	    	  c.setAcquisto(ab);
   	    	  try {
				comp.doSave(c);
			  }catch (SQLException e) {
				 e.printStackTrace();
				 RequestDispatcher error = null;
			     String header = "Client Error";
			     String details = "errore nell'acquisto, salvataggio composizione non effettuato...";
			     response.setStatus(500);
			     error = getServletContext().getRequestDispatcher("/error.jsp?errorMessageHeader=" + header + "&errorMessageDetails=" + details);
			     error.forward(request, response);
			}
   	      }}
   	     
   	   //ora bisogna diminuire le quantit� dei vari articoli nel database
   	   for(ArticoloCart c : cart) {
   		   ArticoloBean art = c.getBean();
   		   try {
			modelarticolo.doChangeQuantity(art.getID(), art.getQuantita() - c.getQCorrente());
		} catch (SQLException e) {
			e.printStackTrace();
			RequestDispatcher error = null;
		    String header = "Client Error";
		    String details = "errore nell'acquisto, e' possibile che le quantita' inserite nel carrello "
		    		+ "siano superiori a quelle disponibili. altrimenti errore nella modifica delle quantita' nel database... ";
		    response.setStatus(500);
		    error = getServletContext().getRequestDispatcher("/error.jsp?errorMessageHeader=" + header + "&errorMessageDetails=" + details);
		    error.forward(request, response);
		}
   	   }
   	          
   	   }else {
		 RequestDispatcher error = null;
	     String header = "Client Error";
	     String details = "carrello vuoto...";
	     response.setStatus(400);
	     error = getServletContext().getRequestDispatcher("/error.jsp?errorMessageHeader=" + header + "&errorMessageDetails=" + details);
	     error.forward(request, response);
   	   }
   	   
   	   getServletContext().setAttribute("by_address_page", true);
   	   RequestDispatcher dispatcher = null;   
       dispatcher = getServletContext().getRequestDispatcher("/Acquisto.jsp");
       dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}