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
	private List<CategoriaMaster> categoriaList = new ArrayList<CategoriaMaster>();
	private DualListModel<Categoria> dualListModel = new DualListModel<Categoria>();
	private CategoriaMaster selectcategoriaMenu = new CategoriaMaster();
	
	
	@PostConstruct
	public void ini(){
		selectIdCategoria = "";
		selectItemsCategorias = new ArrayList<SelectItem>();
		categoriaList = this.produtoFacade.findAllCategoriaMaster();
		for (CategoriaMaster categoriaMenu : this.categoriaList) {
			selectItemsCategorias.add(new SelectItem(categoriaMenu.getIdCategoriaMaster(), categoriaMenu.getNome()));
		}
		dualListModel.setSource(produtoFacade.findAllCategoria());
		dualListModel.setTarget(new ArrayList<Categoria>());
		selectcategoriaMenu = new CategoriaMaster();
	}

	
	public void selectCat(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			CategoriaMaster cat = this.produtoFacade.findCategoriaMaster(new Long(this.selectIdCategoria));
			this.selectcategoriaMenu = cat;
			dualListModel.setTarget(cat.getCategoriaMenus());
		}
		List<Categoria> list = new ArrayList<Categoria>();
		List<Categoria> listAll = this.produtoFacade.findAllCategoria(); 
		for (Categoria itemMenu : listAll) {
			if(itemMenu.getCategoriaMaster() == null || !itemMenu.getCategoriaMaster().getIdCategoriaMaster().toString().equals(this.getSelectIdCategoria())){
				list.add(itemMenu);
			}
		}
		dualListModel.setSource(list);
	}
	
	
	public void salvar(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			List<Categoria> list = dualListModel.getTarget();
			CategoriaMaster categoriaMenu = this.produtoFacade.findCategoriaMaster(new Long(selectIdCategoria));
			if (list!= null && !list.isEmpty()){
				for (Categoria categoria : list) {
					categoria = this.produtoFacade.findCategoria(categoria.getIdCategoria());
					categoria.setCategoriaMaster(categoriaMenu);
				}
			} else {
				categoriaMenu.setCategoriaMenus(null);
			}
			categoriaMenu.setCategoriaMenus(list);
			try{
				this.produtoFacade.updateCategoriaMaster(categoriaMenu);
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


	public List<CategoriaMaster> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<CategoriaMaster> categoriaList) {
		this.categoriaList = categoriaList;
	}


	public DualListModel<Categoria> getDualListModel() {
		return dualListModel;
	}


	public void setDualListModel(DualListModel<Categoria> dualListModel) {
		this.dualListModel = dualListModel;
	}


	public CategoriaMaster getSelectcategoriaMenu() {
		return selectcategoriaMenu;
	}


	public void setSelectcategoriaMenu(CategoriaMaster selectcategoriaMenu) {
		this.selectcategoriaMenu = selectcategoriaMenu;
	}




	
	
	
	
}
