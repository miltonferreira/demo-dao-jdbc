package application;

import java.util.Date;
import java.util.List;

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
		
		System.out.println("\n=== Test 2: seller findByDepartment ===");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== Test 3: seller findAll ===");
		list = sellerDao.findAll();
		
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		/* >>>>>>>>>>>>>>>>>>>> insere um novo vendedor ao sistema <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		System.out.println("\n=== Test 4: seller insert ===");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New Id = " + newSeller.getId());
		*/
		
		// >>>>>>>>>>>>>>>>>>>> insere um novo vendedor ao sistema <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		System.out.println("\n=== Test 5: seller insert ===");
		seller = sellerDao.findById(1); // carrega os dados do ID selecionado
		seller.setName("Martha Wayne"); // muda o nome do vendedor
		sellerDao.update(seller); // passa o obj para atualizar no banco de dados
		System.out.println("Update Completed");
		
		/*
		Department obj = new Department(1, "Books");
		Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, obj);
		
		SellerDao sellerDao = DaoFactory.createSellerDao(); //Program nao conhece a implementação, somente a interface
		
		System.out.println(seller);
		*/
		
		
		
		
		

	}

}
