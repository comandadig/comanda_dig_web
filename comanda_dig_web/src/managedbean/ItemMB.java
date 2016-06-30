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
import javax.faces.model.SelectItem;

import model.CategoriaMenu;
import model.ItemMenu;

import org.primefaces.event.FileUploadEvent;

import util.FotoUtil;
import ejb.CategoriaItensMenuFacade;



@ManagedBean(name="itemMB")
@ViewScoped
public class ItemMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CategoriaItensMenuFacade categoriaItensMenuFacade;
	
	private ItemMenu itemMenu = new ItemMenu();
	private List<ItemMenu> itemMenuList = new ArrayList<ItemMenu>();
	
	private List<CategoriaMenu> categoriaList = new ArrayList<CategoriaMenu>();
	private CategoriaMenu categoriaMenu = new CategoriaMenu();
	private String selectIdCategoria = "";
	private List<SelectItem> selectItemsCategorias = new ArrayList<SelectItem>();
	private List<String> selectItensTable = new ArrayList<String>();
	
	public ItemMB() {

	}

	
	@PostConstruct
	public void ini(){
		this.itemMenu = new ItemMenu();
		this.itemMenuList = categoriaItensMenuFacade.findAllItem();
		this.categoriaList = categoriaItensMenuFacade.findAllCategoria();
		this.selectIdCategoria = "";
		this.selectItemsCategorias = new ArrayList<SelectItem>();
		this.selectItensTable = new ArrayList<String>();
		this.carregaCategorias();
	}

	
	private void carregaCategorias(){
		for (CategoriaMenu categoriaMenu : this.categoriaList) {
			selectItemsCategorias.add(new SelectItem(categoriaMenu.getIdCategoriaMenu(), categoriaMenu.getNome()));
			selectItensTable.add(categoriaMenu.getNome());
		}
		
	}
	
	public void salvar(){
		
		if (this.selectIdCategoria != null && !this.selectIdCategoria.equals("")){
			this.itemMenu.setCategoriaMenu(this.categoriaItensMenuFacade.findCategoria(new Long(this.selectIdCategoria)));
		} else {
			this.itemMenu.setCategoriaMenu(null);
		}
		
		if(this.itemMenu.getIdItemMenu() != null){
			try{
				ItemMenu itemMenuPersist = this.categoriaItensMenuFacade.findItem(this.itemMenu.getIdItemMenu());
				itemMenuPersist.setDesconto(this.itemMenu.getDesconto());
				itemMenuPersist.setDescricaoIngredientes(this.itemMenu.getDescricaoIngredientes());
				itemMenuPersist.setFoto(this.itemMenu.getFoto());
				itemMenuPersist.setNome(this.itemMenu.getNome());
				itemMenuPersist.setValor(this.itemMenu.getValor());
				itemMenuPersist.setDirFoto(FotoUtil.getDiFoto(itemMenu));
				this.itemMenu = this.categoriaItensMenuFacade.updateItem(this.itemMenu);
				String info = "Item alterado com Sucesso ";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,itemMenu.getNome() + info , null));
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			
			
		} else {
			try{
				this.categoriaItensMenuFacade.saveItem(this.itemMenu);
				this.atualizaDirFoto();
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			String info = "Item Cadastrado com Sucesso ";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,itemMenu.getNome() + info , null));
		}
		FotoUtil.criarAtualizarFoto(this.itemMenu);
		this.ini();
	}

	
	


	public void editar(ItemMenu itemMenu){
		this.itemMenu = itemMenu;
		if (this.itemMenu.getCategoriaMenu() != null){
			this.categoriaMenu = this.categoriaItensMenuFacade.findCategoria(this.getItemMenu().getCategoriaMenu().getIdCategoriaMenu());
			this.selectIdCategoria = this.categoriaMenu.getIdCategoriaMenu().toString();
		} else {
			this.categoriaMenu = null;
		}
		
	}
	
	
	
	 
	 public void handleFileUpload(FileUploadEvent event) {
	        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
	        this.itemMenu.setFoto(event.getFile().getContents());
	        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	 
	 private void atualizaDirFoto() {
			if(this.itemMenu != null && this.itemMenu.getIdItemMenu() != null){
				itemMenu.setDirFoto(FotoUtil.getDiFoto(itemMenu));
				itemMenu = this.categoriaItensMenuFacade.updateItem(itemMenu);
			}
		}
	 
	
	public CategoriaItensMenuFacade getCategoriaItensMenuFacade() {
		return categoriaItensMenuFacade;
	}


	public void setCategoriaItensMenuFacade(
			CategoriaItensMenuFacade categoriaItensMenuFacade) {
		this.categoriaItensMenuFacade = categoriaItensMenuFacade;
	}





	public List<CategoriaMenu> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<CategoriaMenu> categoriaList) {
		this.categoriaList = categoriaList;
	}


	public ItemMenu getItemMenu() {
		return itemMenu;
	}


	public void setItemMenu(ItemMenu itemMenu) {
		this.itemMenu = itemMenu;
	}


	public List<ItemMenu> getItemMenuList() {
		return itemMenuList;
	}


	public void setItemMenuList(List<ItemMenu> itemMenuList) {
		this.itemMenuList = itemMenuList;
	}


	public CategoriaMenu getCategoriaMenu() {
		return categoriaMenu;
	}


	public void setCategoriaMenu(CategoriaMenu categoriaMenu) {
		this.categoriaMenu = categoriaMenu;
	}


	public String getSelectIdCategoria() {
		return selectIdCategoria;
	}


	public void setSelectIdCategoria(String selectIdCategoria) {
		this.selectIdCategoria = selectIdCategoria;
	}


	public List<SelectItem> getSelectItemsCategorias() {
		return selectItemsCategorias;
	}


	public void setSelectItemsCategorias(List<SelectItem> selectItemsCategorias) {
		this.selectItemsCategorias = selectItemsCategorias;
	}


	public List<String> getSelectItensTable() {
		return selectItensTable;
	}


	public void setSelectItensTable(List<String> selectItensTable) {
		this.selectItensTable = selectItensTable;
	}
	
	
	
	
	
	
}
