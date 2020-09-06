package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // persistence.xml에 있던 unit name을 넘긴
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            List<Member> result = entityManager.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();
            for(Member m : result){
                System.out.println(m.getName());
            }
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally{
            entityManager.close();
        }

        emf.close();
    }
}
