package pe.registros.ms.ventanilla.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.registros.ms.ventanilla.service.TurnoService;



@Path("/api/v1/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TurnoResource {

    @Inject
    TurnoService turnoService;

    @GET
    @Path("/health")
    public Response healthCheck() {
        return Response.ok("{\"status\": \"Dashboard activo \"}").build();
    }

    @GET
    @Path("/turnos")
    public Response getAllTurnos() {
        return Response.ok(turnoService.getAllTurnos()).build();
    }

}
