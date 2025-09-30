package com.equipement.Repository;

import com.equipement.entity.Cable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CableRepository extends JpaRepository<Cable, Long> {
}
