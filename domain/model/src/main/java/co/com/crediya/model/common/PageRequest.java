package co.com.crediya.model.common;

public record PageRequest(
        int page,
        int size
) {
    public int offset() {
        return page * size;
    }
}