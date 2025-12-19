package np.com.sajansubba.notes.repositories;

import np.com.sajansubba.notes.models.AppRole;
import np.com.sajansubba.notes.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
