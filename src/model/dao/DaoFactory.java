package model.dao;

import model.dao.Impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(); // nao expoe a implementa��o, somente a interface
	}

}
