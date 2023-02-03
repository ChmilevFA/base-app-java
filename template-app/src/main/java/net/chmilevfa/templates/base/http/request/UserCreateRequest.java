package net.chmilevfa.templates.base.http.request;

public record UserCreateRequest(
    String username,
    String password,
    String email) {

}
