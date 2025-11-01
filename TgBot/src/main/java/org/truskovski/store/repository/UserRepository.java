package org.truskovski.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.truskovski.store.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
