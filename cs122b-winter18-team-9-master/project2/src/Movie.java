
public class Movie {
	
	private String dirname;
	private String id;
	private String title;
	private int year;
	private String[] genres;
	
	public Movie(String dirname, String id, String title, int year, String genre) {
		this.dirname = dirname;
		this.id = id;
		this.title = title;
		this.year = year;
		
		this.genres = genre.split(",");
		
	}
	
	public String getDirname() {
		return this.dirname;
	}

	public String getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public String[] getGenres() {
		return this.genres;
	}
	public void showData() {
		System.out.println(this.dirname + " " + this.id + " " + this.title + " " + this.year + " " + this.genres[0] );
	}
}
