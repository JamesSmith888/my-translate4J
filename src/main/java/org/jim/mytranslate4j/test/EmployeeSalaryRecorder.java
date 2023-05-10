package org.jim.mytranslate4j.test;

import java.io.*;
import java.util.*;

class Employee {
    String name;
    int id;
    double salary;

    Employee(String name, int id, double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", salary=" + salary + "]";
    }
}

class SalaryEmployee extends Employee {
    double bonus;

    SalaryEmployee(String name, int id, double salary, double bonus) {
        super(name, id, salary);
        this.bonus = bonus;
    }

    double getTotalSalary() {
        return this.salary + this.bonus;
    }

    @Override
    public String toString() {
        return "SalaryEmployee [id=" + id + ", name=" + name + ", salary=" + salary + ", bonus=" + bonus + ", totalSalary=" + getTotalSalary() + "]";
    }
}

public class EmployeeSalaryRecorder {

    public static void main(String[] args) {
        List<SalaryEmployee> employees = new ArrayList<>();

        // 输入职工信息
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入职工数目:");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("请输入职工名字:");
            String name = sc.next();
            System.out.println("请输入职工ID:");
            int id = sc.nextInt();
            System.out.println("请输入职工工资:");
            double salary = sc.nextDouble();
            System.out.println("请输入职工奖金:");
            double bonus = sc.nextDouble();
            employees.add(new SalaryEmployee(name, id, salary, bonus));
        }

        // 根据总工资排序
        Collections.sort(employees, (e1, e2) -> Double.compare(e2.getTotalSalary(), e1.getTotalSalary()));

        // 保存到文件
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("employee_salaries.txt"));

            for (SalaryEmployee e : employees) {
                bw.write(e.toString());
                bw.newLine();
            }

            bw.close();
            System.out.println("职工工资信息已保存到文件中.");
        } catch (IOException e) {
            System.out.println("文件写入错误: " + e.getMessage());
        }

        sc.close();
    }
}
