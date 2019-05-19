package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.domain.UserBilling;
import com.example.demo.domain.UserPayment;
import com.example.demo.domain.UserShipping;
import com.example.demo.domain.security.PasswordResetToken;
import com.example.demo.domain.security.UserRole;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    
    Optional<User> findById(long id);

    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokenForUser(final User user, final String token);

    User findByUsername(String username);

    User findByEmail(String email);

    User createUser(User user, Set<UserRole> userRoles) throws Exception;

    User save(User user);

    void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
    
    void updateUserShipping(UserShipping userShipping, User user);
    
//    void setUserDefaultPayment(long id, User user);

//    public void setUserDefaultShipping(long defaultShippingId, User user);
    
    
}
