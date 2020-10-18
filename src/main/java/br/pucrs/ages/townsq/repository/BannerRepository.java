package br.pucrs.ages.townsq.repository;

import br.pucrs.ages.townsq.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    Optional<Banner> getBannerByIsActiveEquals(int active);

    List<Banner> findBannersByIsActiveEquals(int active);
}
