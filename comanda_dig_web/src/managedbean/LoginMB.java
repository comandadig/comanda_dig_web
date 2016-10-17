package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ejb.UserFacade;
import model.User;
import util.PaginacaoEnum;



@ManagedBean(name="loginMB")
@SessionScoped
public class LoginMB  implements Serializable {
	
	private String hostName = "localhost";
	private static final long serialVersionUID = 1L;
	
	
	@EJB
	private UserFacade usuarioFacade;
	private User usuario =  new  User();
	private String login = new String();
	private String senha = new String();
	
	
	public LoginMB() {

	}

	
	@PostConstruct
	public void ini() {
		this.usuario =  new  User();
		login = new String();
		senha = new String();
	}
	
	
	
	public String login(){
		List<User>  list = usuarioFacade.listarPorLogin(this.login);
		if (list != null && !list.isEmpty()){
			this.usuario = list.get(0);
			if (usuario.getSenha().equals(this.senha)){
				return PaginacaoEnum.HOME.getValue();
			} else {
				String info = "Senha Inválida !";
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR,info, ""));
			}
			
		} else {
			String info = "Login Inválido !";
			FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage(FacesMessage.SEVERITY_ERROR,info, ""));
		}
		
		return null;
	}


	public String getHostName() {
		return hostName;
	}


	public void setHostName(String hostName) {
		this.hostName = hostName;
	}


	public UserFacade getUsuarioFacade() {
		return usuarioFacade;
	}


	public void setUsuarioFacade(UserFacade usuarioFacade) {
		this.usuarioFacade = usuarioFacade;
	}


	public User getUsuario() {
		return usuario;
	}


	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}
	


	
	
	
	
	
	
}
