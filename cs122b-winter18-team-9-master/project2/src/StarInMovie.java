
public class StarInMovie {
	
	private String fid;
	private String movie_title;
	private String actor_name;
	
	public StarInMovie(String fid, String movie_title, String actor_name) {
		this.fid = fid;
		this.movie_title = movie_title;
		this.actor_name = actor_name;
	}
	
	public String getFID() {
		return this.fid;
	}
	
	public String getTitle() {
		return this.movie_title;
	}
	
	public String getActor() {
		return this.actor_name;
	}
	
	public void showData() {
		System.out.println(this.fid + " " + this.movie_title + " " + this.actor_name);
	}
}
