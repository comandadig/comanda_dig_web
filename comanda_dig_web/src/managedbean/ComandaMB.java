package managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ejb.ComandaFacade;
import model.Comanda;
import util.SituacaoComanda;



@ManagedBean(name="comandaMB")
@ViewScoped
public class ComandaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ComandaFacade comandaFacade;
	
	private Comanda comanda = new Comanda();
	private String codigoComanda = "";
	private boolean comandaDispo = false;
	

	
	public ComandaMB() {
		
	}
	
	
	
	@PostConstruct
	public void ini(){
		codigoComanda = "";
		comandaDispo = false;
		comanda = new Comanda();
	}

	public void abrirComanda(){
		try {
			comandaFacade.abrirComanda(comanda);
		} catch (Exception e) {
			String info = e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR, info , null));
		}
		
		String info = " Comanda Aberta com Sucesso..";
		FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,"Cod Coamnda: " + codigoComanda + info , null));
		this.ini();
	}

	public void consultaComanda(){
		 
		comanda = comandaFacade.buscarComanda(codigoComanda);
		if (comanda != null){
			
			if (comanda.getSituacao().equals(SituacaoComanda.DISPONIVEL.getValue())){
				this.comandaDispo = true;
			} else {
				comanda = null;
				this.comandaDispo = false;
				String info = " Comanda Não Esta Disponivel..";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cod Coamnda: " + codigoComanda + info , null));
			}
			
		} else {
			comanda = null;
			this.comandaDispo = false;
			String info = " Comanda Não Cadastrada..";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cod Coamnda: " + codigoComanda + info , null));
		}
	}



	public ComandaFacade getComandaFacade() {
		return comandaFacade;
	}



	public void setComandaFacade(ComandaFacade comandaFacade) {
		this.comandaFacade = comandaFacade;
	}



	public String getCodigoComanda() {
		return codigoComanda;
	}



	public void setCodigoComanda(String codigoComanda) {
		this.codigoComanda = codigoComanda;
	}






	public boolean isComandaDispo() {
		return comandaDispo;
	}



	public void setComandaDispo(boolean comandaDispo) {
		this.comandaDispo = comandaDispo;
	}



	public Comanda getComanda() {
		return comanda;
	}



	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}

	
	
}
