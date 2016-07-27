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

import ejb.CategoriaItensMenuFacade;
import ejb.PedidoFacade;
import model.CategoriaMenu;
import model.Comanda;
import model.ItemMenu;
import model.User;
import util.SituacaoComanda;



@ManagedBean(name="pedidoMB")
@ViewScoped
public class PedidoMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PedidoFacade pedidoFacade;
	@EJB
	private CategoriaItensMenuFacade itensFacade;
	
	@ManagedProperty("#{loginMB}")
    private LoginMB sessionBean;
	private User user = new User();
	private String codigoComanda = "";
	private List<ItemMenu> itensList = new ArrayList<ItemMenu>();
	private Boolean comandaDispo = false;
	private Comanda comanda = new Comanda();
	private List<CategoriaMenu> categoriaMenus = new ArrayList<CategoriaMenu>();
	private ArrayList<String> categoriasString = new ArrayList<String>();
	private ItemMenu itemMenuSelect = new  ItemMenu();
	private ItemMenu descitemMenuSelect = new  ItemMenu();
	private int quant = 1;
	private String desc = "";

	
	public PedidoMB() {
		
	}
	
	
	
	@PostConstruct
	public void ini(){
		user = sessionBean.getUsuario();
		codigoComanda = "";
		itensList = itensFacade.findAllItem();
		comandaDispo = false;
		categoriasString = new ArrayList<String>();
		categoriaMenus = itensFacade.findAllCategoria();
		if (categoriaMenus != null && !categoriaMenus.isEmpty()){
			for (CategoriaMenu categoriaMenu : categoriaMenus) {
				categoriasString.add(categoriaMenu.getNome());
			}
		}
		itemMenuSelect = new  ItemMenu();
		descitemMenuSelect = new  ItemMenu();
		this.limparDialog();
	}


	public void consultaComanda(){
		System.out.println(codigoComanda);
		Comanda comanda = pedidoFacade.buscarComanda(codigoComanda);
		if (comanda != null){
			
			if (comanda.getSituacao().equals(SituacaoComanda.DISPONIVEL.getValue())){
				comandaDispo = false;
				String info = " Comanda N�o Esta Aberta..";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cod Coamnda: " + codigoComanda + info , null));
			} else {
				comandaDispo = true;
			}
			
		} else {
			comandaDispo = false;
			String info = " Comanda N�o Cadastrada..";
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



	public CategoriaItensMenuFacade getItensFacade() {
		return itensFacade;
	}



	public void setItensFacade(CategoriaItensMenuFacade itensFacade) {
		this.itensFacade = itensFacade;
	}



	



	public List<ItemMenu> getItensList() {
		return itensList;
	}



	public void setItensList(List<ItemMenu> itensList) {
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



	public List<CategoriaMenu> getCategoriaMenus() {
		return categoriaMenus;
	}



	public void setCategoriaMenus(List<CategoriaMenu> categoriaMenus) {
		this.categoriaMenus = categoriaMenus;
	}



	public ArrayList<String> getCategoriasString() {
		return categoriasString;
	}



	public void setCategoriasString(ArrayList<String> categoriasString) {
		this.categoriasString = categoriasString;
	}



	public ItemMenu getItemMenuSelect() {
		return itemMenuSelect;
	}



	public void setItemMenuSelect(ItemMenu itemMenuSelect) {
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



	public ItemMenu getDescitemMenuSelect() {
		return descitemMenuSelect;
	}



	public void setDescitemMenuSelect(ItemMenu descitemMenuSelect) {
		this.descitemMenuSelect = descitemMenuSelect;
	}

	
	
	
	

	
}
