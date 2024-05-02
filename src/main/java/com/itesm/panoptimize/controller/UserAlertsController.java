package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.dto.alert.UserAlertsDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
// Controller class for managing user alerts
@RestController
public class UserAlertsController {
    // Retrieves alerts for a specific recipient.
    @GetMapping("/alerts/{recipient}")
    @Operation(summary = "Obtener alertas de un agente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Alertas del agente recolectado.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserAlertsDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Alertas de agente no encontradas.",
                    content = @Content)
    })
    // Generates sample alerts for testing purposes
    public List<UserAlertsDTO> getAlertsForRecipient(@PathVariable String recipient) {

        List<UserAlertsDTO> alerts = new ArrayList<>();
        UserAlertsDTO alert1 = new UserAlertsDTO("Sergio","Max","Transfer help");
        alerts.add(alert1);
        UserAlertsDTO alert2 = new UserAlertsDTO("Lando","Pierre","Contact help");
        alerts.add(alert2);
        UserAlertsDTO alert3 = new UserAlertsDTO("Sergio","Lewis","Temperature help");
        alerts.add(alert3);

        List<UserAlertsDTO> recipientAlerts = new ArrayList<>();
        for (UserAlertsDTO alert : alerts) {
            if (alert.getRecipient().equals(recipient)) {
                recipientAlerts.add(alert);
            }
        }
        return recipientAlerts;
    }

}
