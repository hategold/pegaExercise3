package yt.item3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import yt.item3.DbConnCtrl;;

public class BrandDaoImpl implements BrandDao {

	private Connection conn; //TODO static?

	public BrandDaoImpl() {
		conn = DbConnCtrl.getConnection();
	}

	@Override
	public boolean selectBrandByID(Brand brand) throws SQLException {
		String query = "SELECT * FROM brands WHERE BrandId= ?";

		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1, brand.getBrandId());

		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {	//set is not empty 
			brand.setBrandName(resultSet.getString("BrandName"));
			brand.setWebsite(resultSet.getString("Website"));
			brand.setCountry(resultSet.getString("Country"));
			resultSet.close();
			preparedStatement.close();
			return true;
		}

		return false;

	}

	@Override
	public boolean deleteBrand(int brandId) {
		int updateStatus = 0;
		try {
			String sqlStatement = "DELETE FROM brands WHERE BrandId=?";

			PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, brandId);
			updateStatus = preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUpdate(updateStatus);
	}

	@Override
	public boolean updateBrand(Brand brand) throws SQLException {
		String sqlStatement = "UPDATE brands SET BrandName=?, Website=?, Country=? WHERE BrandId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);

		preparedStatement.setString(1, brand.getBrandName());
		preparedStatement.setString(2, brand.getWebsite());
		preparedStatement.setString(3, brand.getCountry());
		preparedStatement.setInt(4, brand.getBrandId());
		int updateStatus = preparedStatement.executeUpdate();
		preparedStatement.close();

		return isUpdate(updateStatus);
	}

	private boolean isUpdate(int updateStatus) {
		if (updateStatus == 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean insertBrand(Brand brand) throws SQLException {
		String sqlStatement = "INSERT brands (BrandName, Website, Country) VALUES (?,?,?)";

		PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
		preparedStatement.setString(1, brand.getBrandName());
		preparedStatement.setString(2, brand.getWebsite());
		preparedStatement.setString(3, brand.getCountry());
		int updateStatus = preparedStatement.executeUpdate();
		preparedStatement.close();
		return isUpdate(updateStatus);
	}

	@Override
	public List<Brand> readFullBrands() {
		List<Brand> brandList = new ArrayList<Brand>();

		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM brands");
			while (resultSet.next()) {
				Brand brand = new Brand(Integer.valueOf(resultSet.getString("brandId")));
				brand.setBrandName(resultSet.getString("brandName")).setCountry(resultSet.getString("country")).setWebsite(resultSet.getString("website"));
				brandList.add(brand);
			}
			resultSet.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return brandList;

	}

}
