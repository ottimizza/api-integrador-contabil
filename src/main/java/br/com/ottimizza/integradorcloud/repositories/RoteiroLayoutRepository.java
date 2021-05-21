package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.roteiro_layouts.RoteiroLayout;
import br.com.ottimizza.integradorcloud.domain.models.roteiro_layouts.RoteiroLayoutId;
import feign.Param;

@Repository
public interface RoteiroLayoutRepository extends JpaRepository<RoteiroLayout, RoteiroLayoutId> {
    
    @Query(value = "SELECT rl.fk_layout_id FROM roteiros_layouts rl WHERE rl.fk_roteiro_id = :roteiroId", nativeQuery = true)
    List<BigInteger> getLayoutsIdByRoteiroId(@Param("roteiroId") BigInteger roteiroId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM roteiros_layouts rl WHERE rl.fk_roteiro_id = :roteiroId AND rl.fk_layout_id = :layoutId", nativeQuery = true)
    void deletaPorRoteiroIdELayoutId(@Param("roteiroId") BigInteger roteiroId, @Param("layoutId") BigInteger layoutId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO roteiros_layouts (fk_roteiro_id,fk_layout_id) VALUES  (:roteiroId, :layoutId)", nativeQuery = true)
    void inserir(@Param("roteiroId") BigInteger roteiroId, @Param("layoutId") BigInteger layoutId);
    
}
