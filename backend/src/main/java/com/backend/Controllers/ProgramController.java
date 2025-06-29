package com.backend.Controllers;

import com.backend.EventDTO;
import com.backend.enums.EWeekDays;
import com.backend.services.ProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ProgramController {
  private static final Logger log = LoggerFactory.getLogger(ProgramController.class);
  private final ProgramService programService;

  public ProgramController(ProgramService programService) {
    this.programService = programService;
  }

  /**
   * GET /api/program?userId=20&day=LUNI
   */
  @GetMapping("/program")
  public ResponseEntity<?> getProgram(
          @RequestParam Long userId,
          @RequestParam String day
  ) {
    try {
      EWeekDays enumDay;
      try {
        enumDay = EWeekDays.valueOf(day.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Ziua transmisă nu este validă: " + day));
      }
      log.info("HTTP GET /api/program?userId={}&day={}", userId, day);
      List<EventDTO> list = programService.getProgramForUser(userId, enumDay);
      if (list.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(list);
    } catch (Exception ex) {
      log.error("Eroare la GET /api/program", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Map.of("error", "Nu se poate încărca programul."));
    }
  }
}
