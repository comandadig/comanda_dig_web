package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ejb.ComandaFacade;
import ejb.PedidoFacade;
import ejb.ProdutoFacade;
import exception.ComandaException;
import model.Categoria;
import model.Comanda;
import model.Produto;
import model.User;



@ManagedBean(name="pedidoMB")
@ViewScoped
public class PedidoMB extends AbstractMB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PedidoFacade pedidoFacade;
	@EJB
	private ProdutoFacade itensFacade;
	@EJB
	private ComandaFacade comandaFacade;
	
	
	@ManagedProperty("#{loginMB}")
    private LoginMB sessionBean;
	private User user = new User();
	private String codigoComanda = "";
	private List<Produto> itensList = new ArrayList<Produto>();
	private List<Produto> pedidosList = new ArrayList<>();
	private Boolean comandaDispo = false;
	private Comanda comanda = new Comanda();
	private List<Categoria> categorias = new ArrayList<Categoria>();
	private ArrayList<String> categoriasString = new ArrayList<String>();
	private Produto descitemMenuSelect = new  Produto();

	
	
	
	@PostConstruct
	public void ini(){
		user = sessionBean.getUsuario();
		codigoComanda = "";
		itensList = itensFacade.findAllProduto();
		comandaDispo = false;
		categoriasString = new ArrayList<String>();
		categorias = itensFacade.findAllCategoria();
		if (categorias != null && !categorias.isEmpty()){
			for (Categoria categoria : categorias) {
				categoriasString.add(categoria.getNome());
			}
		}
		descitemMenuSelect = new  Produto();
	}


	public void consultaComanda(){
		try {
			comanda = comandaFacade.vericaComandaPedido(codigoComanda);
			comandaDispo = true;
		} catch (ComandaException e) {
			menssagemErro(e.getMessage());
		}
	}

	
	
	public void adicionarProduto(Produto produto){
		produto.setHasPedido(true);
		this.pedidosList.add(produto);
	}

	
	public void fecharPedido(){
		System.out.println("entou..........");
		for (Produto p : this.pedidosList) {
			System.out.println("produto: "+ p.getNome());
		}
	}
	
	
	public Double totalPedidos(){
		double total = 0d;
		for (Produto p : this.pedidosList) {
			total = total + p.valorTot();
		}
		return total;
	}
	
	public void removeProduto(Produto produto){
		this.pedidosList.remove(produto);
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



	public ProdutoFacade getItensFacade() {
		return itensFacade;
	}



	public void setItensFacade(ProdutoFacade itensFacade) {
		this.itensFacade = itensFacade;
	}



	



	public List<Produto> getItensList() {
		return itensList;
	}



	public void setItensList(List<Produto> itensList) {
		this.itensList = itensList;
	}



	public Boolean getComandaDispo() {
		return comandaDispo;
	}



	public void setComandaDispo(Boolean comandaDispo) {
		this.comandaDispo = comandaDispo;
	}



	public Comanda getComanda() {
		return comanda;
	}



	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}



	public List<Categoria> getCategoriaMenus() {
		return categorias;
	}



	public void setCategoriaMenus(List<Categoria> categorias) {
		this.categorias = categorias;
	}



	public ArrayList<String> getCategoriasString() {
		return categoriasString;
	}



	public void setCategoriasString(ArrayList<String> categoriasString) {
		this.categoriasString = categoriasString;
	}






	public Produto getDescitemMenuSelect() {
		return descitemMenuSelect;
	}



	public void setDescitemMenuSelect(Produto descitemMenuSelect) {
		this.descitemMenuSelect = descitemMenuSelect;
	}


	public List<Produto> getPedidosList() {
		return pedidosList;
	}


	public void setPedidosList(List<Produto> pedidosList) {
		this.pedidosList = pedidosList;
	}

	
	
	
	

	
}
