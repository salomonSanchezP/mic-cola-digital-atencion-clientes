package pe.registros.ms.turnos.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import pe.registros.ms.turnos.dto.TurnoRequestDTO;
import pe.registros.ms.turnos.dto.TurnoRequestUpdateDTO;
import pe.registros.ms.turnos.dto.TurnoResponseDTO;
import pe.registros.ms.turnos.service.TurnoService;

@Path("/api/v1/turnos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TurnoResource {

    @Inject
    TurnoService turnoService;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @POST
    @Operation(summary = "Crear un nuevo turno")
    @Path("/create")
    public Response crearTurno(TurnoRequestDTO request) {
        try {
            TurnoResponseDTO response = turnoService.guardarTurno(request);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al crear turno: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/en-espera")
    @Operation(summary = "Listar todos los turnos en espera")
    public Response listarTurnosEnEspera() {
        return Response.ok(turnoService.listarTurnosEnEspera()).build();
    }

    @PUT
    @Path("/llamar")
    @Operation(summary = "Llamar un turno")
    public Response allamarTurno(TurnoRequestUpdateDTO request) {
        try {
            TurnoResponseDTO response = turnoService.llamarTurno(request);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error: " + e.getMessage()).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Error: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al atender turno: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/atender")
    @Operation(summary = "Atender un turno")
    public Response atenderTurno(TurnoRequestUpdateDTO request) {
        try {
            TurnoResponseDTO response = turnoService.atenderTurno(request);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error: " + e.getMessage()).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Error: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al atender turno: " + e.getMessage()).build();
        }
    }
}
