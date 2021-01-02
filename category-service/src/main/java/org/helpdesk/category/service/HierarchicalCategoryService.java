package org.helpdesk.category.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.helpdesk.category.exception.ServiceException;
import org.helpdesk.category.model.document.Category;
import org.helpdesk.category.model.document.HierarchicalCategory;
import org.helpdesk.category.model.request.CategoryDto;
import org.helpdesk.category.model.request.SubCategoryDto;
import org.helpdesk.category.model.request.UpdateCategoryDto;
import org.helpdesk.category.model.response.CategoryResponseDto;
import org.helpdesk.category.repository.CategoryRepo;
import org.helpdesk.category.repository.HierarchicalCategoryRepo;
import org.helpdesk.category.util.HierarchicalCategorySearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
@Service
public class HierarchicalCategoryService {

    @Autowired
    private HierarchicalCategoryRepo categoryRepo;

    @Autowired
    private ObjectMapper modelMapper;

    public CategoryResponseDto createCategory(CategoryDto request){
        HierarchicalCategory entity= modelMapper.convertValue(request, HierarchicalCategory.class);
        return saveCategory(entity);
    }

    private CategoryResponseDto saveCategory(HierarchicalCategory entity){
        return modelMapper.convertValue(categoryRepo.save(entity), CategoryResponseDto.class);
    }

    public CategoryResponseDto getCategory(String categoryId){
        return modelMapper.convertValue(
                categoryRepo.findById(categoryId)
                            .orElseThrow(ServiceException.throwNotFound(categoryId)),
                CategoryResponseDto.class);
    }

    public CategoryResponseDto updateCategory(String categoryId,CategoryDto request){
        return categoryRepo.findById(categoryId)
                .map(c->{
                    c.setCategoryName(request.getCategoryName());
                    return modelMapper.convertValue(saveCategory(c),CategoryResponseDto.class);
                })
                .orElseThrow(ServiceException.throwNotFound(categoryId));
    }

    public void deleteCategory(String categoryId){
        categoryRepo.findById(categoryId)
                .map(c->{
                    c.setCategoryName(categoryId);
                    // TODO : check for the posts that belong to this category. If it exists then give a message or set the categoryId property of these posts as NULL.
                    categoryRepo.delete(c);
                    return c;
                })
                .orElseThrow(ServiceException.throwNotFound(categoryId));
    }

    public List<CategoryResponseDto> getAllCategories(){
        return Arrays.stream(modelMapper.convertValue(categoryRepo.findAll(), CategoryResponseDto[].class)).collect(Collectors.toList());
    }

    public CategoryResponseDto createSubCategory(String mainCategoryId,SubCategoryDto request){
        return categoryRepo.findById(mainCategoryId)
                .map(mainCategory->{
                    HierarchicalCategory matchCategory = HierarchicalCategorySearchUtil.searchSubNode(mainCategory,request.getParentCategoryId());
                    if (matchCategory.getSubCategoryList()==null){
                        matchCategory.setSubCategoryList(new HashSet<>());
                    }

                    matchCategory.getSubCategoryList().addAll(modelMapper.convertValue(request,HierarchicalCategory.class).getSubCategoryList());
                    return saveCategory(mainCategory);
                })
                .orElseThrow(ServiceException.throwNotFound(mainCategoryId));
    }

    public CategoryResponseDto updateSubCategory(String mainCategoryId,String subCategoryId, UpdateCategoryDto request){
        return categoryRepo.findById(mainCategoryId)
                .map(mainCategory->{
                    HierarchicalCategory matchCategory = HierarchicalCategorySearchUtil.searchSubNode(mainCategory,subCategoryId);
                    matchCategory.setCategoryName(request.getCategoryName());
                    return modelMapper.convertValue(saveCategory(mainCategory),CategoryResponseDto.class);
                })
                .orElseThrow(ServiceException.throwNotFound(mainCategoryId));
    }

    public CategoryResponseDto deleteSubCategory(String mainCategoryId,String subCategoryId){
        return categoryRepo.findById(mainCategoryId)
                .map(mainCategory->{
                    HierarchicalCategory parentMatchCategory = HierarchicalCategorySearchUtil.searchParentNode(mainCategory,subCategoryId);
                    HierarchicalCategory matchCategory = parentMatchCategory.getSubCategoryList().stream()
                            .filter(p->p.getId().equals(subCategoryId)).findAny().get();
                    // TODO : check for the posts that belong to this category. If it exists then give a message or set the categoryId property of these posts as NULL.
                    parentMatchCategory.getSubCategoryList().remove(matchCategory);
                    return modelMapper.convertValue(saveCategory(mainCategory),CategoryResponseDto.class);
                })
                .orElseThrow(ServiceException.throwNotFound(mainCategoryId));
    }

}
