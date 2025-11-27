package model;

import java.util.Set;

public record User(String name, String id, Set<String> watchedMovies) {
}