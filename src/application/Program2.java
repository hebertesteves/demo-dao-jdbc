package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;
import java.util.Scanner;

public class Program2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DepartmentDao departmentDaoJDBC = DaoFactory.createDepartmentDao();

        System.out.println("=== TEST 1: department findById ====");
        Department dep = departmentDaoJDBC.findById(1);
        System.out.println(dep);

        System.out.println("\n=== TEST 2: department findAll ====");
        List<Department> list = departmentDaoJDBC.findAll();
        list.forEach(System.out::println);

        System.out.println("\n=== TEST 3: department INSERT ====");
        dep = new Department(null, "Music");
        departmentDaoJDBC.insert(dep);
        System.out.println("Inserted! New id: " + dep.getId());

        System.out.println("\n=== TEST 4: department UPDATE ====");
        dep = departmentDaoJDBC.findById(13);
        dep.setName("RH");
        departmentDaoJDBC.update(dep);
        System.out.println("Update completed");

        System.out.println("\n=== TEST 5: department DELETE ====");
        System.out.print("Enter id for the delete test: ");
        int id = sc.nextInt();
        departmentDaoJDBC.deleteById(id);
        System.out.println("Delete completed");
        sc.close();
    }
}
