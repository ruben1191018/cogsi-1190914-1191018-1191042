package payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("John", "Doe","john.doe@gmail.com", "Developer", "Software Engineer", 5);
    }

    @Test
    void testGetAndSetFirstName() {
        assertEquals("John", employee.getFirstName());
        employee.setFirstName("Jane");
        assertEquals("Jane", employee.getFirstName());
    }

    @Test
    void testGetAndSetLastName() {
        assertEquals("Doe", employee.getLastName());
        employee.setLastName("Smith");
        assertEquals("Smith", employee.getLastName());
    }

    @Test
    void testGetAndSetRole() {
        assertEquals("Developer", employee.getRole());
        employee.setRole("Manager");
        assertEquals("Manager", employee.getRole());
    }

    @Test
    void testGetAndSetJobTitle() {
        assertEquals("Software Engineer", employee.getJobTitle());
        employee.setJobTitle("Lead Engineer");
        assertEquals("Lead Engineer", employee.getJobTitle());
    }

    @Test
    void testGetAndSetJobYears() {
        assertEquals(5, employee.getJobYears());
        employee.setJobYears(10);
        assertEquals(10, employee.getJobYears());
    }

    @Test
    void testGetAndSetId() {
        employee.setId(100L);
        assertEquals(100L, employee.getId());
    }

    @Test
    void testGetAndSetName() {
        assertEquals("John Doe", employee.getName());
        employee.setName("Jane Smith");
        assertEquals("Jane Smith", employee.getName());
        assertEquals("Jane", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(employee.equals(employee));
    }

    @Test
    void testEqualsDifferentObjectSameValues() {
        Employee employee2 = new Employee("John", "Doe","john.doe@gmail.com", "Developer", "Software Engineer", 5);
        employee2.setId(employee.getId());
        assertTrue(employee.equals(employee2));
    }

    @Test
    void testEqualsDifferentObjectDifferentValues() {
        Employee employee2 = new Employee("Jane", "Smith","jane.smith@gmail.com", "Manager", "Lead Engineer", 10);
        assertFalse(employee.equals(employee2));
    }

    @Test
    void testEqualsNullObject() {
        assertFalse(employee.equals(null));
    }

    @Test
    void testEqualsDifferentClass() {
        assertFalse(employee.equals(new Object()));
    }

    @Test
    void testHashCodeSameObject() {
        assertEquals(employee.hashCode(), employee.hashCode());
    }

    @Test
    void testShouldThrowExceptionWhenEmailIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->new Employee("Jane", "Smith","", "Manager", "Lead Engineer", 10));
    }

    @Test
    void testShouldThrowExceptionWhenEmailIsNull() {
        assertThrows(IllegalArgumentException.class, () ->new Employee("Jane", "Smith",null, "Manager", "Lead Engineer", 10));
    }

    @Test
    void testShouldThrowExceptionWhenEmailIsDoesntHaveAtSign() {
        assertThrows(IllegalArgumentException.class, () ->new Employee("Jane", "Smith","janesmithgmail.com", "Manager", "Lead Engineer", 10));
    }

    @Test
    void testHashCodeDifferentObjectSameValues() {
        Employee employee2 = new Employee("John", "Doe","john.doe@gmail.com", "Developer", "Software Engineer", 5);
        employee2.setId(employee.getId());
        assertEquals(employee.hashCode(), employee2.hashCode());
    }

    @Test
    void testHashCodeDifferentObjectDifferentValues() {
        Employee employee2 = new Employee("Jane", "Smith","jane.smith@gmail.com", "Manager", "Lead Engineer", 10);
        assertNotEquals(employee.hashCode(), employee2.hashCode());
    }
}
