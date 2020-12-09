/**
 * 반복되는 코드를 개선
 * SessionUser user = (SessionUser) httpSession.getAttribute("user");
 * 다른 컨트롤러와 메소드에서 세션값이 필요하면 그때마다 직접 세션에서 값으로 가져와야 함
 * 이 부분을 메소드 인자로 세션값을 받을 수 있도록 변경
 */
package com.automart.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 메소드의 파라미터로 선언된 객체에서만 사용할 수 있음
@Retention(RetentionPolicy.RUNTIME) // 컴파일 이후에도 JVM에 의해서 참조가 가능
public @interface LoginUser {
}