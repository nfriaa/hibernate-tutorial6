# hibernate-tutorial6
Hibernate tutorial 6 : "Many To Many" association

[![contributions welcome](https://img.shields.io/badge/contributions-welcome-orange.svg?style=flat)](https://github.com/nfriaa/hibernate-tutorial6/issues) [![Travis](https://img.shields.io/travis/rust-lang/rust.svg)](https://github.com/nfriaa/hibernate-tutorial6) [![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/nfriaa/hibernate-tutorial6/blob/master/LICENSE)

## Description
A sample code to learn how to map **"Many To Many"** relationship between two entities using the Hibernate ORM.
* JavaSE 8
* Hibernate 5 / Annotations
* Hibernate **"Many To Many"** association
* Maven 4
* MySQL 5

## 1. Database
Create only database, don't create tables (tables will be created by Hibernate)
* database name : **persist_db**

## 2. Maven "pom.xml" dependencies
```xml
<dependencies>
    <!-- MySQL connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>6.0.6</version>
    </dependency>
    <!-- Hibernate -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.2.11.Final</version>
    </dependency>
</dependencies>
```

## 3. Hibernate configuration file "hibernate.cfg.xml"
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- MySQL -->
        <property name="hibernate.dialect">org.hibernate.dialect.MySQL5InnoDBDialect</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/persist_db?useTimezone=true&amp;serverTimezone=UTC</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password"></property>

        <!-- Hibernate -->
        <property name="show_sql">true</property>
        <property name="format_sql">false</property>
        <property name="hibernate.hbm2ddl.auto">create</property>

        <!-- Entities -->
        <mapping class="net.isetjb.hibernatetutorial6.Product"/>
        <mapping class="net.isetjb.hibernatetutorial6.Category"/>
    </session-factory>
</hibernate-configuration>
```
* hibernate.hbm2ddl.auto : "create" => creates the schema necessary for defined entities, destroying any previous data
* Entities : map the two entities in this XML config file (Product and Category)

## 4. "Many To Many" association
`Destination entity : Product.java`
```java
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", length = 255, nullable = true)
    private String name;

    @Column(name = "price", nullable = true)
    private int price;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_category",
            joinColumns = {
                @JoinColumn(name = "product_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "category_id")})
    private List<Category> categories = new ArrayList<>();

    // Getters and Setters here...
}
```

`Source entity : Category.java`
```java
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", length = 255, nullable = true)
    private String name;

    // uncomment this to have bidirectionnel mode
    // Bidirectionnel "Many To Many" :
    //    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    //    private List<Product> products = new ArrayList<>();
    //    public List<Product> getProducts() {
    //        return products;
    //    }
    //
    //    public void setProducts(List<Product> products) {
    //        this.products = products;
    //    }

    // Getters and Setters here...
}
```
* @OManyToMany : the **Many To Many** association always uses an intermediate join table to store the association that joins two entities
* There are two types of **Many To Many** association : 
    * Unidirectional : only source entity has a relationship field that refers to the target entity (our current example is unidirectional)
    * Bidirectional : each entity (i.e. source and target) has a relationship field that refers to each other

## 5. Main Class "Application.java"
* create main class to test the code
* example :
```java
    // new category
    Category category_a = new Category();
    category_a.setName("Cat a");
    session.save(category_a);

    // new category
    Category category_b = new Category();
    category_b.setName("Cat b");
    session.save(category_b);

    // new product
    Product product_x = new Product();
    product_x.setName("Prod x");
    product_x.setPrice(456);
    product_x.getCategories().add(category_b);
    session.save(product_x);

    // new product
    Product product_y = new Product();
    product_y.setName("Prod y");
    product_y.setPrice(123);
    product_y.getCategories().add(category_a);
    session.save(product_y);

    // new product
    Product product_z = new Product();
    product_z.setName("Prod z");
    product_z.setPrice(789);
    product_z.getCategories().add(category_a);
    session.save(product_z);
```