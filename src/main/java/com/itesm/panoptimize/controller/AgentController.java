package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.dto.agent.WorkspaceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//El controlador debe de comparar el ID con el de la base de datos
// para despues mostrar el resto del usuario
@RestController
public class AgentController {

    private List<AgentDTO> agentList = new ArrayList<>();
    private List<WorkspaceDTO> workspaces = new ArrayList<>();

    //Documentación de Open API
    @Operation(summary = "Obtener info  de agente", description = "Obtener la info de agente mediante el id" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Agente encontrado.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Agente no encontrado.",
                    content = @Content),
    })
    //No pongo lo de Open Api por que me marca que no encuentra la dependencia
    //Mapping del dominio que nos dieron en la junta del 2 abril, hay que ver si se puede con el :



    @GetMapping("/agent/:id")
    public ResponseEntity<AgentDTO> getAgent(){
        //List<workspaceDTO> workspaces = new ArrayList<>();
        AgentDTO agentDTO = new AgentDTO();
        agentDTO.setId(1); //Ver si es necesario cambiar esto, seria solamente para la comparación con BD
        agentDTO.setName("Dave Parker");
        agentDTO.setEmailID("chapelle007@gmail.com");
        agentDTO.setUsername("dave.p");
        agentDTO.setPassword("************"); //Ver como darle el formato del password
        agentList.add(agentDTO);
        //Ver si es necesario hacer DTOs para workspace o solo con puro string
        WorkspaceDTO workspace1 = new WorkspaceDTO();
        workspace1.setName("Delivery"); //Creamos workspace dto nuevo y lo llamamos Delivery
        workspaces.add(workspace1);
        agentDTO.setWorkspaces(workspaces);
        return  ResponseEntity.ok(agentDTO);
    }
    @DeleteMapping("/agent/{id}")
    public ResponseEntity<String> deleteAgent(@PathVariable int id) {
        for (AgentDTO agent : agentList) {
            if (agent.getId() == id) {
                agentList.remove(agent);
                return ResponseEntity.ok("Agente eliminado exitosamente");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún agente con el ID proporcionado");
    }
}