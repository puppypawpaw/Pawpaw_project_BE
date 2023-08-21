package kr.co.pawpaw.api.config.aspect;

import kr.co.pawpaw.api.config.annotation.CheckPermission;
import kr.co.pawpaw.common.exception.common.PermissionRequiredException;
import kr.co.pawpaw.domainrdb.user.domain.Role;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckPermissionAspectTest {
    @Mock
    private UserQuery userQuery;
    private CheckPermissionAspect checkPermissionAspect;
    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private CheckPermission checkPermission;

    @BeforeEach
    void setUp() {
        checkPermissionAspect = new CheckPermissionAspect(userQuery);
    }

    @Test
    @DisplayName("권한 없는 테스트")
    void withoutPermission() {
        //given
        UserId userId = UserId.create();
        Role role = Role.ADMIN;
        when(checkPermission.role()).thenReturn(role);
        when(joinPoint.getArgs()).thenReturn(new Object[]{ userId });
        when(userQuery.existsByUserIdAndRole(eq(userId), eq(role))).thenReturn(false);

        //when
        assertThatThrownBy(() -> checkPermissionAspect.validateRole(joinPoint, checkPermission)).isInstanceOf(PermissionRequiredException.class);

        //then
        verify(checkPermission, times(1)).role();
        verify(joinPoint, times(1)).getArgs();
        verify(userQuery, times(1)).existsByUserIdAndRole(userId, role);
    }

    @Test
    @DisplayName("권한 있는 테스트")
    void withPermission() throws Throwable {
        //given
        UserId userId = UserId.create();
        Role role = Role.ADMIN;
        Object resultExpected = new Object();
        when(checkPermission.role()).thenReturn(role);
        when(joinPoint.getArgs()).thenReturn(new Object[]{ userId });
        when(joinPoint.proceed()).thenReturn(resultExpected);
        when(userQuery.existsByUserIdAndRole(eq(userId), eq(role))).thenReturn(true);

        //when
        Object result = checkPermissionAspect.validateRole(joinPoint, checkPermission);

        //then
        verify(checkPermission, times(1)).role();
        verify(joinPoint, times(1)).getArgs();
        verify(userQuery, times(1)).existsByUserIdAndRole(userId, role);
        verify(joinPoint, times(1)).proceed();

        assertThat(result).isEqualTo(resultExpected);
    }

}