package co.istad.mbanking.features.branch.dto;

import co.istad.mbanking.util.TimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class BranchResponse {
    private Integer id;
    private String name;
    private String address;
    private String city;
    private String province;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private String email;
//    private LocalTime openTime;
//    private LocalTime closeTime;
    @JsonDeserialize(using = TimeDeserializer.class)
    private String openTime;

    @JsonDeserialize(using = TimeDeserializer.class)
    private String closeTime;
    private String workDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
