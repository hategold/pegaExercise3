package yt.item3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShoesDBController
 */
@WebServlet("/ShoesDBController")
public class ShoesDBController extends HttpServlet {

	private BrandDao brandDao;

	private static final long serialVersionUID = 1L;

	public static final String LIST_BRANDS = "/listBrands.jsp";

	public static final String INSERT_OR_EDIT = "/modifyBrand.jsp";

	public static enum ActionEnum {
		INSERT, EDIT, DELETE, LIST
	};

	public ShoesDBController() {
		brandDao = new BrandDaoImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");
		RequestDispatcher view = request.getRequestDispatcher(excuteAction(action, request));
		view.forward(request, response);
	}

	private String dispatchToList(HttpServletRequest request) {
		request.setAttribute("brandList", brandDao.readFullBrands());
		return LIST_BRANDS;
	}

	private String excuteAction(String action, HttpServletRequest request) {

		Map<CountryCode, String> countryCodeMap = buildCountryMap();

		try {
			if (ActionEnum.DELETE.name().equalsIgnoreCase(action)) {

				brandDao.deleteBrand(Integer.valueOf(request.getParameter("brandId")));
				return dispatchToList(request);
			}
			if (ActionEnum.EDIT.name().equalsIgnoreCase(action)) {

				Brand brand = new Brand(Integer.parseInt(request.getParameter("brandId")));
				if (!brandDao.selectBrandByID(brand)) //got no data
					return dispatchToList(request);

				request.setAttribute("brand", brand);
				request.setAttribute("countryCodeMap", countryCodeMap);
				return INSERT_OR_EDIT;
			}

			if (ActionEnum.INSERT.name().equalsIgnoreCase(action)) {
				request.setAttribute("countryCodeMap", countryCodeMap);
				return INSERT_OR_EDIT;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
		}
		return dispatchToList(request);

	}

	private Map<CountryCode, String> buildCountryMap() {
		Map<CountryCode, String> countryCodeMap = new EnumMap<CountryCode, String>(CountryCode.class);
		BufferedReader countryCodeReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("/countryCodeToFullName")));

		if (countryCodeReader != null) {
			try {
				String line = "";
				while ((line = countryCodeReader.readLine()) != null) {
					int cammaIndex = line.indexOf(",");
					String countryCodeString = line.substring(0, cammaIndex).trim();
					String countryName = line.substring(cammaIndex + 1).replace("\"", "").trim();
					try {
						countryCodeMap.put(CountryCode.valueOf(countryCodeString), countryName);
					} catch (IllegalArgumentException e) {
						continue;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return countryCodeMap;
	}

	private boolean isCreate(String id) {
		try {
			Integer.valueOf(id);
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Brand brand = new Brand(request.getParameter("brandName"));

		try {
			if (isCreate(request.getParameter("brandId"))) {
				brand.setCountry(request.getParameter("country")).setWebsite(request.getParameter("website"));
				brandDao.insertBrand(brand);
				response.sendRedirect("/webExercise3/ShoesDBController?action=list"); //end post
				return;
			}

			brand.setBrandId(Integer.valueOf(request.getParameter("brandId"))).setCountry(request.getParameter("country"))
					.setWebsite(request.getParameter("website"));
			brandDao.updateBrand(brand);
			response.sendRedirect("/webExercise3/ShoesDBController?action=list");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
