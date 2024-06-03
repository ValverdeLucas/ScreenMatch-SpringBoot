package education.next.one.ScreenMatch.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
