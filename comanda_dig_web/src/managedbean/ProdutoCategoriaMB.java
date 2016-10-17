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
import model.Produto;
import ejb.CategoriaItensMenuFacade;



@ManagedBean(name="ProdutoCategoriaMB")
@ViewScoped
public class ProdutoCategoriaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CategoriaItensMenuFacade categoriaItensMenuFacade;
	
	private String selectIdCategoria = "";
	private List<SelectItem> selectItemsCategorias = new ArrayList<SelectItem>();
	private List<CategoriaMenu> categoriaList = new ArrayList<CategoriaMenu>();
	private DualListModel<Produto> dualListModel = new DualListModel<Produto>();
	private CategoriaMenu selectcategoriaMenu = new CategoriaMenu();
	
	
	
	@PostConstruct
	public void ini(){
		selectIdCategoria = "";
		selectItemsCategorias = new ArrayList<SelectItem>();
		categoriaList = this.categoriaItensMenuFacade.findAllCategoria();
		for (CategoriaMenu categoriaMenu : this.categoriaList) {
			selectItemsCategorias.add(new SelectItem(categoriaMenu.getIdCategoriaMenu(), categoriaMenu.getNome()));
		}
		dualListModel.setSource(categoriaItensMenuFacade.findAllItem());
		dualListModel.setTarget(new ArrayList<Produto>());
		selectcategoriaMenu = new CategoriaMenu();
	}

	
	public void selectCat(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			CategoriaMenu cat = this.categoriaItensMenuFacade.findCategoria(new Long(this.selectIdCategoria));
			this.selectcategoriaMenu = cat;
			dualListModel.setTarget(cat.getItemMenuList());
		}
		List<Produto> list = new ArrayList<Produto>();
		List<Produto> listAll = this.categoriaItensMenuFacade.findAllItem(); 
		for (Produto produto : listAll) {
			if(produto.getCategoriaMenu() == null || !produto.getCategoriaMenu().getIdCategoriaMenu().toString().equals(this.getSelectIdCategoria())){
				list.add(produto);
			}
		}
		dualListModel.setSource(list);
	}
	
	
	public void salvar(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			List<Produto> list = dualListModel.getTarget();
			CategoriaMenu categoriaMenu = this.categoriaItensMenuFacade.findCategoria(new Long(selectIdCategoria));
			if (list!= null && !list.isEmpty()){
				for (Produto produto : list) {
					produto = this.categoriaItensMenuFacade.findItem(produto.getIdProduto());
					produto.setCategoriaMenu(categoriaMenu);
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


	public void setCategoriaItensMenuFacade(CategoriaItensMenuFacade categoriaItensMenuFacade) {
		this.categoriaItensMenuFacade = categoriaItensMenuFacade;
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


	public List<CategoriaMenu> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<CategoriaMenu> categoriaList) {
		this.categoriaList = categoriaList;
	}


	public DualListModel<Produto> getDualListModel() {
		return dualListModel;
	}


	public void setDualListModel(DualListModel<Produto> dualListModel) {
		this.dualListModel = dualListModel;
	}


	public CategoriaMenu getSelectcategoriaMenu() {
		return selectcategoriaMenu;
	}


	public void setSelectcategoriaMenu(CategoriaMenu selectcategoriaMenu) {
		this.selectcategoriaMenu = selectcategoriaMenu;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	


	
	
	
	
}
