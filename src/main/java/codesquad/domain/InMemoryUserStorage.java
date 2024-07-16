package codesquad.domain;

import codesquad.domain.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

public class InMemoryUserStorage {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    private InMemoryUserStorage() {
    }

    private static class SingletonHolder {
        private static final InMemoryUserStorage INSTANCE = new InMemoryUserStorage();
    }

    public static InMemoryUserStorage getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void save(User user) {
        long newId = idGenerator.incrementAndGet();
        user.setPrimaryKey(newId);
        users.putIfAbsent(newId, user);
    }

    public Optional<User> findByUserId(String userId) {
        return users.values().stream()
                .filter(isEqualsUserId(userId))
                .findFirst();
    }

    private Predicate<User> isEqualsUserId(String userId) {
        return user -> user.getUserId().equals(userId);
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(users.values()));
    }

    public void delete(Long userId) {
        users.remove(userId);
    }

}
