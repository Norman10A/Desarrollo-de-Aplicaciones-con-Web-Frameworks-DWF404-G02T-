package sv.edu.udb.service.mapper;
import org.mapstruct.Mapper;
import sv.edu.udb.controller.request.PostRequest;
import sv.edu.udb.controller.response.PostResponse;
import sv.edu.udb.repository.domain.Post;
import java.util.List;
@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(final Post data);
    List<PostResponse> toPostResponseList(final List<Post> postList);
    Post toPost(final PostRequest postRequest);
}
