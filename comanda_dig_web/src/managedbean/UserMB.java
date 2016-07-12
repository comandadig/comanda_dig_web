package managedbean;

import java.io.ByteArrayInputStream;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import model.Endereco;
import model.SalarioUser;
import model.TipoUser;
import model.User;



import util.FotoUtil;
import ejb.TipoUserFacade;
import ejb.UserFacade;



@ManagedBean(name="userMB")
@ViewScoped
public class UserMB  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private UserFacade userFacade;
	@EJB
	private TipoUserFacade tipoUserFacade;
	
	private User user =  new  User();
	private List<User> usarioList = new ArrayList<User>();
	private String[] selectedTipoUsers;   
    private List<String> tipoUsers = new ArrayList<String>();
	private Endereco endereco = new Endereco();
	private SalarioUser salarioUser = new SalarioUser();
	
	
	public UserMB() {

	}
	
	
	
	@PostConstruct
	public void ini(){
		
		this.usarioList = userFacade.findAll();
		
		this.user =  new  User();
		this.tipoUsers = new ArrayList<String>();
		this.endereco = new Endereco();
		this.salarioUser = new SalarioUser();
		List<TipoUser>tipoUsersList = tipoUserFacade.findAll();
		for (TipoUser tipoUser : tipoUsersList) {
			tipoUsers.add(tipoUser.getDescricao());
		}
	}
	
	
	
	public void salvar(){
		
		
		
		this.user.setEndereco(this.endereco);
		this.user.setSalarioUser(this.salarioUser);
		
		
		// add novo usuario 
		if (user.getIdUser() == null){
			

			if (!this.validaEmail(this.user.getEmail())){
				String info = " E-mail já Cadastrado..";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR,"E-mail " + user.getEmail() + info , null));
				return;
			}
			
			
			if (!this.validaLogin(this.user.getLogin())){
				String info = " Login já Cadastrado..";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR,"Login " + user.getLogin() + info, null));
				return;
			}
			
			try{
				this.userFacade.save(user);
				this.atualizaDirFoto();
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			
			String info = "Usuário Cadastrado com Sucesso ";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,user.getLogin() + info , null));
		
			// update user	
		} else {
			User userPersist =  this.userFacade.find(user.getIdUser());
			
			if (userPersist.getEmail().equalsIgnoreCase(this.user.getEmail())){
				userPersist.setEmail(this.user.getEmail());
			} else {
				if (!this.validaEmail(this.user.getEmail())){
					String info = " E-mail Já Cadastrado..";
					FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getEmail()+ info , null));
					return;
				}
			}
			
			if (userPersist.getLogin().equalsIgnoreCase(this.user.getLogin())){
				userPersist.setLogin(this.user.getLogin());
			} else {
				if (!this.validaLogin(this.user.getLogin())){
					String info = " Login Já Cadastrado..";
					FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getLogin() +  info, null ));
					return;
				}
			}
			
			try{
				user = this.userFacade.update(user);
			}catch (Exception e){
				String info = e.getMessage();
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_FATAL, info , null));
				return;
			}
			
			String info = " Usuário Alterado com Sucesso";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO, user.getLogin() + info, null));
		}
		
		FotoUtil.criarAtualizarFoto(user);
		this.ini();
	}
	
	
	


	private void atualizaDirFoto() {
		if(this.user != null && this.user.getIdUser() != null){
			user.setDirFoto(FotoUtil.getDiFoto(user));
			user = this.userFacade.update(user);
		}
	}



	public void excluir(User User){
		this.userFacade.delete(User);
		String info = "Usuário Excluido com Sucesso";
		FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_INFO,info,"" ));
		this.ini();
	}
	
	
	
	 public void handleFileUpload(FileUploadEvent event) {
	        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
	        user.setFoto(event.getFile().getContents());
	        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	 
	 
	public StreamedContent getStreamedImageById(User user){
		if (user != null && user.getFoto() != null){
			return new DefaultStreamedContent(new ByteArrayInputStream(user.getFoto()),"image/jpg");
		}
		return null;
	}
	
	
	private boolean validaEmail(String email){
		List<User> list = userFacade.listarPorEmail(email);
		return list.isEmpty();
		
	}
	
	private boolean validaLogin(String login){
		List<User> list = userFacade.listarPorLogin(login);
		return list.isEmpty();
	}
	    
	
	
	public void selectUser(User user){
		this.user = user;
		System.out.println(user.getIdUser());
		this.endereco = this.userFacade.findEnderecoByUser(user.getIdUser());
		this.salarioUser = this.userFacade.findSalarioByUser(user.getIdUser());
		if (this.endereco == null) this.endereco = new Endereco();
		if (this.salarioUser == null) this.salarioUser = new SalarioUser();
	}
	
	public void limparSal(){
		this.salarioUser = new SalarioUser();
	}

	public List<User> getUsarioList() {
		return usarioList;
	}


	public void setUsarioList(List<User> usarioList) {
		this.usarioList = usarioList;
	}


	public UserFacade getUserFacade() {
		return userFacade;
	}


	public void setUserFacade(UserFacade userFacade) {
		this.userFacade = userFacade;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public TipoUserFacade getTipoUserFacade() {
		return tipoUserFacade;
	}


	public void setTipoUserFacade(TipoUserFacade tipoUserFacade) {
		this.tipoUserFacade = tipoUserFacade;
	}




	public String[] getSelectedTipoUsers() {
		return selectedTipoUsers;
	}


	public void setSelectedTipoUsers(String[] selectedTipoUsers) {
		this.selectedTipoUsers = selectedTipoUsers;
	}


	public List<String> getTipoUsers() {
		return tipoUsers;
	}


	public void setTipoUsers(List<String> tipoUsers) {
		this.tipoUsers = tipoUsers;
	}


	public Endereco getEndereco() {
		return endereco;
	}


	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}


	public SalarioUser getSalarioUser() {
		return salarioUser;
	}


	public void setSalarioUser(SalarioUser salarioUser) {
		this.salarioUser = salarioUser;
	}
	
	private int percent = 0;


	public int getPercent() {
		return percent;
	}


	public void setPercent(int percent) {
		this.percent = percent;
	}

	
}
