package com.budgetplatform.common.api;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public record PageRequestSpec(
        int page,
        int size,
        String sort,
        Direction direction
) {

    public enum Direction {
        ASC,
        DESC
    }

    public static PageRequestSpec of(
            int page,
            int size,
            String sort,
            String direction,
            Set<String> allowedSorts,
            String defaultSort,
            Direction defaultDirection,
            int maxSize
    ) {
        if (page < 0) {
            throw badRequest("Page must be greater than or equal to 0.");
        }
        if (size < 1 || size > maxSize) {
            throw badRequest("Page size must be between 1 and " + maxSize + ".");
        }

        String normalizedSort = normalize(sort);
        if (normalizedSort == null) {
            normalizedSort = defaultSort;
        }
        if (!allowedSorts.contains(normalizedSort)) {
            throw badRequest("Unsupported sort field: " + normalizedSort);
        }

        Direction normalizedDirection = parseDirection(direction, defaultDirection);
        return new PageRequestSpec(page, size, normalizedSort, normalizedDirection);
    }

    public <T> List<T> slice(List<T> items) {
        long from = (long) page * size;
        if (from >= items.size()) {
            return List.of();
        }
        int fromIndex = (int) from;
        int toIndex = Math.min(fromIndex + size, items.size());
        return items.subList(fromIndex, toIndex);
    }

    private static Direction parseDirection(String value, Direction defaultDirection) {
        String normalized = normalize(value);
        if (normalized == null) {
            return defaultDirection;
        }
        try {
            return Direction.valueOf(normalized.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw badRequest("Unsupported sort direction: " + value);
        }
    }

    private static String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static ApplicationException badRequest(String message) {
        return new ApplicationException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, message);
    }
}
