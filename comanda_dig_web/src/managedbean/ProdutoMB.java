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

import model.Categoria;
import model.Produto;

import org.primefaces.event.FileUploadEvent;

import util.FotoUtil;
import ejb.ProdutoFacade;



@ManagedBean(name="produtoMB")
@ViewScoped
public class ProdutoMB extends AbstractMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ProdutoFacade produtoFacade;
	
	private Produto produto = new Produto();
	private List<Produto> produtoList = new ArrayList<Produto>();
	
	private List<Categoria> categoriaList = new ArrayList<Categoria>();
	private Categoria categoria = new Categoria();
	private String selectIdCategoria = "";
	private List<SelectItem> selectItemsCategorias = new ArrayList<SelectItem>();
	private List<String> selectItensTable = new ArrayList<String>();
	
	public ProdutoMB() {

	}

	
	@PostConstruct
	public void ini(){
		this.produto = new Produto();
		this.produtoList = produtoFacade.findAllProduto();
		this.categoriaList = produtoFacade.findAllCategoria();
		this.selectIdCategoria = "";
		this.selectItemsCategorias = new ArrayList<SelectItem>();
		this.selectItensTable = new ArrayList<String>();
		this.carregaCategorias();
	}

	
	private void carregaCategorias(){
		for (Categoria categoria : this.categoriaList) {
			selectItemsCategorias.add(new SelectItem(categoria.getIdCategoria(), categoria.getNome()));
			selectItensTable.add(categoria.getNome());
		}
		
	}
	
	public void salvar(){
		
		if (this.selectIdCategoria != null && !this.selectIdCategoria.equals("")){
			this.produto.setCategoria(this.produtoFacade.findCategoria(new Long(this.selectIdCategoria)));
		} else {
			this.produto.setCategoria(null);
		}
		
		if(this.produto.getIdProduto() != null){
			try{
				Produto produtoPersist = this.produtoFacade.findProduto(this.produto.getIdProduto());
				produtoPersist.setDesconto(this.produto.getDesconto());
				produtoPersist.setDescricaoIngredientes(this.produto.getDescricaoIngredientes());
				produtoPersist.setFoto(this.produto.getFoto());
				produtoPersist.setNome(this.produto.getNome());
				produtoPersist.setValor(this.produto.getValor());
				produtoPersist.setDirFoto(FotoUtil.getDiFoto(produto));
				this.produto = this.produtoFacade.updateProduto(this.produto);
				String info = "Item alterado com Sucesso ";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,produto.getNome() + info , null));
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			
			
		} else {
			try{
				this.produtoFacade.saveProduto(this.produto);
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
		if (this.produto.getCategoria() != null){
			this.categoria = this.produtoFacade.findCategoria(this.getProduto().getCategoria().getIdCategoria());
			this.selectIdCategoria = this.categoria.getIdCategoria().toString();
		} else {
			this.categoria = null;
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
				produto = this.produtoFacade.updateProduto(produto);
			}
		}


	public ProdutoFacade getCategoriaItensMenuFacade() {
		return produtoFacade;
	}


	public void setCategoriaItensMenuFacade(ProdutoFacade produtoFacade) {
		this.produtoFacade = produtoFacade;
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


	public List<Categoria> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<Categoria> categoriaList) {
		this.categoriaList = categoriaList;
	}


	public Categoria getCategoriaMenu() {
		return categoria;
	}


	public void setCategoriaMenu(Categoria categoria) {
		this.categoria = categoria;
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
