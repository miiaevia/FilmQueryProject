package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static String URL = "jdbc:mysql://localhost:3306/sdvid";
	private static String user = "student";
	private static String pass = "student";

	@Override
	public Film getFilmById(int filmId) {
		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT * FROM film WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				String desc = rs.getString(3);
				int releaseYear = rs.getInt(4);
				int languageId = rs.getInt(5);
				int rentalDuration = rs.getInt(6);
				double rentalRate = rs.getDouble(7);
				int movieLength = rs.getInt(8);
				double replacementCost = rs.getDouble(9);
				String rating = rs.getString(10);
				String specialFeatures = rs.getString(11);
				List<Actor> actors = getActorsByFilmId(filmId);

				film = new Film(id, title, desc, releaseYear, languageId, rentalDuration, rentalRate, movieLength, replacementCost, rating, specialFeatures, actors);
				
			}
			conn.close();
			stmt.close();
			rs.close();
		}
		catch (SQLException e) {
			System.err.println("Unable to access database. Exiting...");
			e.printStackTrace();
			System.exit(1);
		}
		return film;
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			System.err.println("Unable to load DB driver. Exiting...");
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public Actor getActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT * FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				String firstName = rs.getString(2); 
				String lastName = rs.getString(3);
				
				Actor newActor = new Actor(id, firstName, lastName);
				actor = newActor;
			}
			
			conn.close();
			stmt.close();
			rs.close();
		}
		catch (SQLException e) {
			System.err.println("Unable to access database. Exiting...");
			e.printStackTrace();
			System.exit(1);
		}

		return actor;
	}

	@Override
	public List<Actor> getActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<Actor>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT a.id, a.first_name, a.last_name FROM actor a JOIN film_actor fa ON a.id = fa.actor_id WHERE fa.film_id = ? ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String firstName = rs.getString(2);
				String lastName = rs.getString(3);
			//	System.out.println(id + " " + firstName + " " + lastName);
				Actor actor = new Actor(id, firstName, lastName);
				actors.add(actor);
			}
			
			conn.close();
			stmt.close();
			rs.close();
		}
		catch (SQLException e) {
			System.err.println("Unable to access DB. Exiting.");
			e.printStackTrace();
			System.exit(1);
		}
		
		return actors;
	}
	
	@Override
	public List<Film> lookupFilm(String keyword) {
		List<Film> films = new ArrayList<>(); 
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			
			String newKeyword = "%" + keyword + "%";
			String sql = "SELECT id, title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features FROM film WHERE title LIKE ? OR description LIKE ? ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, newKeyword);
			stmt.setString(2, newKeyword);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				String desc = rs.getString(3);
				int releaseYear = rs.getInt(4);
				int languageId = rs.getInt(5);
				int rentalDuration = rs.getInt(6);
				double rentalRate = rs.getDouble(7);
				int movieLength = rs.getInt(8);
				double replacementCost = rs.getDouble(9);
				String rating = rs.getString(10);
				String specialFeatures = rs.getString(11);
//				System.out.println(id + " " +  title + " " + desc);
				List<Actor> actors = getActorsByFilmId(id);
				
				Film film = new Film(id, title, desc, releaseYear, languageId, rentalDuration, rentalRate, movieLength, replacementCost, rating, specialFeatures, actors);				
				films.add(film);
			
//			System.out.println(id + " " + title + " " + desc + " " 
//			+ releaseYear + " " + languageId + " " + rentDur + " " + rentRate+ " "
//			+ length + " " + replacementCost + " " + rating + " " + specialFeat );
			}
			
			rs.close();
			stmt.close();
			conn.close();
		}
		catch (SQLException e) {
			System.err.println("Unable to access DB.");
			e.printStackTrace();
		}
		return films;
	}
}
