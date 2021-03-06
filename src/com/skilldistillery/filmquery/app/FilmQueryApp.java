package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
  
  DatabaseAccessor db = new DatabaseAccessorObject();

  public static void main(String[] args) {
    FilmQueryApp app = new FilmQueryApp();
//    app.test();
    app.launch();
  }

  private void test() {
    Film film = db.getFilmById(1);
    System.out.println(film);
    Actor actor = db.getActorById(1);
    System.out.println(actor);
  }

  private void launch() {
    Scanner input = new Scanner(System.in);
    
    startUserInterface(input);
    
    input.close();
  }

  private void startUserInterface(Scanner input) {
    System.out.println("Would you like to: ");
    System.out.println("\t1. Look up a film by id");
    System.out.println("\t2. Look up a film by keyword");
    System.out.println("\t3. Exit");
    int selection = input.nextInt();
    
    if (selection == 1) {
    		System.out.print("Enter a film id: ");
    		int id = input.nextInt(); 
    		Film film = db.getFilmById(id);
    		
    		if (film == null) {
    			System.out.println("\nFilm not found.\n");
    		}
    		else {
    			film.printFilmCast(film); 
    		}
    		launch();    			
    }
    else if (selection == 2) {
    		System.out.print("Enter a keyword: ");
    		String keyword = input.next(); 
    		List<Film> films = db.lookupFilm(keyword);
    		
    		if (films.size() == 0) {
    			System.out.println("\nFilm not found.\n");
    			launch();
    		}
    		else {
    			//See "todo" on line 51, add loop for list
    			for (Film film : films) {
    				film.printFilmCast(film);
    			}
    			launch();
    		}
    }
    else if (selection == 3) {
    		System.out.println("Goodbye");
    		System.exit(0);
    }
    else {
    		System.out.println("That is not a valid option. Please try again.\n");
    		launch();
    }
  }

}
