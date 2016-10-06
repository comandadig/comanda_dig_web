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

import model.CategoriaMaster;
import model.CategoriaMenu;
import ejb.CategoriaItensMenuFacade;



@ManagedBean(name="menuCategoriaMB")
@ViewScoped
public class MenuCategoriaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CategoriaItensMenuFacade categoriaItensMenuFacade;
	
	private String selectIdCategoria = "";
	private List<SelectItem> selectItemsCategorias = new ArrayList<SelectItem>();
	private List<CategoriaMaster> categoriaList = new ArrayList<CategoriaMaster>();
	private DualListModel<CategoriaMenu> dualListModel = new DualListModel<CategoriaMenu>();
	private CategoriaMaster selectcategoriaMenu = new CategoriaMaster();
	
	
	public MenuCategoriaMB() {
	}

	
	@PostConstruct
	public void ini(){
		selectIdCategoria = "";
		selectItemsCategorias = new ArrayList<SelectItem>();
		categoriaList = this.categoriaItensMenuFacade.findAllCategoriaMaster();
		for (CategoriaMaster categoriaMenu : this.categoriaList) {
			selectItemsCategorias.add(new SelectItem(categoriaMenu.getIdCategoriaMaster(), categoriaMenu.getNome()));
		}
		dualListModel.setSource(categoriaItensMenuFacade.findAllCategoria());
		dualListModel.setTarget(new ArrayList<CategoriaMenu>());
		selectcategoriaMenu = new CategoriaMaster();
	}

	
	public void selectCat(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			CategoriaMaster cat = this.categoriaItensMenuFacade.findCategoriaMaster(new Long(this.selectIdCategoria));
			this.selectcategoriaMenu = cat;
			dualListModel.setTarget(cat.getCategoriaMenus());
		}
		List<CategoriaMenu> list = new ArrayList<CategoriaMenu>();
		List<CategoriaMenu> listAll = this.categoriaItensMenuFacade.findAllCategoria(); 
		for (CategoriaMenu itemMenu : listAll) {
			if(itemMenu.getCategoriaMaster() == null || !itemMenu.getCategoriaMaster().getIdCategoriaMaster().toString().equals(this.getSelectIdCategoria())){
				list.add(itemMenu);
			}
		}
		dualListModel.setSource(list);
	}
	
	
	public void salvar(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			List<CategoriaMenu> list = dualListModel.getTarget();
			CategoriaMaster categoriaMenu = this.categoriaItensMenuFacade.findCategoriaMaster(new Long(selectIdCategoria));
			if (list!= null && !list.isEmpty()){
				for (CategoriaMenu itemMenu : list) {
					itemMenu = this.categoriaItensMenuFacade.findCategoria(itemMenu.getIdCategoriaMenu());
					itemMenu.setCategoriaMaster(categoriaMenu);
				}
			} else {
				categoriaMenu.setCategoriaMenus(null);
			}
			categoriaMenu.setCategoriaMenus(list);
			try{
				this.categoriaItensMenuFacade.updateCategoriaMaster(categoriaMenu);
				String info = "Categoria: " + categoriaMenu.getNome()+ " alterada com Sucesso ";
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


	public List<CategoriaMaster> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<CategoriaMaster> categoriaList) {
		this.categoriaList = categoriaList;
	}


	public DualListModel<CategoriaMenu> getDualListModel() {
		return dualListModel;
	}


	public void setDualListModel(DualListModel<CategoriaMenu> dualListModel) {
		this.dualListModel = dualListModel;
	}


	public CategoriaMaster getSelectcategoriaMenu() {
		return selectcategoriaMenu;
	}


	public void setSelectcategoriaMenu(CategoriaMaster selectcategoriaMenu) {
		this.selectcategoriaMenu = selectcategoriaMenu;
	}




	
	
	
	
}
