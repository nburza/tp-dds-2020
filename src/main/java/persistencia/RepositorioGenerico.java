package persistencia;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.Collection;
import java.util.Optional;

public abstract class RepositorioGenerico<E extends EntidadPersistente> implements WithGlobalEntityManager {

    public Collection<E> getAllInstances() {

        return entityManager()
                .createQuery("FROM " + getClase().getSimpleName(), getClase())
                .getResultList();
    }

    public Optional<E> getPorId(long id) {

        return Optional.ofNullable(entityManager().find(getClase(), id));
    }

    public void agregar(E unElemento) {

        entityManager().persist(unElemento);
    }

    public void borrar(E unElemento) {

        entityManager().remove(unElemento);
    }

    protected abstract Class<E> getClase();
}
