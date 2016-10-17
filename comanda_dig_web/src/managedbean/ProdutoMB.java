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
import model.Produto;

import org.primefaces.event.FileUploadEvent;

import util.FotoUtil;
import ejb.CategoriaItensMenuFacade;



@ManagedBean(name="produtoMB")
@ViewScoped
public class ProdutoMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CategoriaItensMenuFacade categoriaItensMenuFacade;
	
	private Produto produto = new Produto();
	private List<Produto> produtoList = new ArrayList<Produto>();
	
	private List<CategoriaMenu> categoriaList = new ArrayList<CategoriaMenu>();
	private CategoriaMenu categoriaMenu = new CategoriaMenu();
	private String selectIdCategoria = "";
	private List<SelectItem> selectItemsCategorias = new ArrayList<SelectItem>();
	private List<String> selectItensTable = new ArrayList<String>();
	
	public ProdutoMB() {

	}

	
	@PostConstruct
	public void ini(){
		this.produto = new Produto();
		this.produtoList = categoriaItensMenuFacade.findAllItem();
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
			this.produto.setCategoriaMenu(this.categoriaItensMenuFacade.findCategoria(new Long(this.selectIdCategoria)));
		} else {
			this.produto.setCategoriaMenu(null);
		}
		
		if(this.produto.getIdProduto() != null){
			try{
				Produto produtoPersist = this.categoriaItensMenuFacade.findItem(this.produto.getIdProduto());
				produtoPersist.setDesconto(this.produto.getDesconto());
				produtoPersist.setDescricaoIngredientes(this.produto.getDescricaoIngredientes());
				produtoPersist.setFoto(this.produto.getFoto());
				produtoPersist.setNome(this.produto.getNome());
				produtoPersist.setValor(this.produto.getValor());
				produtoPersist.setDirFoto(FotoUtil.getDiFoto(produto));
				this.produto = this.categoriaItensMenuFacade.updateItem(this.produto);
				String info = "Item alterado com Sucesso ";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,produto.getNome() + info , null));
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			
			
		} else {
			try{
				this.categoriaItensMenuFacade.saveItem(this.produto);
				this.atualizaDirFoto();
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			String info = "Item Cadastrado com Sucesso ";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,produto.getNome() + info , null));
		}
		FotoUtil.criarAtualizarFoto(this.produto);
		this.ini();
	}

	
	


	public void editar(Produto produto){
		this.produto = produto;
		if (this.produto.getCategoriaMenu() != null){
			this.categoriaMenu = this.categoriaItensMenuFacade.findCategoria(this.getProduto().getCategoriaMenu().getIdCategoriaMenu());
			this.selectIdCategoria = this.categoriaMenu.getIdCategoriaMenu().toString();
		} else {
			this.categoriaMenu = null;
		}
		
	}
	
	
	
	 
	 public void handleFileUpload(FileUploadEvent event) {
	        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
	        this.produto.setFoto(event.getFile().getContents());
	        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	 
	 private void atualizaDirFoto() {
			if(this.produto != null && this.produto.getIdProduto() != null){
				produto.setDirFoto(FotoUtil.getDiFoto(produto));
				produto = this.categoriaItensMenuFacade.updateItem(produto);
			}
		}


	public CategoriaItensMenuFacade getCategoriaItensMenuFacade() {
		return categoriaItensMenuFacade;
	}


	public void setCategoriaItensMenuFacade(CategoriaItensMenuFacade categoriaItensMenuFacade) {
		this.categoriaItensMenuFacade = categoriaItensMenuFacade;
	}


	public Produto getProduto() {
		return produto;
	}


	public void setProduto(Produto produto) {
		this.produto = produto;
	}


	public List<Produto> getProdutoList() {
		return produtoList;
	}


	public void setProdutoList(List<Produto> produtoList) {
		this.produtoList = produtoList;
	}


	public List<CategoriaMenu> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<CategoriaMenu> categoriaList) {
		this.categoriaList = categoriaList;
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	 
	
	
	
	
	
	
	
}
