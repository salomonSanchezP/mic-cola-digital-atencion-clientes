package pe.registros.ms.ventanilla.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.registros.ms.ventanilla.entity.TurnoEntity;




@ApplicationScoped
public class TurnoRepository implements PanacheRepositoryBase<TurnoEntity, String> {

}
