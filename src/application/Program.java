package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao(); // faz conexão com banco de dados
		
		System.out.println("=== Test 1: seller findById ===");
		Seller seller = sellerDao.findById(3); // pesquisa funcionario pela ID e retorna resultado
		
		System.out.println(seller);
		
		/*
		Department obj = new Department(1, "Books");
		Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, obj);
		
		SellerDao sellerDao = DaoFactory.createSellerDao(); //Program nao conhece a implementação, somente a interface
		
		System.out.println(seller);
		*/
		
		
		
		
		

	}

}
