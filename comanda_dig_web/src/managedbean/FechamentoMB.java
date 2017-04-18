package managedbean;

import java.text.DecimalFormat;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ejb.ComandaFacade;
import exception.ComandaException;
import model.Comanda;
import model.Pedido;
import model.PedidosComanda;

@ManagedBean(name="fechamentoMB")
@ViewScoped
public class FechamentoMB extends AbstractMB{

	@EJB
	private ComandaFacade comandaFacade;
	
	private String codComanda;
	private boolean comandaDispo;
	private Comanda comanda;
	private PedidosComanda pedidosComanda;
	private int percentual = 10;
	
	
	public void vericarComanda(){
		try {
			comanda = comandaFacade.vericaComandaPedido(codComanda);
			comandaDispo = true;
			pedidosComanda = comandaFacade.pedidosComanda(comanda);
		} catch (ComandaException e) {
			super.menssagemErro(e.getMessage());
		}
	}
	
	
	public String totalString(){
		
		String msg = "Fechamento Comanda. Valor Total: ";
		double result = 0;
		if (pedidosComanda != null){
			for (Pedido pedido : pedidosComanda.getPedidos()) {
				result = result + pedido.getValor();
			}
		}
		result = result + ((result * percentual) / 100);
		DecimalFormat formato = new DecimalFormat("#,##0.00");
		msg = msg + formato.format(result);
		return msg;
	}
	
	
	public String totalPedido(){
		double result = 0;
		if (pedidosComanda != null){
			for (Pedido pedido : pedidosComanda.getPedidos()) {
				result = result + pedido.getValor();
			}
		}
		DecimalFormat formato = new DecimalFormat("#,##0.00");  
		return formato.format(result);
	}


	public ComandaFacade getComandaFacade() {
		return comandaFacade;
	}


	public void setComandaFacade(ComandaFacade comandaFacade) {
		this.comandaFacade = comandaFacade;
	}


	public String getCodComanda() {
		return codComanda;
	}


	public void setCodComanda(String codComanda) {
		this.codComanda = codComanda;
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


	public PedidosComanda getPedidosComanda() {
		return pedidosComanda;
	}


	public void setPedidosComanda(PedidosComanda pedidosComanda) {
		this.pedidosComanda = pedidosComanda;
	}



	public int getPercentual() {
		return percentual;
	}



	public void setPercentual(int percentual) {
		this.percentual = percentual;
	}
	
	
	
}
