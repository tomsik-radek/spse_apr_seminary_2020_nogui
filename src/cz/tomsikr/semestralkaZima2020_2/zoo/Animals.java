package cz.tomsikr.semestralkaZima2020_2.zoo;

public class Animals {
	int id;
	String species;
	String category;
	String name;
	String age;
	String health;
	String weight;
	String sex;
	String wingspan;
	String notes;
	
	public Animals(int id, String species, String category, String name, String age, String health, String weight, String sex, String wingspan, String notes) {
		this.id = id;
		this.species = species;
		this.category = category;
		this.name = name;
		this.age = age;
		this.health = health;
		this.weight = weight;
		this.sex = sex;
		this.wingspan = wingspan;
		this.notes = notes;
	}
	
	public int getID() {
		return id;
	}
	public String getSpecies() {
		return species;
	}
	public String getCategory() {
		return category;
	}
	public String getName() {
		return name;
	}
	public  String getAge() {
		return name;
	}
	public String getHealth() {
		return health;
	}
	public String getWeight() {
		return weight;
	}
	public String getSex() {
		return sex;
	}
	public String getWingspan() {
		return wingspan;
	}
	public String getNotes() {
		return notes;   
	}
	
	public void setID(int id) {
		this.id = id;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setWingspan(String wingspan) {
		this.wingspan = wingspan;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
