package main;

import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import usuario.Usuario;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public static void main(String[] args) { new Bootstrap().run();}

    public void run(){
        withTransaction(() -> {
            try {
                persist(new Usuario("migue","alta clave"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
