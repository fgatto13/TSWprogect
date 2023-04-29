<%@ page language="java" import="java.util.*, bean.ArticoloBean, java.text.DecimalFormat, bean.ArticoloCart" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
    
<%
    ServletContext sc = this.getServletContext();
    List<?>cart = (List<?>)session.getAttribute("cart");
    
    //utilizzato per gestire i prezzi e visualizzarli con massimo 2 cifre dopo la virgola
    DecimalFormat frmt = new DecimalFormat();
	frmt.setMaximumFractionDigits(2);
	   
    sc.setAttribute("page","carrello");
    if((cart==null)){  
    	response.sendRedirect("./product?action=cart");
    	return;
    }
    float tot=0;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cart Section</title>
</head>
<body>
    <jsp:include page="header.jsp"></jsp:include>
    <section class="h-100 h-custom" style="background-color: #eee;">
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col">
        <div class="card">
          <div class="card-body p-4">

            <div class="row">

              <div class="col-lg-7">
                <h5 class="mb-3"><a href="catalogo.jsp?action=redirect" class="text-body"><i
                      class="fas fa-long-arrow-alt-left me-2"></i>Continue shopping</a></h5>
                <hr>

                <div class="d-flex justify-content-between align-items-center mb-4">
                  <div>
                    <p class="mb-1">Shopping cart</p>
                    <p class="mb-0">You have <%=cart.size() %> items in your cart</p>
                  </div>
                  <div>
                    <p class="mb-0"><span class="text-muted">Sort by:</span> <a href="product?action=sortCart"
                        class="text-body">price <i class="fas fa-angle-down mt-1"></i></a></p>
                  </div>
                </div>
     
                <%
                //mostro a video gli elementi del carrello
                ArticoloBean bean = null;
                if (cart != null && cart.size() != 0) {
                Iterator<?> it = cart.iterator();
    			   while (it.hasNext()) {
    				   ArticoloCart a = (ArticoloCart) it.next(); 
    				   
    				   //mi prelevo il bean e la quantit� da ArticoloCart
    				   bean = a.getBean();
    				   int q = a.getQCorrente();
    				   
    				   
                %>
                
                <div class="card mb-3">
                  <div class="card-body">
                    <div class="d-flex justify-content-between">
                      <div class="d-flex flex-row align-items-center">
                        <div>
                          <img src="data:image/*;base64,<%= bean.getImmagine() %>" 
                           class="img-fluid rounded-3" alt="Shopping item" style="width: 65px;">
                        </div>
                        <div class="ms-3">
                          <h5><%=bean.getNome() %></h5>
                          <p class="small mb-0"><%=bean.getTipologia() %></p>
                        </div>
                      </div>
                      <div class="d-flex flex-row align-items-center">
                        <div style="width: 50px;">
                          <h5 class="fw-normal mb-0"><%=q%></h5>
                        </div>
                        <div style="width: 80px;">
                          <h5 class="mb-0"><%=frmt.format(bean.getPrezzo()*q) %></h5>
                        </div>
                        <div style="width: 80px;">
                          <h6 class="mb-0"><a href="product?action=deleteByCart&id=<%=bean.getID()%>">Delete By Cart</a></h6>
                        </div>
                        
                        <a href="#!" style="color: #cecece;"><i class="fas fa-trash-alt"></i></a>
                      </div>
                    </div>
                  </div>
                </div>
                
                <% 
                //mi ricavo il prezzo totale che il cliente deve spendere per comprare l'intero carrello
                tot+=bean.getPrezzo()*q;}
    			}else{
    			%>
    			 <div class="ms-3">
                    <h5>nessun articolo nel carrello...</h5>
                 </div>
    			   <% 
    			}
    			 %>

                

              </div>
              <div class="col-lg-5">

                <div class="card bg-primary text-white rounded-3">
                  <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                      <h5 class="mb-0">Card details</h5>
                      <img src="https://mdbcdn.b-cdn.net/img/Photos/Avatars/avatar-6.webp"
                        class="img-fluid rounded-3" style="width: 45px;" alt="Avatar">
                    </div>

                    <p class="small mb-2">Card type</p>
                    <a href="#!" type="submit" class="text-white"><i
                        class="fab fa-cc-mastercard fa-2x me-2"></i></a>
                    <a href="#!" type="submit" class="text-white"><i
                        class="fab fa-cc-visa fa-2x me-2"></i></a>
                    <a href="#!" type="submit" class="text-white"><i
                        class="fab fa-cc-amex fa-2x me-2"></i></a>
                    <a href="#!" type="submit" class="text-white"><i class="fab fa-cc-paypal fa-2x"></i></a>

                    <form class="mt-4" action="product" method="post">
                    <input type="hidden" name="action" value="buy">
                      <div class="form-outline form-white mb-4">
                        <input type="text" id="typeName" class="form-control form-control-lg" siez="17"
                          placeholder="Cardholder's Name" />
                        <label class="form-label" for="typeName">Cardholder's Name</label>
                      </div>

                      <div class="form-outline form-white mb-4">
                        <input type="text" id="typeText" class="form-control form-control-lg" siez="17"
                          placeholder="1234 5678 9012 3457" minlength="19" maxlength="19" />
                        <label class="form-label" for="typeText">Card Number</label>
                      </div>

                      <div class="row mb-4">
                        <div class="col-md-6">
                          <div class="form-outline form-white">
                            <input type="text" id="typeExp" class="form-control form-control-lg"
                              placeholder="MM/YYYY" size="7" id="exp" minlength="7" maxlength="7" />
                            <label class="form-label" for="typeExp">Expiration</label>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="form-outline form-white">
                            <input type="password" id="typeText" class="form-control form-control-lg"
                              placeholder="&#9679;&#9679;&#9679;" size="1" minlength="3" maxlength="3" />
                            <label class="form-label" for="typeText">Cvv</label>
                          </div>
                        </div>
                      </div>


                    <hr class="my-4">

                    <div class="d-flex justify-content-between">
                      <p class="mb-2">Subtotal</p>
                      <p class="mb-2"><%=tot %></p>
                    </div>

                    <div class="d-flex justify-content-between">
                      <p class="mb-2">Shipping</p>
                      <p class="mb-2">$20.00</p>
                    </div>

                    <div class="d-flex justify-content-between mb-4">
                      <p class="mb-2">Total(Incl. taxes)</p>
                      <p class="mb-2"><%=tot+20 %></p>
                    </div>

                    <button type="submit" class="btn btn-info btn-block btn-lg">
                      <div class="d-flex justify-content-between">
                        <span><%=tot+20 %>$ </span>
                        <span> Checkout <i class="fas fa-long-arrow-alt-right ms-2"></i></span>
                      </div>
                    </button>
                   </form>
                  </div>
                </div>

              </div>

            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</section>
    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>