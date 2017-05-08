package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ejb.CaixaFacade;
import model.Caixa;
import model.Comanda;
import util.SituacaoCaixa;
import util.SituacaoComanda;



@ManagedBean(name="caixaMB")
@ViewScoped
public class CaixaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CaixaFacade caixaFacade;
	
	private Boolean caixaAberto = false;
	private Caixa caixa = new Caixa();
	
	private List<Comanda> comandasDisponiveis = new ArrayList<>();
	private List<Comanda> comandasAbertas = new ArrayList<>();
	private List<Comanda> comandasFechadas = new ArrayList<>();
	private Comanda comandaSelect = new Comanda();
	
	public CaixaMB() {
		
	}
	
	public void teste(){
		System.out.println(caixa.getCadastroCliente());
	}
	
	
	@PostConstruct
	public void ini(){
		List<Caixa> list = caixaFacade.findCaixaBySituacao(SituacaoCaixa.ABERTO);
		if (list != null && !list.isEmpty()){
			this.caixaAberto = true;
			this.caixa = list.get(0);
			this.comandasAbertas = caixaFacade.findComandasByCaixa(caixa, SituacaoComanda.ABERTA);
			
		} else {
			this.caixaAberto = false;
		}
		
		comandaSelect = new Comanda();
	}

	
	public void abrirCaixa(){
		caixa = caixaFacade.abrirCaixa(caixa);
		this.comandasDisponiveis = caixaFacade.findComandasByCaixa(caixa, SituacaoComanda.ABERTA);
		this.caixaAberto = true;
	}

	
	public void fecharCaixa(){
		caixa = caixaFacade.abrirCaixa(caixa);
		this.caixaAberto = true;
	}


	public CaixaFacade getCaixaFacade() {
		return caixaFacade;
	}



	public void setCaixaFacade(CaixaFacade caixaFacade) {
		this.caixaFacade = caixaFacade;
	}



	public Caixa getCaixa() {
		return caixa;
	}



	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}



	public Boolean getCaixaAberto() {
		return caixaAberto;
	}



	public void setCaixaAberto(Boolean caixaAberto) {
		this.caixaAberto = caixaAberto;
	}



	public List<Comanda> getComandasDisponiveis() {
		return comandasDisponiveis;
	}



	public void setComandasDisponiveis(List<Comanda> comandasDisponiveis) {
		this.comandasDisponiveis = comandasDisponiveis;
	}



	public List<Comanda> getComandasAbertas() {
		return comandasAbertas;
	}



	public void setComandasAbertas(List<Comanda> comandasAbertas) {
		this.comandasAbertas = comandasAbertas;
	}



	public List<Comanda> getComandasFechadas() {
		return comandasFechadas;
	}



	public void setComandasFechadas(List<Comanda> comandasFechadas) {
		this.comandasFechadas = comandasFechadas;
	}



	public Comanda getComandaSelect() {
		return comandaSelect;
	}



	public void setComandaSelect(Comanda comandaSelect) {
		this.comandaSelect = comandaSelect;
	}
	
	
	

	
}
