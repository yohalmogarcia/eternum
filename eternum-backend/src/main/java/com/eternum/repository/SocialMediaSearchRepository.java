package com.eternum.repository;

import com.eternum.entity.SocialMediaSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialMediaSearchRepository extends JpaRepository<SocialMediaSearch, Integer> {

    List<SocialMediaSearch> findByFkUserOrderBySearchDateDesc(Integer fkUser);

    List<SocialMediaSearch> findByFkUserAndPlatformSearchedOrderBySearchDateDesc(Integer fkUser, String platformSearched);

}
