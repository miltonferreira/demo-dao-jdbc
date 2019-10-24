package model.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn; // tem a conexao em qualquer lugar dentro da classe
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	// insere novo vendedor a tabela no BD -----------------------------
	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null; // responsavel por inseri as informações no BD
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			// ao executar atualização de vendedor, mostra as linhas afetadas
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				
				ResultSet rs = st.getGeneratedKeys(); //????????
				
				if(rs.next()) {
					int id = rs.getInt(1); // vai modificar a primeira coluna
					obj.setId(id); // entrega o ID gerado para o obj
				}
				DB.closeResultSet(rs); // foi aberto somente dentro desse if
				
			} else {
				// se não modificou nem uma linha, lança um erro
				throw new DbException("Unexpected error! No rows affected!");
				
			}
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}
	
	// atualização de dados de um vendedor -----------------------------
	@Override
	public void update(Seller obj) {
		
		PreparedStatement st = null; // responsavel por inseri as informações no BD
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			// ao executar atualização de vendedor
			st.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}
	
	// procura vendedor pelo ID
	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ? ");
			
			st.setInt(1, id); // recebe o id do parametro no lugar do ?
			rs = st.executeQuery(); // executa uma consulta SQL com retorno
			
			// verifica se veio algum resultado
			if(rs.next()) {
				
				// cria um obj departamento puxando do banco de dados -------------------------
				Department dep = instantiateDepartment(rs);
				
				// cria um obj vendedor puxando do banco de dados -----------------------------
				Seller obj = instantiateSeller(rs, dep);
				
				return obj; // se pegou as infos retorna o vendedor
			}
			
			return null; // rs.next() retornou vazio, indicando que não existe dados da id
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			
		}
		
	}
	
	// cria um obj vendedor puxando do banco de dados -----------------------------
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep); // associação do obj vendedor com o obj departamento
		return obj;
		
	}
	
	// cria um obj departamento puxando do banco de dados -------------------------
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		
		Department dep =  new Department();
		dep.setId(rs.getInt("DepartmentId")); // pega o id do departamento
		dep.setName(rs.getString("DepName")); // pega o nome do departamento
		return dep;
		
	}
	
	// busca todos os vendedores em ordena por nome -----------------------------
	@Override
	public List<Seller> findAll() {
		
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery(); // executa uma consulta SQL com retorno
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>(); // guarda qualquer departamento instanciado
			
			// verifica se veio algum resultado, podendo ser 0 ou mais valores 
			while(rs.next()) {
				
				// procura um Id do departamento, se não tiver retorna null
				Department dep = map.get(rs.getInt("DepartmentId")); 
				
				// primeira vez que entra no while dep vai esta vazio
				if(dep == null) {
					// cria um obj departamento puxando do banco de dados -------------------------
					dep = instantiateDepartment(rs); // instancia um departamento
					map.put(rs.getInt("DepartmentId"), dep); // salva o id do departamento dentro do map
				}
				
				// cria um obj vendedor puxando do banco de dados -----------------------------
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
				
			}
			
			return list; // se pegou as infos retorna o vendedor
			//return null; // rs.next() retornou vazio, indicando que não existe dados da id
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			
		}
	}
	
	// busca vendedores pela id do departamento retornando uma lista de vendedores
	@Override
	public List<Seller> findByDepartment(Department department) {
		
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId()); // recebe o id do parametro no lugar do ?
			rs = st.executeQuery(); // executa uma consulta SQL com retorno
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>(); // guarda qualquer departamento instanciado
			
			// verifica se veio algum resultado, podendo ser 0 ou mais valores 
			while(rs.next()) {
				
				// procura um Id do departamento, se não tiver retorna null
				Department dep = map.get(rs.getInt("DepartmentId")); 
				
				// primeira vez que entra no while dep vai esta vazio
				if(dep == null) {
					// cria um obj departamento puxando do banco de dados -------------------------
					dep = instantiateDepartment(rs); // instancia um departamento
					map.put(rs.getInt("DepartmentId"), dep); // salva o id do departamento dentro do map
				}
				
				// cria um obj vendedor puxando do banco de dados -----------------------------
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
				
			}
			
			return list; // se pegou as infos retorna o vendedor
			//return null; // rs.next() retornou vazio, indicando que não existe dados da id
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			
		}
		
	}

}
