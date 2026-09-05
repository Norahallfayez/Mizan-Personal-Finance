package mizan.model;

public record Category(
        int categoryId,
        int userId,
        String name,
        String type,
        String icon
) {
    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}

