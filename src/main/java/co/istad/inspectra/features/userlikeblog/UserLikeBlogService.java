package co.istad.inspectra.features.userlikeblog;

import co.istad.inspectra.features.userlikeblog.dto.UserLikeBlogResponse;

import java.util.List;

public interface UserLikeBlogService {

    List<UserLikeBlogResponse> getLikedUsers(String blogUuid);

}
