package org.helpdesk.category.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.helpdesk.category.exception.ServiceException;
import org.helpdesk.category.model.document.Category;
import org.helpdesk.category.model.request.CategoryRequestDto;
import org.helpdesk.category.model.response.CategoryListResponseDto;
import org.helpdesk.category.model.response.CategoryResponseDto;
import org.helpdesk.category.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ObjectMapper modelMapper;

    public CategoryResponseDto createCategory(CategoryRequestDto request){
        return
        Optional.ofNullable(request.getParentCategoryId())
                .map(s -> {
                    //if parentCategoryId not null in request check if parent category exist
                    Category parentCategory = getCategoryEntity(request.getParentCategoryId());
                    Category entity= modelMapper.convertValue(request,Category.class);
                    entity.setCustomParentCategoryId(parentCategory);
                    return saveCategory(entity);
                })
                .orElseGet(() -> {
                    Category entity= modelMapper.convertValue(request,Category.class);
                    return saveCategory(entity);
                });
    }

    private CategoryResponseDto saveCategory(Category entity){
        return modelMapper.convertValue(categoryRepo.save(entity), CategoryResponseDto.class);
    }

    public CategoryResponseDto getCategory(String categoryId){
        return modelMapper.convertValue(
                getCategoryEntity(categoryId),
                CategoryResponseDto.class);
    }

    public Category getCategoryEntity(String categoryId){
        return categoryRepo.findById(categoryId)
                .orElseThrow(ServiceException.throwNotFound(categoryId));
    }


    public CategoryResponseDto getCategoryIncludingSubCategories(final String id) {
       return manageSubCategories(getCategoryEntity(id));
    }

    private CategoryResponseDto manageSubCategories(final Category root){
        final Collection<Category> documents = categoryRepo.findByParentCategoryIdLike(root.getId());
        final Map<String, Category> map = new HashMap<>();

        for( final Category document: documents ) {
            String customPath = String.format("%s%s",document.getParentCategoryId().substring(1),document.getId());
            map.put(customPath, document );

            final String path = customPath
                    .substring(0, customPath.lastIndexOf(Category.PATH_SEPARATOR));

            String rootPath = Optional.ofNullable(root.getParentCategoryId())
                    .map(r->String.format("%s%s",r.substring(1),root.getId()))
                    .orElse(root.getId());
            if(path.equals(rootPath)) {
                root.getSubCategoryList().add( document );
            } else {
                final Category parent = map.get( path );
                if( parent != null ) {
                    parent.getSubCategoryList().add( document );
                }
            }
        }
        return modelMapper.convertValue(root,CategoryResponseDto.class);
    }

    public CategoryResponseDto updateCategory(String categoryId,CategoryRequestDto request){
        return categoryRepo.findById(categoryId)
                .map(c-> Optional.ofNullable(request.getParentCategoryId())
                        .map(s -> {
                            //if parentCategoryId not null in request check if parent category exist
                            Category parentCategory = getCategoryEntity(request.getParentCategoryId());
                            c.setCustomParentCategoryId(parentCategory);
                            Optional.ofNullable(request.getCategoryName())
                                    .ifPresent(cName -> c.setCategoryName(cName));
                            return saveCategory(c);
                        })
                        .orElseGet(() -> Optional.ofNullable(request.getCategoryName())
                                .map(cName->{
                                    c.setCategoryName(request.getCategoryName());
                                   return saveCategory(c);
                                })
                                .orElse(modelMapper.convertValue(c,CategoryResponseDto.class))))
                .orElseThrow(ServiceException.throwNotFound(categoryId));
    }

    public void deleteCategory(String categoryId){
        categoryRepo.findById(categoryId)
                .map(c->{
                    // TODO : check for the posts that belong to this category. If it exists then give a message or set the categoryId property of these posts as NULL.
                    // TODO : delete all categories belongs to this category.

                    categoryRepo.deleteByParentCategoryIdLike(c.getId());
                    categoryRepo.delete(c);
                    return c;
                })
                .orElseThrow(ServiceException.throwNotFound(categoryId));
    }

    public List<CategoryResponseDto> getAllMainCategories(){
        return Arrays.stream(modelMapper.convertValue(categoryRepo.findByParentCategoryIdIsNull(), CategoryResponseDto[].class)).collect(Collectors.toList());
    }

    public CategoryListResponseDto getAllCategories(){
        return new CategoryListResponseDto(Arrays.stream(modelMapper.convertValue(categoryRepo.findByParentCategoryIdIsNull().stream().map(category -> manageSubCategories(category)).collect(Collectors.toList()), CategoryResponseDto[].class)).collect(Collectors.toList()));
    }
/*
    public CategoryResponseDto createSubCategory(String mainCategoryId,SubCategoryDto request){
        return categoryRepo.findById(mainCategoryId)
                .map(mainCategory->{
                    Category matchCategory = HierarchicalCategorySearchUtil.searchSubNode(mainCategory,request.getParentCategoryId());
                    if (matchCategory.getSubCategoryList()==null){
                        matchCategory.setSubCategoryList(new HashSet<>());
                    }

                    matchCategory.getSubCategoryList().addAll(modelMapper.convertValue(request,Category.class).getSubCategoryList());
                    return saveCategory(mainCategory);
                })
                .orElseThrow(ServiceException.throwNotFound(mainCategoryId));
    }

    public CategoryResponseDto updateSubCategory(String mainCategoryId,String subCategoryId, UpdateCategoryDto request){
        return categoryRepo.findById(mainCategoryId)
                .map(mainCategory->{
                    Category matchCategory = HierarchicalCategorySearchUtil.searchSubNode(mainCategory,subCategoryId);
                    matchCategory.setCategoryName(request.getCategoryName());
                    return modelMapper.convertValue(saveCategory(mainCategory),CategoryResponseDto.class);
                })
                .orElseThrow(ServiceException.throwNotFound(mainCategoryId));
    }

    public CategoryResponseDto deleteSubCategory(String mainCategoryId,String subCategoryId){
        return categoryRepo.findById(mainCategoryId)
                .map(mainCategory->{
                    Category parentMatchCategory = HierarchicalCategorySearchUtil.searchParentNode(mainCategory,subCategoryId);
                    Category matchCategory = parentMatchCategory.getSubCategoryList().stream()
                            .filter(p->p.getId().equals(subCategoryId)).findAny().get();
                    // TODO : check for the posts that belong to this category. If it exists then give a message or set the categoryId property of these posts as NULL.
                    parentMatchCategory.getSubCategoryList().remove(matchCategory);
                    return modelMapper.convertValue(saveCategory(mainCategory),CategoryResponseDto.class);
                })
                .orElseThrow(ServiceException.throwNotFound(mainCategoryId));
    }
*/
}
