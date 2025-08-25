package co.istad.mbanking.features.auth.dto;

public record ChangePasswordRequest(

        String oldPassword,

        String password,

        String confirmedPassword

) {
}