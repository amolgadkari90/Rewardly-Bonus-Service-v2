package com.rewardly.bonus.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.rewardly.bonus.dto.BonusDto;
import com.rewardly.bonus.entity.Bonus;

@Mapper(componentModel = "Spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BonusMapper {
	
	@Mapping(target = "bonusAmount", source = "totalBonusBeforeTax" )
	Bonus toEntity(BonusDto bonusDto);	
	
	@Mapping(target = "empName", ignore = true)
	BonusDto toDto(Bonus bonus);
	
	List<BonusDto> toDtoList(List<Bonus> bonuses);	

}
