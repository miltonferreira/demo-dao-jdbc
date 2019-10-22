package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {

	void insert(Seller obj); // operação responsavel por inserir o obj no banco de dados

	void update(Seller obj);

	void deleteById(Integer id); //
	
	// retorna um Seller, responsavel por pegar ID e consultar no BD
	// se existir retorna Seller, caso contrario retorna null
	Seller findById(Integer id);

	List<Seller> findAll(); // retorna todos os departamentos em uma lista

}
