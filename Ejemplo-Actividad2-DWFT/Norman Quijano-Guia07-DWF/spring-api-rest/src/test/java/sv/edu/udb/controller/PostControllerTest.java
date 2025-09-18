package sv.edu.udb.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.controller.response.PostResponse;
import sv.edu.udb.repository.domain.Post;
import sv.edu.udb.controller.response.PostResponse;
import sv.edu.udb.service.PostService;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(PostController.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostService postService;
    private Post post;
    @BeforeEach
    void init() {
        this.post = Post
                .builder()
                .id(1L)
                .title("testing post")
                .postDate(LocalDate.of(2024, 9, 28))
                .build();
    }
    @Test
    @DisplayName("Get all post")
    void findAllPost_when_performGetRequest() throws Exception {
        final List<PostResponse> expectedList = List.of(PostResponse
                .builder()
                .title(this.post.getTitle())
                .postDate(this.post.getPostDate())
                .build());
        //Inicializamos objetos
        when(postService.findAll()).thenReturn(expectedList);
        this.mockMvc.perform(get("/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedList)));
    }

}
