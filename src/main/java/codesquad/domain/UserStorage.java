package codesquad.domain;

import codesquad.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Long insert(User user);

    Optional<User> selectById(Long id);

    List<User> selectAll();

    void deleteById(Long id);
    
}
