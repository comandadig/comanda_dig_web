package managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ejb.CaixaFacade;
import ejb.ComandaFacade;
import exception.ComandaException;
import model.Comanda;



@ManagedBean(name="comandaMB")
@ViewScoped
public class ComandaMB extends AbstractMB implements Serializable {
	
	private static final String COMANDA_ABERTA_COM_SUCESSO = "Comanda Aberta com Sucesso.";

	private static final String NAO_FOI_POSSIVEL_ABRIR_A_COMANDA = "Não foi possivel abrir a comanda.";

	private static final long serialVersionUID = 1L;
	
	@EJB
	private ComandaFacade comandaFacade;
	
	@EJB
	private CaixaFacade caixaFacade;
	
	private Comanda comanda = new Comanda();
	private String codigoComanda = "";
	private boolean comandaDispo = false;
	
	
	
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
			menssagemErro(NAO_FOI_POSSIVEL_ABRIR_A_COMANDA);
		}
		menssagemSucesso(COMANDA_ABERTA_COM_SUCESSO);
		this.ini();
	}

	public void consultaComanda(){
		try {
			comanda = comandaFacade.vericaAberturaComanda(codigoComanda);
			comandaDispo = true;
		} catch (ComandaException e) {
			menssagemErro(e.getMessage());
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
