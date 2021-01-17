package com.automart.user.domain;

// ouath provider 별로 로그인 후 전달해주는 data가 다르기 때문에 로그인시 provider를 확인해서 각각의 process를 거침
public enum AuthProvider {
    local,
    google,
    naver,
    kakao
}
