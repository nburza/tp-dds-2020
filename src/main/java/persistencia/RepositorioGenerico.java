package persistencia;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class RepositorioGenerico<E extends EntidadPersistente> implements WithGlobalEntityManager {

    public Collection<E> getAllInstances() {

        return entityManager()
                .createQuery("FROM " + getClase().getSimpleName(), getClase())
                .getResultList();
    }

    public Optional<E> getPorId(long id) {

        return Optional.ofNullable(entityManager().find(getClase(), id));
    }

    public List<E> getPorListaDeIds(List<Long> ids) {

        return getAllInstances().stream().filter(e -> ids.stream().anyMatch(i -> i.equals(e.getId()))).collect(Collectors.toList());
    }

    public void agregar(E unElemento) {

        entityManager().persist(unElemento);
    }

    public void borrar(E unElemento) {

        entityManager().remove(unElemento);
    }

    protected abstract Class<E> getClase();
}
