package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.CelularDAO;
import model.Celular;
import spark.Request;
import spark.Response;


public class CelularService {

	private CelularDAO celularDAO = new CelularDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_DESCRICAO = 2;
	private final int FORM_ORDERBY_PRECO = 3;
	
	
	public CelularService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Celular(), FORM_ORDERBY_DESCRICAO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Celular(), orderBy);
	}

	
	public void makeForm(int tipo, Celular celular, int orderBy) {
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umCelular = "";
		if(tipo != FORM_INSERT) {
			umCelular += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umCelular += "\t\t<tr>";
			umCelular += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/celular/list/1\">Novo Celular</a></b></font></td>";
			umCelular += "\t\t</tr>";
			umCelular += "\t</table>";
			umCelular += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/celular/";
			String name, descricao, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Celular";
				descricao = "leite, pão, ...";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + celular.getID();
				name = "Atualizar Celular (ID " + celular.getID() + ")";
				descricao = celular.getDescricao();
				buttonLabel = "Atualizar";
			}
			umCelular += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umCelular += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umCelular += "\t\t<tr>";
			umCelular += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umCelular += "\t\t</tr>";
			umCelular += "\t\t<tr>";
			umCelular += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umCelular += "\t\t</tr>";
			umCelular += "\t\t<tr>";
			umCelular += "\t\t\t<td>&nbsp;Descrição: <input class=\"input--register\" type=\"text\" name=\"descricao\" value=\""+ descricao +"\"></td>";
			umCelular += "\t\t\t<td>Preco: <input class=\"input--register\" type=\"text\" name=\"preco\" value=\""+ celular.getPreco() +"\"></td>";
			umCelular += "\t\t\t<td>Quantidade: <input class=\"input--register\" type=\"text\" name=\"quantidade\" value=\""+ celular.getQuantidade() +"\"></td>";
			umCelular += "\t\t</tr>";
			umCelular += "\t\t<tr>";
			umCelular += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\""+ celular.getDataFabricacao().toString() + "\"></td>";
			umCelular += "\t\t\t<td>Data de validade: <input class=\"input--register\" type=\"text\" name=\"dataValidade\" value=\""+ celular.getDataValidade().toString() + "\"></td>";
			umCelular += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umCelular += "\t\t</tr>";
			umCelular += "\t</table>";
			umCelular += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umCelular += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umCelular += "\t\t<tr>";
			umCelular += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Celular (ID " + celular.getID() + ")</b></font></td>";
			umCelular += "\t\t</tr>";
			umCelular += "\t\t<tr>";
			umCelular += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umCelular += "\t\t</tr>";
			umCelular += "\t\t<tr>";
			umCelular += "\t\t\t<td>&nbsp;Descrição: "+ celular.getDescricao() +"</td>";
			umCelular += "\t\t\t<td>Preco: "+ celular.getPreco() +"</td>";
			umCelular += "\t\t\t<td>Quantidade: "+ celular.getQuantidade() +"</td>";
			umCelular += "\t\t</tr>";
			umCelular += "\t\t<tr>";
			umCelular += "\t\t\t<td>&nbsp;Data de fabricação: "+ celular.getDataFabricacao().toString() + "</td>";
			umCelular += "\t\t\t<td>Data de validade: "+ celular.getDataValidade().toString() + "</td>";
			umCelular += "\t\t\t<td>&nbsp;</td>";
			umCelular += "\t\t</tr>";
			umCelular += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-PRODUTO>", umCelular);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Celulars</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/celular/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/celular/list/" + FORM_ORDERBY_DESCRICAO + "\"><b>Descrição</b></a></td>\n" +
        		"\t<td><a href=\"/celular/list/" + FORM_ORDERBY_PRECO + "\"><b>Preço</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Celular> celulars;
		if (orderBy == FORM_ORDERBY_ID) {                 	celulars = celularDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_DESCRICAO) {		celulars = celularDAO.getOrderByDescricao();
		} else if (orderBy == FORM_ORDERBY_PRECO) {			celulars = celularDAO.getOrderByPreco();
		} else {											celulars = celularDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Celular p : celulars) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getID() + "</td>\n" +
            		  "\t<td>" + p.getDescricao() + "</td>\n" +
            		  "\t<td>" + p.getPreco() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/celular/" + p.getID() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/celular/update/" + p.getID() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteCelular('" + p.getID() + "', '" + p.getDescricao() + "', '" + p.getPreco() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-PRODUTO>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String descricao = request.queryParams("descricao");
		float preco = Float.parseFloat(request.queryParams("preco"));
		int quantidade = Integer.parseInt(request.queryParams("quantidade"));
		LocalDateTime dataFabricacao = LocalDateTime.parse(request.queryParams("dataFabricacao"));
		LocalDate dataValidade = LocalDate.parse(request.queryParams("dataValidade"));
		
		String resp = "";
		
		Celular celular = new Celular(-1, descricao, preco, quantidade, dataFabricacao, dataValidade);
		
		if(celularDAO.insert(celular) == true) {
            resp = "Celular (" + descricao + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Celular (" + descricao + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Celular celular = (Celular) celularDAO.get(id);
		
		if (celular != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, celular, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Celular " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Celular celular = (Celular) celularDAO.get(id);
		
		if (celular != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, celular, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Celular " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Celular celular = celularDAO.get(id);
        String resp = "";       

        if (celular != null) {
        	celular.setDescricao(request.queryParams("descricao"));
        	celular.setPreco(Float.parseFloat(request.queryParams("preco")));
        	celular.setQuantidade(Integer.parseInt(request.queryParams("quantidade")));
        	celular.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao")));
        	celular.setDataValidade(LocalDate.parse(request.queryParams("dataValidade")));
        	celularDAO.update(celular);
        	response.status(200); // success
            resp = "Celular (ID " + celular.getID() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Celular (ID \" + celular.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Celular celular = celularDAO.get(id);
        String resp = "";       

        if (celular != null) {
            celularDAO.delete(id);
            response.status(200); // success
            resp = "Celular (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Celular (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}