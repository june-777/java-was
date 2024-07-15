package codesquad.servlet.fixture;

import codesquad.domain.model.User;

public class UserFixture {

    public static User createUser1() {
        return new User("아이디1", "비밀번호1", "이름1", "email1@woowah.com");
    }

}
