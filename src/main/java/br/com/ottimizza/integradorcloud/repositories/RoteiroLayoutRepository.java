package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.roteiro_layouts.RoteiroLayout;
import br.com.ottimizza.integradorcloud.domain.models.roteiro_layouts.RoteiroLayoutId;
import feign.Param;

@Repository
public interface RoteiroLayoutRepository extends JpaRepository<RoteiroLayout, RoteiroLayoutId> {
    
    @Query(value = "SELECT rl.fk_layout_id FROM roteiros_layouts rl WHERE rl.fk_roteiro_id = :roteiroId", nativeQuery = true)
    List<BigInteger> getLayoutsIdByRoteiroId(@Param("roteiroId") BigInteger roteiroId);

    @Query(value = "DELETE FROM roteiros_layouts rl WHERE rl.fk_roteiro_id = :roteiroId AND rl.fk_layout_id = :layoutId", nativeQuery = true)
    void deletaPorRoteiroIdELayoutId(@Param("roteiroId") BigInteger roteiroId, @Param("layoutId") BigInteger layoutId);
}
