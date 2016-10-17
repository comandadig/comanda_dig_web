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

import org.primefaces.event.FileUploadEvent;

import ejb.ProdutoFacade;
import model.CategoriaMaster;
import util.FotoUtil;



@ManagedBean(name="categoriaMasterMB")
@ViewScoped
public class CategoriaMasterMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ProdutoFacade produtoFacade;
	private CategoriaMaster categoriaMaster = new CategoriaMaster();
	private List<CategoriaMaster> categoriaList = new ArrayList<CategoriaMaster>();
	
	
	
	@PostConstruct
	public void ini(){
		this.categoriaMaster = new CategoriaMaster();
		this.categoriaList = produtoFacade.findAllCategoriaMaster();
	}

	
	public void salvar(){
		
		if(this.categoriaMaster.getIdCategoriaMaster() != null){
			try{
				categoriaMaster = produtoFacade.updateCategoriaMaster(categoriaMaster);
				String info = "Categoria alterada com Sucesso ";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,categoriaMaster.getNome() + info , null));
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			
			
		} else {
			try{
				this.produtoFacade.saveCategoriaMaster(this.categoriaMaster);
				this.atualizaDirFoto();
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			String info = "Categoria Cadastrada com Sucesso ";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,categoriaMaster.getNome() + info , null));
		}
		
		FotoUtil.criarAtualizarFoto(this.categoriaMaster);
		this.ini();
	}

	
	 private void atualizaDirFoto() {
		if (this.categoriaMaster != null && this.categoriaMaster.getIdCategoriaMaster() != null){
			categoriaMaster.setDirfoto(FotoUtil.getDiFoto(categoriaMaster));
			categoriaMaster = produtoFacade.updateCategoriaMaster(categoriaMaster);
		 }
	}


	public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        this.categoriaMaster.setFoto(event.getFile().getContents());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	
	public ProdutoFacade getCategoriaItensMenuFacade() {
		return produtoFacade;
	}


	public void setCategoriaItensMenuFacade(
			ProdutoFacade produtoFacade) {
		this.produtoFacade = produtoFacade;
	}


	public CategoriaMaster getCategoriaMaster() {
		return categoriaMaster;
	}


	public void setCategoriaMaster(CategoriaMaster categoriaMaster) {
		this.categoriaMaster = categoriaMaster;
	}


	public List<CategoriaMaster> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<CategoriaMaster> categoriaList) {
		this.categoriaList = categoriaList;
	}


	
	
	
	
	
	
	
}
