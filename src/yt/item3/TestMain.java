package yt.item3;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import yt.item3.DbConnCtrl;;

public class TestMain {

	public static void main(String[] args) {
		try {
			Connection con = DbConnCtrl.getConnection();
			Statement stat = con.createStatement();
			ResultSet resultSet = stat.executeQuery("SELECT * FROM brands");
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numOfColumns = metaData.getColumnCount();

			for (int i = 1; i <= numOfColumns; i++) {
				System.out.printf("%-8s ", metaData.getColumnName(i));
				System.out.println();
			}

			while (resultSet.next()) {
				for (int i = 1; i <= numOfColumns; i++) {
					System.out.printf("%-8s ", resultSet.getObject(i));
					System.out.println();
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
