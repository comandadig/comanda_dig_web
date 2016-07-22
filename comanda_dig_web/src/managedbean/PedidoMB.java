package managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ejb.PedidoFacade;
import model.Comanda;
import model.User;



@ManagedBean(name="pedidoMB")
@ViewScoped
public class PedidoMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PedidoFacade pedidoFacade;
	
	@ManagedProperty("#{loginMB}")
    private LoginMB sessionBean;
	private User user = new User();
	private String codigoComanda = "";

	
	public PedidoMB() {
		
	}
	
	
	
	@PostConstruct
	public void ini(){
		user = sessionBean.getUsuario();
		codigoComanda = "";
	}


	public void consultaComanda(){
		System.out.println(codigoComanda);
		Comanda comanda = pedidoFacade.buscarComanda(codigoComanda);
		System.out.println("id comanda..." +comanda.getIdComanda());
	}

	public PedidoFacade getPedidoFacade() {
		return pedidoFacade;
	}



	public void setPedidoFacade(PedidoFacade pedidoFacade) {
		this.pedidoFacade = pedidoFacade;
	}



	public LoginMB getSessionBean() {
		return sessionBean;
	}



	public void setSessionBean(LoginMB sessionBean) {
		this.sessionBean = sessionBean;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public String getCodigoComanda() {
		return codigoComanda;
	}



	public void setCodigoComanda(String codigoComanda) {
		this.codigoComanda = codigoComanda;
	}

	
	
	
	

	
}
