package model;

public class Defendant {
	private int id;
	private String name;
	private String gender;
	private String birthDay;
	private String birthProvince;
	private String birthCity;
	private String nation;
	private String education;
	private String career;
	private String householdReg;    //户籍地
	private String residence;       //居住地
	private Judgment judgment;

	public Defendant() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getBirthProvince() {
		return birthProvince;
	}

	public void setBirthProvince(String birthProvince) {
		this.birthProvince = birthProvince;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getHouseholdReg() {
		return householdReg;
	}

	public void setHouseholdReg(String householdReg) {
		this.householdReg = householdReg;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public Judgment getJudgment() {
		return judgment;
	}

	public void setJudgment(Judgment judgment) {
		this.judgment = judgment;
	}
}
