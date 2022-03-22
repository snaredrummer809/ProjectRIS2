package Controllers;

public class PatientTableModel {
	String firstName,lastName,DOB,sex,race,ethnicity;
	int ID;

	public PatientTableModel(int ID, String firstName, String lastName, String DOB, String sex, String race, String ethnicity) {
		super();
		this.ID = ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.DOB = DOB;
		this.sex = sex;
		this.race =race;
		this.ethnicity = ethnicity;
	}

	/*
	public PatientTableModel(String username, String displayName, String email, String role, int iD) {
		super();
		this.username = username;
		this.displayName = displayName;
		this.email = email;
		this.role = role;
		this.ID = iD;
	}
	*/

	public PatientTableModel(int ID, String DOB, String lastName, String firstName) {
		this.ID = ID;
		this.DOB = DOB;
		this.firstName = firstName;
		this.lastName = lastName;
		
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String DOB) {
		this.DOB = DOB;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}
	
	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	
}
