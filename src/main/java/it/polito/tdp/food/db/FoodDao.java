package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public void loadIdMap (Map<Integer,Food> idMap) {
		String sql = "SELECT * FROM food" ;
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				if (!idMap.containsKey(res.getInt("food_code"))) {
					Food f = new Food(res.getInt("food_code"), res.getString("display_name"));
					idMap.put(f.getFood_code(),f);
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Food> getVertici(Map<Integer, Food> idMap, int porzioni) {
		String sql="SELECT p1.food_code AS f, COUNT(distinct p1.portion_id) AS porzioni "
				+ "FROM `portion` p1 "
				+ "GROUP BY p1.food_code";
		List<Food> result= new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				if (idMap.containsKey(res.getInt("f"))) {
					if(res.getInt("porzioni")<=porzioni) {
						result.add(idMap.get(res.getInt("f")));
					}
				}
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenze(Map<Integer, Food> idMap){
		String sql="SELECT f1.food_code AS fo1, f2.food_code AS fo2, AVG(c.condiment_calories) AS peso "
				+ "FROM food_condiment f1, food_condiment f2, condiment c "
				+ "WHERE f1.food_code > f2.food_code "
				+ "		AND f1.condiment_code = f2.condiment_code AND f1.condiment_code=c.condiment_code "
				+ "GROUP BY f1.food_code, f2.food_code";
		List<Adiacenza> result= new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				if (idMap.containsKey(res.getInt("fo1")) && idMap.containsKey(res.getInt("fo2"))) {
					Adiacenza a = new Adiacenza (idMap.get(res.getInt("fo1")),idMap.get(res.getInt("fo2")),res.getDouble("peso"));
					result.add(a);
				}
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
