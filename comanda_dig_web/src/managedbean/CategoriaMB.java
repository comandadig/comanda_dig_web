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

import model.Categoria;

import org.primefaces.event.FileUploadEvent;

import util.FotoUtil;
import ejb.ProdutoFacade;



@ManagedBean(name="categoriaMB")
@ViewScoped
public class CategoriaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ProdutoFacade produtoFacade;
	private Categoria categoria = new Categoria();
	private List<Categoria> categoriaList = new ArrayList<Categoria>();
	
	
	@PostConstruct
	public void ini(){
		this.categoria = new Categoria();
		this.categoriaList = produtoFacade.findAllCategoria();
	}

	
	public void salvar(){
		if(this.getCategoria().getIdCategoria() != null){
			try{
				Categoria categoriaPersist = this.produtoFacade.findCategoria(this.categoria.getIdCategoria());
				categoriaPersist.setDesconto(this.getCategoria().getDesconto());
				categoriaPersist.setDescricao(this.getCategoria().getDescricao());
				categoriaPersist.setFoto(this.getCategoria().getFoto());
				categoriaPersist.setNome(this.getCategoria().getNome());
				categoriaPersist.setDirfoto(FotoUtil.getDiFoto(categoria));
				this.categoria = this.produtoFacade.updateCategoria(this.categoria);
				String info = "Categoria alterada com Sucesso ";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,categoria.getNome() + info , null));
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
		} else {
			try{
				this.produtoFacade.saveCategoria(this.categoria);
				this.atualizaDirFoto();
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			String info = "Categoria Cadastrada com Sucesso ";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,categoria.getNome() + info , null));
		}
		
		FotoUtil.criarAtualizarFoto(this.categoria);
		this.ini();
	}

	
	 private void atualizaDirFoto() {
		 if (this.categoria != null && this.categoria.getIdCategoria() != null){
			 categoria.setDirfoto(FotoUtil.getDiFoto(categoria));
			 categoria = produtoFacade.updateCategoria(categoria);
		 }
	}


	public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        this.categoria.setFoto(event.getFile().getContents());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


	public ProdutoFacade getProdutoFacade() {
		return produtoFacade;
	}


	public void setProdutoFacade(ProdutoFacade produtoFacade) {
		this.produtoFacade = produtoFacade;
	}


	public Categoria getCategoria() {
		return categoria;
	}


	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}


	public List<Categoria> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<Categoria> categoriaList) {
		this.categoriaList = categoriaList;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
	
	
}
