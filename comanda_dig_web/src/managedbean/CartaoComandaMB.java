package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ejb.CartaoComandaFacade;
import model.CartaoComanda;



@ManagedBean(name="cartaoComandaMB")
@ViewScoped
public class CartaoComandaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CartaoComandaFacade cartaoComandaFacade;
	private CartaoComanda cartaoComanda = new CartaoComanda();
	private List<CartaoComanda> comandas = new ArrayList<CartaoComanda>();
	
	
	
	public CartaoComandaMB() {

	}

	
	@PostConstruct
	public void ini(){
		this.cartaoComanda = new CartaoComanda();
		this.comandas = cartaoComandaFacade.findAll();
	}

	
	public void salvar(){
		if (cartaoComanda != null && cartaoComanda.getIdCartaoComanda() != null){
			CartaoComanda cartaoComandaPersist = cartaoComandaFacade.find(cartaoComanda.getIdCartaoComanda());
			cartaoComandaPersist.setCodComanda(cartaoComanda.getCodComanda());
			cartaoComandaPersist.setSituacao(cartaoComanda.getSituacao());
			cartaoComanda = this.cartaoComandaFacade.update(cartaoComandaPersist);
			String info = "Cartão Comanda Atualizado com Sucesso ";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,cartaoComanda.getCodComanda() + info , null));
		} else{
			cartaoComandaFacade.update(cartaoComanda);
			String info = "Cartão Comanda Cadastrado com Sucesso ";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,cartaoComanda.getCodComanda() + info , null));
		}
		this.ini();
	}


	public CartaoComandaFacade getCartaoComandaFacade() {
		return cartaoComandaFacade;
	}


	public void setCartaoComandaFacade(CartaoComandaFacade cartaoComandaFacade) {
		this.cartaoComandaFacade = cartaoComandaFacade;
	}


	public CartaoComanda getCartaoComanda() {
		return cartaoComanda;
	}


	public void setCartaoComanda(CartaoComanda cartaoComanda) {
		this.cartaoComanda = cartaoComanda;
	}


	public List<CartaoComanda> getComandas() {
		return comandas;
	}


	public void setComandas(List<CartaoComanda> comandas) {
		this.comandas = comandas;
	}

	
	
	
	
	
	
	
	
}
