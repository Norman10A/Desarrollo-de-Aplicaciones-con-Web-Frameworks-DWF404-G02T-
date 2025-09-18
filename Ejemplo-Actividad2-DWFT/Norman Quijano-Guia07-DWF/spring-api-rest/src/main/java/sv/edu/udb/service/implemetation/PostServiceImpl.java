package sv.edu.udb.service.implemetation;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.udb.controller.request.PostRequest;
import sv.edu.udb.controller.response.PostResponse;
import sv.edu.udb.repository.PostRepository;
import sv.edu.udb.repository.domain.Post;
import sv.edu.udb.service.PostService;
import sv.edu.udb.service.mapper.PostMapper;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    @NonNull
    private final PostRepository postRepository;
    @NonNull
    private final PostMapper postMapper;
    @Override
    public List<PostResponse> findAll() {
        return postMapper.toPostResponseList(postRepository.findAll());
    }
    @Override
    public PostResponse findById(final Long id) {
        return postMapper.toPostResponse(
                postRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Resource not found id " + id)));
    }
    @Override
    public PostResponse save(final PostRequest postRequest) {
        final Post post = postMapper.toPost(postRequest);
        return postMapper.toPostResponse(postRepository.save(post));
    }
    @Override
    public PostResponse update(final Long id, final PostRequest postRequest) {
        final Post postToUpdate = postRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Resource not found id " + id));
        postToUpdate.setTitle(postRequest.getTitle());
        postToUpdate.setPostDate(postRequest.getPostDate());
        postRepository.save(postToUpdate);
        return postMapper.toPostResponse(postToUpdate);
    }
    @Override
    public void delete(final Long id) {
        postRepository.deleteById(id);
    }

}
