package model.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
	
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

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
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentId")); // pega o id do departamento
				dep.setName(rs.getString("DepName")); // pega o nome do departamento
				
				// cria um obj vendedor puxando do banco de dados -----------------------------
				Seller obj = new Seller();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				obj.setDepartment(dep); // associa��o do obj vendedor com o obj departamento
				
				return obj; // se pegou as infos retorna o vendedor
			}
			
			return null; // rs.next() retornou vazio, indicando que n�o existe dados da id
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			
		}
		
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
