package dao;

import model.Celular;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class CelularDAO extends DAO {	
	public CelularDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Celular celular) {
		boolean status = false;
		try {
			String sql = "INSERT INTO celular (descricao, preco, quantidade, datafabricacao, datavalidade) "
		               + "VALUES ('" + celular.getDescricao() + "', "
		               + celular.getPreco() + ", " + celular.getQuantidade() + ", ?, ?);";
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(celular.getDataFabricacao()));
			st.setDate(2, Date.valueOf(celular.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Celular get(int id) {
		Celular celular = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM celular WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 celular = new Celular(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
	                				   rs.getInt("quantidade"), 
	        			               rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			               rs.getDate("datavalidade").toLocalDate());
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return celular;
	}
	
	
	public List<Celular> get() {
		return get("");
	}

	
	public List<Celular> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Celular> getOrderByDescricao() {
		return get("descricao");		
	}
	
	
	public List<Celular> getOrderByPreco() {
		return get("preco");		
	}
	
	
	private List<Celular> get(String orderBy) {
		List<Celular> celulars = new ArrayList<Celular>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM celular" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Celular p = new Celular(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
	        			                rs.getInt("quantidade"),
	        			                rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			                rs.getDate("datavalidade").toLocalDate());
	            celulars.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return celulars;
	}
	
	
	public boolean update(Celular celular) {
		boolean status = false;
		try {  
			String sql = "UPDATE celular SET descricao = '" + celular.getDescricao() + "', "
					   + "preco = " + celular.getPreco() + ", " 
					   + "quantidade = " + celular.getQuantidade() + ","
					   + "datafabricacao = ?, " 
					   + "datavalidade = ? WHERE id = " + celular.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(celular.getDataFabricacao()));
			st.setDate(2, Date.valueOf(celular.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM celular WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}