package com.multitenant.demo.service;

import com.multitenant.demo.database.entities.Resource;
import com.multitenant.demo.database.repositories.FacultyRepository;
import com.multitenant.demo.dto.faculty.ResourceRequestDTO;
import com.multitenant.demo.dto.faculty.ResourceResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceService {
    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<ResourceResponseDTO> getAllFaculities() {
        return facultyRepository.findAll().stream().map(
                resource -> modelMapper.map(resource, ResourceResponseDTO.class)
        ).collect(Collectors.toList());
    }

    public ResourceResponseDTO addFaculty(ResourceRequestDTO resourceRequestDTO) {
        Resource resource = modelMapper.map(resourceRequestDTO, Resource.class);
        Resource savedResourceEntity = facultyRepository.save(resource);
        return modelMapper.map(savedResourceEntity, ResourceResponseDTO.class);
    }

    public ResourceResponseDTO getFaculty(Long id) {
        Optional<Resource> facultyEntityOpt = facultyRepository.findById(id);
        if (facultyEntityOpt.isEmpty()) {
            throw  new RuntimeException("Faculty not found");
        }
        return modelMapper.map(facultyEntityOpt.get(), ResourceResponseDTO.class);
    }

    public ResourceResponseDTO updateFaculty(Long id, ResourceRequestDTO resourceRequestDTO) {
        Optional<Resource> facultyEntityOpt = facultyRepository.findById(id);
        if (facultyEntityOpt.isPresent()) {
            Resource resourceEntity = facultyEntityOpt.get();
            modelMapper.map(resourceRequestDTO, resourceEntity);
            Resource updatedResourceEntity = facultyRepository.save(resourceEntity);
            return modelMapper.map(updatedResourceEntity, ResourceResponseDTO.class);
        }
        throw  new RuntimeException("Faculty not found");
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }
}
