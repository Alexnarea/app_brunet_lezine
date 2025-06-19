package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.AdministratorDto
import com.example.app_brunet_lezine.entity.Administrator
import org.springframework.stereotype.Component

@Component
object AdministratorMapper {
    fun toEntity(administratorDto: AdministratorDto): Administrator{
        val administrator = Administrator()
        administrator.id = administratorDto.id
        administrator.fullName = administratorDto.fullName
        administrator.nui = administratorDto.nui
        administrator.phone = administratorDto.phone
        administrator.birthdate = administratorDto.birthdate
        administrator.gender = administratorDto.gender
        return administrator
    }

    fun toAdminDto(administrator: Administrator): AdministratorDto{
        val administratorDto = AdministratorDto()
        administratorDto.id = administrator.id
        administratorDto.fullName = administrator.fullName
        administratorDto.nui = administrator.nui
        administratorDto.phone = administrator.phone
        administratorDto.birthdate = administrator.birthdate
        administratorDto.gender = administrator.gender
        return administratorDto
    }
}