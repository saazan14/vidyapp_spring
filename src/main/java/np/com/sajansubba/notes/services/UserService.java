package np.com.sajansubba.notes.services;

import np.com.sajansubba.notes.dtos.UserDTO;
import np.com.sajansubba.notes.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void updateUserRole(Long userId, String roleName);
    UserDTO getUserById(Long id);
    User findByUsername(String username);
}
