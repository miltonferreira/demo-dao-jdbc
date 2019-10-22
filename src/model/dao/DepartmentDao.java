package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {

	void insert(Department obj); // operação responsavel por inserir o obj no banco de dados

	void update(Department obj);

	void deleteById(Integer id); //
	
	// retorna um Department, responsavel por pegar ID e consultar no BD
	// se existir retorna Department, caso contrario retorna null
	Department findById(Integer id);

	List<Department> findAll(); // retorna todos os departamentos em uma lista

}
