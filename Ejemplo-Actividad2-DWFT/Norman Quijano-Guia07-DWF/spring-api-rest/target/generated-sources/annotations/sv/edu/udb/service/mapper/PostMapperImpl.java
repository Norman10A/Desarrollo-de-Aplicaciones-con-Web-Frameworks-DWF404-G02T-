package sv.edu.udb.service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sv.edu.udb.controller.request.PostRequest;
import sv.edu.udb.controller.response.PostResponse;
import sv.edu.udb.repository.domain.Post;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-17T14:49:05-0600",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostResponse toPostResponse(Post data) {
        if ( data == null ) {
            return null;
        }

        PostResponse.PostResponseBuilder postResponse = PostResponse.builder();

        postResponse.title( data.getTitle() );
        postResponse.postDate( data.getPostDate() );

        return postResponse.build();
    }

    @Override
    public List<PostResponse> toPostResponseList(List<Post> postList) {
        if ( postList == null ) {
            return null;
        }

        List<PostResponse> list = new ArrayList<PostResponse>( postList.size() );
        for ( Post post : postList ) {
            list.add( toPostResponse( post ) );
        }

        return list;
    }

    @Override
    public Post toPost(PostRequest postRequest) {
        if ( postRequest == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        post.title( postRequest.getTitle() );
        post.postDate( postRequest.getPostDate() );

        return post.build();
    }
}
