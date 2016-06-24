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

import model.CategoriaMenu;
import model.ItemMenu;
import ejb.CategoriaItensMenuFacade;



@ManagedBean(name="itensCategoriaMB")
@ViewScoped
public class ItensCategoriaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CategoriaItensMenuFacade categoriaItensMenuFacade;
	
	private String selectIdCategoria = "";
	private List<SelectItem> selectItemsCategorias = new ArrayList<SelectItem>();
	private List<CategoriaMenu> categoriaList = new ArrayList<CategoriaMenu>();
	private DualListModel<ItemMenu> dualListModel = new DualListModel<ItemMenu>();
	private CategoriaMenu selectcategoriaMenu = new CategoriaMenu();
	
	
	public ItensCategoriaMB() {
	}

	
	@PostConstruct
	public void ini(){
		selectIdCategoria = "";
		selectItemsCategorias = new ArrayList<SelectItem>();
		categoriaList = this.categoriaItensMenuFacade.findAllCategoria();
		for (CategoriaMenu categoriaMenu : this.categoriaList) {
			selectItemsCategorias.add(new SelectItem(categoriaMenu.getIdCategoriaMenu(), categoriaMenu.getNome()));
		}
		dualListModel.setSource(categoriaItensMenuFacade.findAllItem());
		dualListModel.setTarget(new ArrayList<ItemMenu>());
		selectcategoriaMenu = new CategoriaMenu();
	}

	
	public void selectCat(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			CategoriaMenu cat = this.categoriaItensMenuFacade.findCategoria(new Long(this.selectIdCategoria));
			this.selectcategoriaMenu = cat;
			dualListModel.setTarget(cat.getItemMenuList());
		}
		List<ItemMenu> list = new ArrayList<ItemMenu>();
		List<ItemMenu> listAll = this.categoriaItensMenuFacade.findAllItem(); 
		for (ItemMenu itemMenu : listAll) {
			if(itemMenu.getCategoriaMenu() == null || !itemMenu.getCategoriaMenu().getIdCategoriaMenu().toString().equals(this.getSelectIdCategoria())){
				list.add(itemMenu);
			}
		}
		dualListModel.setSource(list);
	}
	
	
	public void salvar(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			List<ItemMenu> list = dualListModel.getTarget();
			CategoriaMenu categoriaMenu = this.categoriaItensMenuFacade.findCategoria(new Long(selectIdCategoria));
			if (list!= null && !list.isEmpty()){
				for (ItemMenu itemMenu : list) {
					itemMenu = this.categoriaItensMenuFacade.findItem(itemMenu.getIdItemMenu());
					itemMenu.setCategoriaMenu(categoriaMenu);
				}
			} else {
				categoriaMenu.setItemMenuList(null);
			}
			categoriaMenu.setItemMenuList(list);
			try{
				this.categoriaItensMenuFacade.updateCategoria(categoriaMenu);
				String info = "Itens da Categoria: " + categoriaMenu.getNome()+ " alterados com Sucesso ";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO, info , null));
			}catch (Exception e){
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage() , null));
				return;
			}
			
		}
		 this.ini();
	}
	
	public CategoriaItensMenuFacade getCategoriaItensMenuFacade() {
		return categoriaItensMenuFacade;
	}


	public void setCategoriaItensMenuFacade(
			CategoriaItensMenuFacade categoriaItensMenuFacade) {
		this.categoriaItensMenuFacade = categoriaItensMenuFacade;
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


	public List<CategoriaMenu> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<CategoriaMenu> categoriaList) {
		this.categoriaList = categoriaList;
	}


	public DualListModel<ItemMenu> getDualListModel() {
		return dualListModel;
	}


	public void setDualListModel(DualListModel<ItemMenu> dualListModel) {
		this.dualListModel = dualListModel;
	}


	public CategoriaMenu getSelectcategoriaMenu() {
		return selectcategoriaMenu;
	}


	public void setSelectcategoriaMenu(CategoriaMenu selectcategoriaMenu) {
		this.selectcategoriaMenu = selectcategoriaMenu;
	}


	
	
	
	
}
