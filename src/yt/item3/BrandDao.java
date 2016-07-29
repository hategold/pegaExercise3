package yt.item3;

import java.sql.SQLException;
import java.util.List;

import yt.item3.Brand;

public interface BrandDao {

	public boolean selectBrandByID(Brand brand) throws SQLException;

	public boolean deleteBrand(int brandId) throws SQLException;

	public boolean updateBrand(Brand brand) throws SQLException;

	public boolean insertBrand(Brand brand) throws SQLException;

	public List<Brand> readFullBrands();
}
