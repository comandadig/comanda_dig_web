package managedbean;

import java.text.DecimalFormat;

import javax.annotation.PostConstruct;
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

	private static final String FECHAMENTO_COMANDA_VALOR_TOTAL = "Fechamento Comanda. Valor Total: ";

	private static final String MASK = "#,##0.00";
	
	

	@EJB
	private ComandaFacade comandaFacade;
	
	private String formaPagamento = "D";
	private double valorCliente = 0;
	private double valorTroco = 0;
	private String codComanda = "";
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
		
		if (pedidosComanda == null || pedidosComanda.getPedidos() == null) return "0"; 
		
		String msg = FECHAMENTO_COMANDA_VALOR_TOTAL;
		double result = 0;
		if (pedidosComanda != null){
			for (Pedido pedido : pedidosComanda.getPedidos()) {
				result = result + pedido.getValor();
			}
		}
		result = result + ((result * percentual) / 100);
		DecimalFormat formato = new DecimalFormat(MASK);
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
		DecimalFormat formato = new DecimalFormat(MASK);  
		return formato.format(result);
	}


	@PostConstruct
	public void init(){
		valorCliente = 0;
		valorTroco = 0;
		formaPagamento = "D";
		codComanda = new String("");
		comandaDispo = false;
		comanda = new Comanda();
		pedidosComanda = new PedidosComanda();
	    percentual = 10;
	}
	
	
	public void fecharComanda(){
		try {
			comanda.setFormaPagamento(formaPagamento);
			comandaFacade.fecharComanda(comanda,percentual);
			this.init();
			super.menssagemSucesso("Comanda fechada com Sucesso..");
		} catch (ComandaException e) {
			super.menssagemErro(e.getMessage());
		}
		
	}
	
	
	public void calculaTroco(){
		
		double result = 0;
		if (pedidosComanda != null){
			for (Pedido pedido : pedidosComanda.getPedidos()) {
				result = result + pedido.getValor();
			}
		}
		result = result + ((result * percentual) / 100);
		
		valorTroco = valorCliente - result;
		
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


	public String getFormaPagamento() {
		return formaPagamento;
	}


	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}


	public static String getFechamentoComandaValorTotal() {
		return FECHAMENTO_COMANDA_VALOR_TOTAL;
	}


	public static String getMask() {
		return MASK;
	}


	public double getValorCliente() {
		return valorCliente;
	}


	public void setValorCliente(double valorCliente) {
		this.valorCliente = valorCliente;
	}


	public double getValorTroco() {
		return valorTroco;
	}


	public void setValorTroco(double valorTroco) {
		this.valorTroco = valorTroco;
	}
	
	
	
}
