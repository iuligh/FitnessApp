package com.backend.Controllers;

import com.backend.DTOS.UserProfileDto;
import com.backend.model.User;
import com.backend.payload.response.UserInfoResponse;
import com.backend.services.StorageService;
import com.backend.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            log.info("Fetching profile for user ID={}", id);
            User user = userService.getUserById(id);
            if (user == null) {
                log.warn("User not found for ID={}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            UserInfoResponse dto = new UserInfoResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRoles().stream()
                            .map(r -> r.getName().name())
                            .collect(Collectors.toList()),
                    user.getProfileImageUrl(),
                    user.getFullName(),
                    user.getPhoneNumber(),
                    user.getCity(),
                    user.getAddress()
            );
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            log.error("Error fetching profile for user ID={}", id, ex);
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Unable to fetch user profile",
                            "details", ex.getMessage() != null ? ex.getMessage() : "Unknown"
                    ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileDto profileDto
    ) {
        try {
            log.info("Updating profile for user ID={}", id);
            User updated = userService.updateUserProfile(id, profileDto);
            if (updated == null) {
                log.warn("User not found for update, ID={}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            UserInfoResponse dto = new UserInfoResponse(
                    updated.getId(),
                    updated.getUsername(),
                    updated.getEmail(),
                    updated.getRoles().stream()
                            .map(r -> r.getName().name())
                            .collect(Collectors.toList()),
                    updated.getProfileImageUrl(),
                    updated.getFullName(),
                    updated.getPhoneNumber(),
                    updated.getCity(),
                    updated.getAddress()
            );
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            log.warn("Validation error updating user ID={}: {}", id, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Error updating profile for user ID={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to update user profile"));
        }
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            log.info("Uploading profile image for user ID={}", id);
            if (file.isEmpty()) {
                log.warn("Empty file upload attempt for user ID={}", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "No file provided"));
            }

            String imageUrl = storageService.saveProfileImage(file, id);
            if (imageUrl == null) {
                log.error("StorageService failed to save image for user ID={}", id);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to store image"));
            }

            UserProfileDto profileDto = new UserProfileDto();
            profileDto.setProfileImageUrl(imageUrl);
            User updated = userService.updateUserProfile(id, profileDto);
            if (updated == null) {
                log.warn("User not found when setting image URL, ID={}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            return ResponseEntity.ok(Map.of("profileImageUrl", updated.getProfileImageUrl()));
        } catch (IllegalArgumentException ex) {
            log.warn("Validation error uploading image for user ID={}: {}", id, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Error uploading profile image for user ID={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to upload image"));
        }
    }
}
