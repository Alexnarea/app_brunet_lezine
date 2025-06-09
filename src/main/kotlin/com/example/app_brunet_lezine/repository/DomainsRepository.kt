package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.Domains
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DomainsRepository: JpaRepository<Domains, Long>{
}