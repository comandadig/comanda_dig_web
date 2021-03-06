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

import model.Menu;
import model.Categoria;
import model.Produto;
import model.User;
import ejb.ProdutoFacade;
import ejb.UserFacade;



@WebListener
public class FotoUtil implements ServletContextListener{
	
	private static final String PASTA_ALL_IMAGENS = "/imagens";
	private static final String PASTA_DEFAULT_FOTOS = "/fotos";
	private static final String TIPE_USU = "usu_";
	private static final String TIPE_CATEGORIA = "cat_";
	private static final String TIPE_CATEGORIA_MASTER = "menu_";
	private static final String TIPE_PRODUTO = "prod_";
	private static final String EXTENCAO = ".jpg";

	@EJB
	private UserFacade userFacade;
	@EJB
	private ProdutoFacade produtoFacade;
	
	public static String CONTEXT_PATH_FOTOS = "C:\\comanda_dig\\imagens\\";
	public static String CONTEXT_PATH_IMAGENS = "C:\\comanda_dig\\imagens\\";


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
			
			List<Categoria> catList = this.produtoFacade.findAllCategoria();
			for (Categoria categoria : catList) {
				criarAtualizarFoto(categoria);
			}
			
			List<Produto> produtos = this.produtoFacade.findAllProduto();
			for (Produto produto : produtos) {
				criarAtualizarFoto(produto);
			}
		}

		System.out.println("INICIALIZOU FOTOS....");
	}

	
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("FUDEU A BAGA�A....");
	}
	
	public static void criarAtualizarFoto(Produto produto) {
		if (produto != null && produto.getIdProduto() != null){
			String arquivo = CONTEXT_PATH_FOTOS + File.separator+ TIPE_PRODUTO + produto.getIdProduto()+EXTENCAO;
			if (produto.getFoto() != null){
				criaArquivo(produto.getFoto(), arquivo);
			} else {
				criaArquivo(getFotoDefault(TIPE_PRODUTO),arquivo);
			}
		}
	}

	public static void criarAtualizarFoto(Categoria categoria) {
		if (categoria != null && categoria.getIdCategoria() != null){
			String arquivo = CONTEXT_PATH_FOTOS + File.separator+ TIPE_CATEGORIA + categoria.getIdCategoria()+EXTENCAO;
			if (categoria.getFoto() != null){
				criaArquivo(categoria.getFoto(), arquivo);
			} else {
				criaArquivo(getFotoDefault(TIPE_CATEGORIA),arquivo);
			}
		}
	}

	public static void criarAtualizarFoto(Menu categoriaMenu) {
		if (categoriaMenu != null && categoriaMenu.getIdMenu() != null){
			String arquivo = CONTEXT_PATH_FOTOS + File.separator+ TIPE_CATEGORIA_MASTER + categoriaMenu.getIdMenu()+EXTENCAO;
			if (categoriaMenu.getFoto() != null){
				criaArquivo(categoriaMenu.getFoto(), arquivo);
			} else {
				criaArquivo(getFotoDefault(TIPE_CATEGORIA_MASTER),arquivo);
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
			} else if (tipo.equals(FotoUtil.TIPE_PRODUTO)){
				arquivo = CONTEXT_PATH_IMAGENS + File.separator+ "default.jpg";
			} else if (tipo.equals(FotoUtil.TIPE_CATEGORIA_MASTER)) {
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
		else if (object instanceof Categoria) return PASTA_DEFAULT_FOTOS +  "/" + TIPE_CATEGORIA + ((Categoria)object).getIdCategoria()+EXTENCAO;
		else if (object instanceof Produto) return PASTA_DEFAULT_FOTOS +  "/"+ TIPE_PRODUTO + ((Produto)object).getIdProduto()+EXTENCAO;
		else if (object instanceof Menu) return PASTA_DEFAULT_FOTOS +  "/"+ TIPE_CATEGORIA_MASTER + ((Menu)object).getIdMenu()+EXTENCAO;
		return null;
	}

}
