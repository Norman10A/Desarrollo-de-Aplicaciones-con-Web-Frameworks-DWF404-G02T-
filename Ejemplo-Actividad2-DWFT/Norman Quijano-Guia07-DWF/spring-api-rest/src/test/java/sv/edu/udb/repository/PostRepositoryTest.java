package sv.edu.udb.repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sv.edu.udb.repository.domain.Post;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
@DataJpaTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @BeforeEach
    void init() {
        final String expectedTitle = "Anything you want to write";
        final LocalDate expectedDate = LocalDate.of(2024, 8, 24);
        final Post newPost = Post
                .builder()
                .title(expectedTitle)
                .postDate(expectedDate)
                .build();
        postRepository.save(newPost);
    }
    @AfterEach
    void clean() {
        postRepository.deleteAll();
    }
    @Test
    void shouldHasOnePost_When_FindAll() {
        int expectedPostNumber = 1;
        final List<Post> actualPostList = postRepository.findAll();
        assertNotNull(actualPostList);
        assertEquals(expectedPostNumber, actualPostList.size());
    }
    @Test
    void shouldGetPost_When_IdExist() {
        final Long expectedId = 1L;
        final String expectedTitle = "Anything you want to write";
        final LocalDate expectedDate = LocalDate.of(2024, 8, 24);
        final Post actualPost = postRepository.findById(expectedId).orElse(null);
        assertNotNull(actualPost);
        assertEquals(expectedId, actualPost.getId());
        assertEquals(expectedTitle, actualPost.getTitle());
        assertEquals(expectedDate, actualPost.getPostDate());
    }
    @Test
    void shouldSavePost_When_PostIsNew() {
        final Long expectedId = 2L;
        final String expectedTitle = "Anything you want to write";
        final LocalDate expectedDate = LocalDate.of(2024, 8, 24);
        final Post newPost = Post
                .builder()
                .title(expectedTitle)
                .postDate(expectedDate)
                .build();
        postRepository.save(newPost);
        final Post actualPost = postRepository.findById(expectedId).orElse(null);
        assertNotNull(actualPost);
        assertEquals(expectedId, actualPost.getId());
        assertEquals(expectedTitle, actualPost.getTitle());
        assertEquals(expectedDate, actualPost.getPostDate());
    }
    @Test
    void shouldDeletePost_When_PostExist() {
        final String expectedTitle = "Anything you want to write";
        final LocalDate expectedDate = LocalDate.of(2024, 8, 24);
        final Post newPost = Post
                .builder()
                .title(expectedTitle)
                .postDate(expectedDate)
                .build();
        postRepository.saveAndFlush(newPost);
        final Long expectedId = newPost.getId();
        final Post actualPost = postRepository.findById(expectedId).orElse(null);
        assertNotNull(actualPost); //Exist post
        postRepository.deleteById(expectedId); //deleting
        final Post deletedPost = postRepository.findById(expectedId).orElse(null);
        assertNull(deletedPost);
    }


}
