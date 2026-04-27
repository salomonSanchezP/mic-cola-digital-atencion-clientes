package pe.registros.ms.ventanilla.messagin;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pe.registros.ms.ventanilla.dto.TurnoEnEsperaDTO;

@Path("/api/v1/turnos")
@RegisterRestClient(configKey = "ms-api-turnos")
public interface TurnoClient {

    @GET
    @Path("/en-espera")
    @Produces(MediaType.APPLICATION_JSON)
    List<TurnoEnEsperaDTO> getTurnosEnEspera();
}