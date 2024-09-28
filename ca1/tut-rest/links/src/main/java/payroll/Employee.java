package payroll;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class Employee {

	private @Id @GeneratedValue Long id;
	private String jobTitle;
	private Integer jobYears;
	private String firstName;
	private String lastName;
	private String email;


	private String role;

	Employee() {}

	Employee(String firstName, String lastName,String email, String role, String jobTitle, Integer jobYears) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.email= email;
		this.role = role;
		this.jobTitle = jobTitle;
		this.jobYears = jobYears;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Integer getJobYears() {
		return jobYears;
	}

	public void setJobYears(Integer jobYears) {
		this.jobYears = jobYears;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public void setName(String name) {

		String[] parts = name.split(" ");
		this.firstName = parts[0];
		this.lastName = parts[1];
	}

	public Long getId() {
		return this.id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(!email.isEmpty()){
			this.email = email;
		}else{
			throw new IllegalArgumentException("Email cannot be empty");
		}
	}

	public String getRole() {
		return this.role;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof Employee))
			return false;
		Employee employee = (Employee) o;
		return Objects.equals(this.id, employee.id) && Objects.equals(this.jobTitle, employee.jobTitle)
				&& Objects.equals(this.jobYears, employee.jobYears)
				&& Objects.equals(this.firstName, employee.firstName)
				&& Objects.equals(this.lastName, employee.lastName)
				&& Objects.equals(this.email, employee.email)
				&& Objects.equals(this.role, employee.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.jobTitle, this.jobYears,this.firstName, this.lastName, this.role);
	}

	@Override
	public String toString() {
		return "Employee{" + "id=" + this.id
				+ '\'' + "jobTitle=" + this.jobTitle + '\'' + "jobYears=" + this.jobYears +", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName
				+ '\'' +", email='" + this.email + ", role='" + this.role + '\'' + '}';
	}
}
