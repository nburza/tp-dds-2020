package main;

import egreso.ValidadorDeEgresos;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public class Job implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public static void main(String[] args) {new Job().run();}

    public void run() {
        withTransaction(() -> ValidadorDeEgresos.getInstance().validarTodos());
    }
}
