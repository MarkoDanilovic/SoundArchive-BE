package com.example.soundarchive.mapper;

import com.example.soundarchive.exception.InternalServerErrorException;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ObjectUtilMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ObjectUtilMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <S, T> T map(S source, Class<T> targetClass) {

        try {
            //   modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(source, targetClass);
        } catch (IllegalArgumentException e) {
            throw new InternalServerErrorException(
                    "IllegalArgumentException exception occurred in ObjectUtilMapper: " + e.getMessage());
        } catch (ConfigurationException e) {
            throw new InternalServerErrorException(
                    "ConfigurationException exception occurred in ObjectUtilMapper: " + e.getMessage());
        } catch (MappingException e) {
            throw new InternalServerErrorException(
                    "MappingException exception occurred in ObjectUtilMapper: " + e.getMessage());
        }
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        try {
            return source.stream()
                    .map(element -> modelMapper.map(element, targetClass))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new InternalServerErrorException(
                    "IllegalArgumentException exception occurred in ObjectUtilMapper: " + e.getMessage());
        } catch (ConfigurationException e) {
            throw new InternalServerErrorException(
                    "ConfigurationException exception occurred in ObjectUtilMapper: " + e.getMessage());
        } catch (MappingException e) {
            throw new InternalServerErrorException(
                    "MappingException exception occurred in ObjectUtilMapper: " + e.getMessage());
        }
    }
}
