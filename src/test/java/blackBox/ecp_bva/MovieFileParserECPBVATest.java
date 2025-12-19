

import model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - ECP & BVA for MovieFileParser class
 * Tests equivalence class partitioning and boundary value analysis
 */
public class MovieFileParserECPBVATest {
    private MovieFileParser parser;
    private Path testFilePath;

    @BeforeEach
    void setUp() throws IOException {
        parser = new MovieFileParser();
        testFilePath = Files.createTempFile("movies", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    //------- readMovies() - ECP & BVA Tests -------//

    // ECP: Valid equivalence class - normal movie format
    @Test
    public void testReadMovies_ECP_ValidFormat() throws Exception {
        String content = "Inception,I001\nAction,Sci-Fi\nThe Matrix,TM002\nAction,Sci-Fi";
        Files.writeString(testFilePath, content);
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(2, movies.size());
        assertEquals("Inception", movies.get(0).title());
        assertEquals("I001", movies.get(0).id());
        assertArrayEquals(new String[]{"Action", "Sci-Fi"}, movies.get(0).genres());
    }

    // BVA: Boundary - single movie
    @Test
    public void testReadMovies_BVA_SingleMovie() throws Exception {
        String content = "Solo Movie,SM001\nDrama";
        Files.writeString(testFilePath, content);
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(1, movies.size());
        assertEquals("Solo Movie", movies.get(0).title());
    }

    // BVA: Boundary - empty file
    @Test
    public void testReadMovies_BVA_EmptyFile() throws Exception {
        Files.writeString(testFilePath, "");
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertTrue(movies.isEmpty());
    }

    // ECP: Valid equivalence class - single genre
    @Test
    public void testReadMovies_ECP_SingleGenre() throws Exception {
        String content = "Action Movie,AM001\nAction";
        Files.writeString(testFilePath, content);
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(1, movies.size());
        assertArrayEquals(new String[]{"Action"}, movies.get(0).genres());
    }

    // ECP: Valid equivalence class - multiple genres
    @Test
    public void testReadMovies_ECP_MultipleGenres() throws Exception {
        String content = "Complex Movie,CM001\nAction,Drama,Sci-Fi,Thriller";
        Files.writeString(testFilePath, content);
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(1, movies.size());
        assertEquals(4, movies.get(0).genres().length);
    }

    // ECP: Invalid equivalence class - wrong title/ID format
    @Test
    public void testReadMovies_ECP_WrongTitleIdFormat() {
        String content = "InvalidFormat";
        
        assertThrows(Exception.class, () -> {
            Files.writeString(testFilePath, content);
            parser.readMovies(testFilePath.toString());
        });
    }

    // ECP: Invalid equivalence class - too many commas in title line
    @Test
    public void testReadMovies_ECP_TooManyCommas() {
        String content = "Movie,Title,ID001\nAction";
        
        assertThrows(Exception.class, () -> {
            Files.writeString(testFilePath, content);
            parser.readMovies(testFilePath.toString());
        });
    }

    // ECP: Invalid equivalence class - missing genres line
    @Test
    public void testReadMovies_ECP_MissingGenres() {
        String content = "Movie Title,MT001";
        
        assertThrows(Exception.class, () -> {
            Files.writeString(testFilePath, content);
            parser.readMovies(testFilePath.toString());
        });
    }

    // BVA: Boundary - blank lines in file
    @Test
    public void testReadMovies_BVA_WithBlankLines() throws Exception {
        String content = "\n\nInception,I001\nAction\n\nMatrix,M002\nSci-Fi\n\n";
        Files.writeString(testFilePath, content);
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(2, movies.size());
    }

    // ECP: Valid equivalence class - whitespace in data
    @Test
    public void testReadMovies_ECP_WithWhitespace() throws Exception {
        String content = "  Inception  ,  I001  \n  Action  ,  Sci-Fi  ";
        Files.writeString(testFilePath, content);
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(1, movies.size());
        assertEquals("Inception", movies.get(0).title());
        assertEquals("I001", movies.get(0).id());
        assertEquals("Action", movies.get(0).genres()[0]);
        assertEquals("Sci-Fi", movies.get(0).genres()[1]);
    }

    // BVA: Boundary - many movies
    @Test
    public void testReadMovies_BVA_ManyMovies() throws Exception {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            content.append("Movie").append(i).append(",M").append(String.format("%03d", i)).append("\n");
            content.append("Genre").append(i).append("\n");
        }
        Files.writeString(testFilePath, content.toString());
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(50, movies.size());
    }

    // ECP: Valid equivalence class - special characters in title
    @Test
    public void testReadMovies_ECP_SpecialCharsInTitle() throws Exception {
        String content = "Movie: The Sequel,MTS001\nAction";
        Files.writeString(testFilePath, content);
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(1, movies.size());
        assertEquals("Movie: The Sequel", movies.get(0).title());
    }

    // BVA: Boundary - empty genres (only commas)
    @Test
    public void testReadMovies_BVA_EmptyGenres() throws Exception {
        String content = "Movie,M001\n,,,";
        Files.writeString(testFilePath, content);
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(1, movies.size());
        // Empty strings filtered out
        assertTrue(movies.get(0).genres().length == 0 || 
                   (movies.get(0).genres().length > 0 && !movies.get(0).genres()[0].isEmpty()));
    }

    // ECP: Valid equivalence class - genres with spaces
    @Test
    public void testReadMovies_ECP_GenresWithSpaces() throws Exception {
        String content = "Movie,M001\nScience Fiction,Action Adventure";
        Files.writeString(testFilePath, content);
        
        List<Movie> movies = parser.readMovies(testFilePath.toString());
        
        assertEquals(1, movies.size());
        assertEquals("Science Fiction", movies.get(0).genres()[0]);
        assertEquals("Action Adventure", movies.get(0).genres()[1]);
    }
}
