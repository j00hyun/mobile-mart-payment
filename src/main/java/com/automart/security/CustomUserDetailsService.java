package com.automart.security;

import com.automart.exception.NotFoundUserException;
import com.automart.user.domain.User;
import com.automart.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Security가 User 클래스를 사용해 Authentication을 사용
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // User정보를 DB에서 가져온다
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 유저를 찾을 수 없습니다."));

        // DB에서 가져온 User 정보는 UserPrincipal 클래스로 변경해 Spring Security로 전달한다.
        // UserPrincipal은 Spring Security의 UserDetails를 implements 하였으므로, 이제 Spring Security는 User 클래스를 사용해 Authentication을 사용 할수 있게 되었다.
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserByNo(int no) {
        User user = userRepository.findByNo(no).orElseThrow(
                () -> new NotFoundUserException("해당 고유번호의 유저를 찾을 수 없습니다.")
        );

        return UserPrincipal.create(user);
    }
}
