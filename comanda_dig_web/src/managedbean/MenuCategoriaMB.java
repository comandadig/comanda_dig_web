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

import org.primefaces.model.DualListModel;

import model.Menu;
import model.Categoria;
import ejb.ProdutoFacade;



@ManagedBean(name="menuCategoriaMB")
@ViewScoped
public class MenuCategoriaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ProdutoFacade produtoFacade;
	
	private String selectIdCategoria = "";
	private List<SelectItem> selectItemsCategorias = new ArrayList<SelectItem>();
	private List<Menu> categoriaList = new ArrayList<Menu>();
	private DualListModel<Categoria> dualListModel = new DualListModel<Categoria>();
	private Menu selectcategoriaMenu = new Menu();
	
	
	@PostConstruct
	public void ini(){
		selectIdCategoria = "";
		selectItemsCategorias = new ArrayList<SelectItem>();
		categoriaList = this.produtoFacade.findAllMenu();
		for (Menu categoriaMenu : this.categoriaList) {
			selectItemsCategorias.add(new SelectItem(categoriaMenu.getIdMenu(), categoriaMenu.getNome()));
		}
		dualListModel.setSource(produtoFacade.findAllCategoria());
		dualListModel.setTarget(new ArrayList<Categoria>());
		selectcategoriaMenu = new Menu();
	}

	
	public void selectCat(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			Menu cat = this.produtoFacade.findMenu(new Long(this.selectIdCategoria));
			this.selectcategoriaMenu = cat;
			dualListModel.setTarget(cat.getCategorias());
		}
		List<Categoria> list = new ArrayList<Categoria>();
		List<Categoria> listAll = this.produtoFacade.findAllCategoria(); 
		for (Categoria itemMenu : listAll) {
			if(itemMenu.getCategoriaMaster() == null || !itemMenu.getCategoriaMaster().getIdMenu().toString().equals(this.getSelectIdCategoria())){
				list.add(itemMenu);
			}
		}
		
		
		
		dualListModel.setSource(list);
	}
	
	
	public void salvar(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			List<Categoria> list = dualListModel.getTarget();
			Menu categoriaMenu = this.produtoFacade.findMenu(new Long(selectIdCategoria));
			if (list!= null && !list.isEmpty()){
				for (Categoria categoria : list) {
					categoria = this.produtoFacade.findCategoria(categoria.getIdCategoria());
					categoria.setCategoriaMaster(categoriaMenu);
				}
			} else {
				categoriaMenu.setCategorias(null);
			}
			categoriaMenu.setCategorias(list);
			try{
				this.produtoFacade.updateMenu(categoriaMenu);
				String info = "Categoria: " + categoriaMenu.getNome()+ " alterada com Sucesso ";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO, info , null));
			}catch (Exception e){
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage() , null));
				return;
			}
			
		}
		 this.ini();
	}
	
	public ProdutoFacade getCategoriaItensMenuFacade() {
		return produtoFacade;
	}


	public void setCategoriaItensMenuFacade(
			ProdutoFacade produtoFacade) {
		this.produtoFacade = produtoFacade;
	}


	public List<SelectItem> getSelectItemsCategorias() {
		return selectItemsCategorias;
	}


	public void setSelectItemsCategorias(List<SelectItem> selectItemsCategorias) {
		this.selectItemsCategorias = selectItemsCategorias;
	}



	public String getSelectIdCategoria() {
		return selectIdCategoria;
	}


	public void setSelectIdCategoria(String selectIdCategoria) {
		this.selectIdCategoria = selectIdCategoria;
	}


	public List<Menu> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<Menu> categoriaList) {
		this.categoriaList = categoriaList;
	}


	public DualListModel<Categoria> getDualListModel() {
		return dualListModel;
	}


	public void setDualListModel(DualListModel<Categoria> dualListModel) {
		this.dualListModel = dualListModel;
	}


	public Menu getSelectcategoriaMenu() {
		return selectcategoriaMenu;
	}


	public void setSelectcategoriaMenu(Menu selectcategoriaMenu) {
		this.selectcategoriaMenu = selectcategoriaMenu;
	}




	
	
	
	
}
