package co.istad.mbanking.features.branch.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import co.istad.mbanking.util.TimeDeserializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchRequest {
    @NotBlank(message = "Branch name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    private String city;
    private String province;
    private Double latitude;
    private Double longitude;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid")
    private String phoneNumber;

    @Email(message = "Email must be valid")
    private String email;

    @JsonDeserialize(using = TimeDeserializer.class)
    private String openTime;

    @JsonDeserialize(using = TimeDeserializer.class)
    private String closeTime;

    private String workDays;
}
