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

import model.CategoriaMenu;

import org.primefaces.event.FileUploadEvent;

import util.FotoUtil;
import ejb.CategoriaItensMenuFacade;



@ManagedBean(name="categoriaMB")
@ViewScoped
public class CategoriaMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CategoriaItensMenuFacade categoriaItensMenuFacade;
	private CategoriaMenu categoriaMenu = new CategoriaMenu();
	private List<CategoriaMenu> categoriaList = new ArrayList<CategoriaMenu>();
	
	
	
	public CategoriaMB() {

	}

	
	@PostConstruct
	public void ini(){
		this.categoriaMenu = new CategoriaMenu();
		this.categoriaList = categoriaItensMenuFacade.findAllCategoria();
	}

	
	public void salvar(){
		
		if(this.getCategoriaMenu().getIdCategoriaMenu() != null){
			try{
				CategoriaMenu categoriaMenuPersist = this.categoriaItensMenuFacade.findCategoria(this.categoriaMenu.getIdCategoriaMenu());
				categoriaMenuPersist.setDescontoCat(this.getCategoriaMenu().getDescontoCat());
				categoriaMenuPersist.setDescricao(this.getCategoriaMenu().getDescricao());
				categoriaMenuPersist.setFoto(this.getCategoriaMenu().getFoto());
				categoriaMenuPersist.setNome(this.getCategoriaMenu().getNome());
				this.categoriaMenu = this.categoriaItensMenuFacade.updateCategoria(this.categoriaMenu);
				String info = "Categoria alterada com Sucesso ";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,categoriaMenu.getNome() + info , null));
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			
			
		} else {
			try{
				this.categoriaItensMenuFacade.saveCategoria(this.categoriaMenu);
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			String info = "Categoria Cadastrada com Sucesso ";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,categoriaMenu.getNome() + info , null));
		}
		
		FotoUtil.criarAtualizarFoto(this.categoriaMenu);
		this.ini();
	}

	
	 public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        this.categoriaMenu.setFoto(event.getFile().getContents());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	
	public CategoriaItensMenuFacade getCategoriaItensMenuFacade() {
		return categoriaItensMenuFacade;
	}


	public void setCategoriaItensMenuFacade(
			CategoriaItensMenuFacade categoriaItensMenuFacade) {
		this.categoriaItensMenuFacade = categoriaItensMenuFacade;
	}


	public CategoriaMenu getCategoriaMenu() {
		return categoriaMenu;
	}


	public void setCategoriaMenu(CategoriaMenu categoriaMenu) {
		this.categoriaMenu = categoriaMenu;
	}


	public List<CategoriaMenu> getCategoriaList() {
		return categoriaList;
	}


	public void setCategoriaList(List<CategoriaMenu> categoriaList) {
		this.categoriaList = categoriaList;
	}
	
	
	
	
	
	
}
