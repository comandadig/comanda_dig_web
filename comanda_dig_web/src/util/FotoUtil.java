package util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.CategoriaMenu;
import model.ItemMenu;
import model.User;
import ejb.CategoriaItensMenuFacade;
import ejb.UserFacade;



@WebListener
public class FotoUtil implements ServletContextListener{
	
	private static final String PASTA_ALL_IMAGENS = "/imagens";
	private static final String PASTA_DEFAULT_FOTOS = "/fotos";
	private static final String TIPE_USU = "usu_";
	private static final String TIPE_CATEGORIA = "cat_";
	private static final String TIPE_ITEM = "item_";
	private static final String EXTENCAO = ".jpg";

	@EJB
	private UserFacade userFacade;
	@EJB
	private CategoriaItensMenuFacade categoriaItensMenuFacade;
	
	public static String CONTEXT_PATH_FOTOS = "";
	public static String CONTEXT_PATH_IMAGENS = "";


	public void contextInitialized(ServletContextEvent event) {
		
		FotoUtil.CONTEXT_PATH_FOTOS = (event.getServletContext().getRealPath(PASTA_DEFAULT_FOTOS));
		FotoUtil.CONTEXT_PATH_IMAGENS = (event.getServletContext().getRealPath(PASTA_ALL_IMAGENS));
		File folder = new File(FotoUtil.CONTEXT_PATH_FOTOS);
		
		if (!folder.exists()){
			
			folder.mkdir();
			
			List<User> list = userFacade.findAll();
			for (User user : list) {
				criarAtualizarFoto(user);
			}
			
			List<CategoriaMenu> catList = this.categoriaItensMenuFacade.findAllCategoria();
			for (CategoriaMenu categoriaMenu : catList) {
				criarAtualizarFoto(categoriaMenu);
			}
			
			List<ItemMenu> itemMenus = this.categoriaItensMenuFacade.findAllItem();
			for (ItemMenu itemMenu : itemMenus) {
				criarAtualizarFoto(itemMenu);
			}
		}

		System.out.println("INICIALIZOU FOTOS....");
	}

	
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("FUDEU A BAGAÇA....");
	}
	
	public static void criarAtualizarFoto(ItemMenu itemMenu) {
		if (itemMenu != null && itemMenu.getIdItemMenu() != null){
			String arquivo = CONTEXT_PATH_FOTOS + File.separator+ TIPE_ITEM + itemMenu.getIdItemMenu()+EXTENCAO;
			if (itemMenu.getFoto() != null){
				criaArquivo(itemMenu.getFoto(), arquivo);
			} else {
				criaArquivo(getFotoDefault(TIPE_ITEM),arquivo);
			}
		}
	}

	public static void criarAtualizarFoto(CategoriaMenu categoriaMenu) {
		if (categoriaMenu != null && categoriaMenu.getIdCategoriaMenu() != null){
			String arquivo = CONTEXT_PATH_FOTOS + File.separator+ TIPE_CATEGORIA + categoriaMenu.getIdCategoriaMenu()+EXTENCAO;
			if (categoriaMenu.getFoto() != null){
				criaArquivo(categoriaMenu.getFoto(), arquivo);
			} else {
				criaArquivo(getFotoDefault(TIPE_CATEGORIA),arquivo);
			}
		}
	}

	public static void criarAtualizarFoto(User user){
		if (user != null && user.getIdUser() != null){
			String arquivo = CONTEXT_PATH_FOTOS + File.separator+ TIPE_USU + user.getIdUser()+EXTENCAO;
			if (user.getFoto() != null){
				criaArquivo(user.getFoto(), arquivo);
			} else {
				criaArquivo(getFotoDefault(TIPE_USU),arquivo);
			}
		}
	}


	private static void criaArquivo(byte[] bytes, String arquivo) {
		FileOutputStream fos;

		try {
			File file = new File(arquivo);
			if (!file.exists()) file.createNewFile();

			fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	

	private static byte[] getFotoDefault(String tipo) {

		BufferedImage imagem = null;
		try {
			String arquivo = "";
			if (tipo.equals(FotoUtil.TIPE_USU)){
				 arquivo = CONTEXT_PATH_IMAGENS + File.separator+ "default_usu.jpg";
			} else if (tipo.equals(FotoUtil.TIPE_CATEGORIA)){
				arquivo = CONTEXT_PATH_IMAGENS + File.separator+ "default.jpg";
			} else if (tipo.equals(FotoUtil.TIPE_ITEM)){
				arquivo = CONTEXT_PATH_IMAGENS + File.separator+ "default.jpg";
			}
			
			imagem = ImageIO.read(new File(arquivo));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		BufferedImage bi = new BufferedImage(imagem.getWidth(null),imagem.getHeight(null),BufferedImage.TYPE_INT_RGB);  
		Graphics bg = bi.getGraphics();  
		bg.drawImage(imagem, 0, 0, null);  
		bg.dispose();  

		ByteArrayOutputStream buff = new ByteArrayOutputStream();         
		try {    
			ImageIO.write(bi, "JPG", buff);    
		} catch (IOException e) {    
			e.printStackTrace();    
		}    
		return buff.toByteArray();        

	}
	
	public static String getDiFoto(Object object){
		
		if (object instanceof User) return  PASTA_DEFAULT_FOTOS + "/"+ TIPE_USU + ((User)object).getIdUser()+EXTENCAO;
		else if (object instanceof CategoriaMenu) return PASTA_DEFAULT_FOTOS +  "/" + TIPE_CATEGORIA + ((CategoriaMenu)object).getIdCategoriaMenu()+EXTENCAO;
		else if (object instanceof ItemMenu) return PASTA_DEFAULT_FOTOS +  "/"+ TIPE_ITEM + ((ItemMenu)object).getIdItemMenu()+EXTENCAO;
		return null;
	}

}
