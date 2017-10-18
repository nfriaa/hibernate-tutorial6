package net.isetjb.hibernatetutorial6;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Application class.
 *
 * @author Nafaa Friaa (nafaa.friaa@isetjb.rnu.tn)
 */
public class Application {

    /**
     * Attribute declaration for factory to share between methods.
     */
    private static SessionFactory factory;

    public static void main(String[] args) {
        System.out.println("JavaSE + Maven + Hibernate + MySQL : Many to Many Association");

        // Open connection  pool
        factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

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

            // new product
            Product product_w = new Product();
            product_w.setName("Prod w");
            product_w.setPrice(258);
            session.save(product_w);

            // new category
            Category category_c = new Category();
            category_c.setName("Cat c");
            category_c.getProducts().add(product_w);
            session.save(category_c);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            //e.printStackTrace();
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            session.close();
        }

        // Cleaning up connection pool
        factory.close();
    }

}
