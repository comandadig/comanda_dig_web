package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ejb.ProdutoFacade;
import ejb.PedidoFacade;
import model.Categoria;
import model.Comanda;
import model.Produto;
import model.User;
import util.SituacaoComanda;



@ManagedBean(name="pedidoMB")
@ViewScoped
public class PedidoMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PedidoFacade pedidoFacade;
	@EJB
	private ProdutoFacade itensFacade;
	
	@ManagedProperty("#{loginMB}")
    private LoginMB sessionBean;
	private User user = new User();
	private String codigoComanda = "";
	private List<Produto> itensList = new ArrayList<Produto>();
	private Boolean comandaDispo = false;
	private Comanda comanda = new Comanda();
	private List<Categoria> categorias = new ArrayList<Categoria>();
	private ArrayList<String> categoriasString = new ArrayList<String>();
	private Produto itemMenuSelect = new  Produto();
	private Produto descitemMenuSelect = new  Produto();
	private int quant = 1;
	private String desc = "";

	
	
	
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
		itemMenuSelect = new  Produto();
		descitemMenuSelect = new  Produto();
		this.limparDialog();
	}


	public void consultaComanda(){
		System.out.println(codigoComanda);
		Comanda comanda = pedidoFacade.buscarComanda(codigoComanda);
		if (comanda != null){
			
			if (comanda.getSituacao().equals(SituacaoComanda.DISPONIVEL.getValue())){
				comandaDispo = false;
				String info = " Comanda Não Esta Aberta..";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cod Coamnda: " + codigoComanda + info , null));
			} else {
				comandaDispo = true;
			}
			
		} else {
			comandaDispo = false;
			String info = " Comanda Não Cadastrada..";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cod Coamnda: " + codigoComanda + info , null));
		}
	}

	
	public void limparDialog(){
		quant = 1;
		desc = "";
	}
	
	public void adicionarItem(){
		
		System.out.println("item "  + itemMenuSelect.getNome());
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



	public Produto getItemMenuSelect() {
		return itemMenuSelect;
	}



	public void setItemMenuSelect(Produto itemMenuSelect) {
		this.itemMenuSelect = itemMenuSelect;
	}



	public int getQuant() {
		return quant;
	}



	public void setQuant(int quant) {
		this.quant = quant;
	}



	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
	}



	public Produto getDescitemMenuSelect() {
		return descitemMenuSelect;
	}



	public void setDescitemMenuSelect(Produto descitemMenuSelect) {
		this.descitemMenuSelect = descitemMenuSelect;
	}

	
	
	
	

	
}
