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

import model.Categoria;
import model.Produto;
import ejb.ProdutoFacade;



@ManagedBean(name="ProdutoCategoriaMB")
@ViewScoped
public class ProdutoCategoriaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ProdutoFacade produtoFacade;
	
	private String selectIdCategoria = "";
	private List<SelectItem> selectItemsCategorias = new ArrayList<SelectItem>();
	private List<Categoria> categoriaList = new ArrayList<Categoria>();
	private DualListModel<Produto> dualListModel = new DualListModel<Produto>();
	private Categoria selectcategoriaMenu = new Categoria();
	
	
	
	@PostConstruct
	public void ini(){
		selectIdCategoria = "";
		selectItemsCategorias = new ArrayList<SelectItem>();
		categoriaList = this.produtoFacade.findAllCategoria();
		for (Categoria categoria : this.categoriaList) {
			selectItemsCategorias.add(new SelectItem(categoria.getIdCategoria(), categoria.getNome()));
		}
		dualListModel.setSource(produtoFacade.findAllProduto());
		dualListModel.setTarget(new ArrayList<Produto>());
		selectcategoriaMenu = new Categoria();
	}

	
	public void selectCat(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			Categoria cat = this.produtoFacade.findCategoria(new Long(this.selectIdCategoria));
			this.selectcategoriaMenu = cat;
			dualListModel.setTarget(cat.getProdutosList());
		}
		List<Produto> list = new ArrayList<Produto>();
		List<Produto> listAll = this.produtoFacade.findAllProduto(); 
		for (Produto produto : listAll) {
			if(produto.getCategoria() == null || !produto.getCategoria().getIdCategoria().toString().equals(this.getSelectIdCategoria())){
				list.add(produto);
			}
		}
		dualListModel.setSource(list);
	}
	
	
	public void salvar(){
		if (selectIdCategoria != null && !selectIdCategoria.equals("")){
			List<Produto> list = dualListModel.getTarget();
			Categoria categoria = this.produtoFacade.findCategoria(new Long(selectIdCategoria));
			if (list!= null && !list.isEmpty()){
				for (Produto produto : list) {
					produto = this.produtoFacade.findProduto(produto.getIdProduto());
					produto.setCategoria(categoria);
				}
			} else {
				categoria.setProdutosList(null);
			}
			categoria.setProdutosList(list);
			try{
				this.produtoFacade.updateCategoria(categoria);
				String info = "Itens da Categoria: " + categoria.getNome()+ " alterados com Sucesso ";
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


	public void setCategoriaItensMenuFacade(ProdutoFacade produtoFacade) {
		this.produtoFacade = produtoFacade;
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


	public List<Categoria> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<Categoria> categoriaList) {
		this.categoriaList = categoriaList;
	}


	public DualListModel<Produto> getDualListModel() {
		return dualListModel;
	}


	public void setDualListModel(DualListModel<Produto> dualListModel) {
		this.dualListModel = dualListModel;
	}


	public Categoria getSelectcategoriaMenu() {
		return selectcategoriaMenu;
	}


	public void setSelectcategoriaMenu(Categoria selectcategoriaMenu) {
		this.selectcategoriaMenu = selectcategoriaMenu;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	


	
	
	
	
}
