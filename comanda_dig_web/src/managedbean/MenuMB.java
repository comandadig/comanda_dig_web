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
import model.Menu;
import util.FotoUtil;



@ManagedBean(name="menuMB")
@ViewScoped
public class MenuMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ProdutoFacade produtoFacade;
	private Menu menu = new Menu();
	private List<Menu> menuList = new ArrayList<Menu>();
	
	
	
	@PostConstruct
	public void ini(){
		this.menu = new Menu();
		this.menuList = produtoFacade.findAllCategoriaMaster();
	}

	
	public void salvar(){
		
		if(this.menu.getIdMenu() != null){
			try{
				menu = produtoFacade.updateCategoriaMaster(menu);
				String info = "Categoria alterada com Sucesso ";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,menu.getNome() + info , null));
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			
			
		} else {
			try{
				this.produtoFacade.saveCategoriaMaster(this.menu);
				this.atualizaDirFoto();
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			String info = "Categoria Cadastrada com Sucesso ";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,menu.getNome() + info , null));
		}
		
		FotoUtil.criarAtualizarFoto(this.menu);
		this.ini();
	}

	
	 private void atualizaDirFoto() {
		if (this.menu != null && this.menu.getIdMenu() != null){
			menu.setDirfoto(FotoUtil.getDiFoto(menu));
			menu = produtoFacade.updateCategoriaMaster(menu);
		 }
	}


	public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        this.menu.setFoto(event.getFile().getContents());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


	public ProdutoFacade getProdutoFacade() {
		return produtoFacade;
	}


	public void setProdutoFacade(ProdutoFacade produtoFacade) {
		this.produtoFacade = produtoFacade;
	}


	public Menu getMenu() {
		return menu;
	}


	public void setMenu(Menu menu) {
		this.menu = menu;
	}


	public List<Menu> getMenuList() {
		return menuList;
	}


	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
		
	
	
	
	
	
}
