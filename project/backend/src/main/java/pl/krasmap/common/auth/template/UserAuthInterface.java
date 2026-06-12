package pl.krasmap.common.auth.template;

public interface UserAuthInterface {
    String GenerateJwt(int userId);
}
