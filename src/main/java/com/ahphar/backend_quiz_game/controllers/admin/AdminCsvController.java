package com.ahphar.backend_quiz_game.controllers.admin;

import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.repositories.PhaseRepository;
import com.ahphar.backend_quiz_game.services.LeaderboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/csv")
@Tag(name = "Admin CSV", description = "Endpoints for admin CSV operations")
@RequiredArgsConstructor
public class AdminCsvController {
    
    private final LeaderboardService leaderboardService;
    private final PhaseRepository phaseRepository;

    @Operation(
        summary = "Export leaderboard to CSV",
        description = "Exports the leaderboard of the specific phase to a CSV file.",
        security = @SecurityRequirement(name = "bearerAuth")
        )
    @GetMapping("/download-leaderboard/{phaseId}")
    public ResponseEntity<ByteArrayResource> downloadLeaderboardCsv(@PathVariable Long phaseId) {
        
        Phase phase = phaseRepository.findById(phaseId)
            .orElseThrow(() -> new IllegalArgumentException("Phase not found with ID: " + phaseId));
        
        List<Map<String, Object>> entries = leaderboardService.getLeaderboardEntries(phase);

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Username,Score,Phase Name\n");

        for (Map<String, Object> entry : entries) {
            String rank = entry.get("Rank") != null ? entry.get("Rank").toString() : "00";
            String username = entry.get("username") != null ? entry.get("username").toString() : "Unknown";
            String score = entry.get("score") != null ? entry.get("score").toString() : "0";
            String phaseName = entry.get("phaseName") != null ? entry.get("phaseName").toString() : "Unknown";
            csvBuilder.append(String.format("%s,%s,%s,%s\n", rank,username, score, phaseName));
        }

        byte[] csvBytes = csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(csvBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leaderboard.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csV");

        return ResponseEntity.ok()
                            .headers(headers)
                            .contentLength(csvBytes.length)
                            .body(resource);
    }
}
