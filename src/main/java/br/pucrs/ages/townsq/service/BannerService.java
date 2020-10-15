package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Banner;
import br.pucrs.ages.townsq.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BannerService {

    private final BannerRepository repo;

    @Autowired
    public BannerService(BannerRepository r){
        this.repo = r;
    }

    public Optional<Banner> getActiveBanner(){
        return repo.getBannerByIsActiveEquals(1);
    }

    public Banner save(Banner ads) {
        updateAllBannersToInactive();
        ads.setIsActive(1);
        return repo.save(ads);
    }

    public List<Banner> getAllBanners() {
        return repo.findBannersByIsActiveEquals(1);
    }

    public void updateAllBannersToInactive() {
        List<Banner> list = getAllBanners();
        list.forEach(banner -> {
            banner.setIsActive(0);
            repo.save(banner);
        });
    }
}
