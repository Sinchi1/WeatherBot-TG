package org.truskovski.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.truskovski.store.entity.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather,Long> {
}
