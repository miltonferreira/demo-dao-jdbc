package model.dao;

import db.DB;
import model.dao.Impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection()); // nao expoe a implementa��o, somente a interface
	}

}
