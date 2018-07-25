
public class Actor {

	private String stagename;
	private int dob;
	private String id;
	
	public Actor(String id, String stagename, int dob) {
		this.id = id;
		this.stagename = stagename;
		this.dob = dob;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getName() {
		return this.stagename;
	}
	
	public int getDOB() {
		return this.dob;
	}
	
	public void showData() {
		System.out.println(this.id + " " + this.stagename + " " + this.dob);
	}
}
