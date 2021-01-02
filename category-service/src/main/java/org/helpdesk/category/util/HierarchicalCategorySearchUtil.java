package org.helpdesk.category.util;

import org.bson.types.ObjectId;
import org.helpdesk.category.exception.ServiceException;
import org.helpdesk.category.model.document.Category;
import org.helpdesk.category.model.document.HierarchicalCategory;

import java.util.*;
import java.util.function.Function;

public class HierarchicalCategorySearchUtil {

    public static void setCommonProperties(HierarchicalCategory category, Optional<String> auditor){
        breadthFirstSearchAlgorithm(category,null,setNodeCommonPropertiesFunction(category,auditor));
    }

    public static HierarchicalCategory searchSubNode(HierarchicalCategory rootNode, String parentNodeId){
        HierarchicalCategory result = breadthFirstSearchAlgorithm(rootNode,parentNodeId,searchSubNodeFunction(parentNodeId));
        return Optional.ofNullable(result)
                .orElseThrow(ServiceException.throwNotFound(parentNodeId));
    }

    public static HierarchicalCategory searchParentNode(HierarchicalCategory rootNode, String subNodeId){
        HierarchicalCategory result = breadthFirstSearchAlgorithm(rootNode,subNodeId,searchParentNodeFunction(subNodeId));
        return Optional.ofNullable(result)
                .orElseThrow(ServiceException.throwNotFound(subNodeId));
    }


    private static HierarchicalCategory breadthFirstSearchAlgorithm(HierarchicalCategory rootNode, String parentNodeId, Function<HierarchicalCategory,Optional<HierarchicalCategory>> f){
        Queue<HierarchicalCategory> queue = new LinkedList<>();
        queue.add(rootNode);
        Set<HierarchicalCategory> alreadyVisited = new HashSet<>();
        alreadyVisited.add(rootNode);

        while (!queue.isEmpty()){
            HierarchicalCategory currentSubCategory=queue.remove();

            Optional<HierarchicalCategory> fOut = f.apply(currentSubCategory);
            if (fOut.isPresent()){
                return currentSubCategory;
            }

            if (currentSubCategory.getSubCategoryList()!=null){
                currentSubCategory.getSubCategoryList().stream().forEach(subCategory -> {
                    if(subCategory!=null && !alreadyVisited.contains(subCategory)){
                        queue.add(subCategory);
                        alreadyVisited.add(subCategory);
                    }
                });
            }
        }
        return null;
    }

    private static Function<HierarchicalCategory, Optional<HierarchicalCategory>> searchSubNodeFunction(String parentNodeId){
        return (currentSubCategory)->{
            if (currentSubCategory.getId().equals(parentNodeId)){
                return Optional.ofNullable(currentSubCategory);
            }
            return Optional.empty();
        };
    }

    private static Function<HierarchicalCategory, Optional<HierarchicalCategory>> searchParentNodeFunction(String subNodeId){
        return (currentSubCategory)->{
            if (currentSubCategory.getSubCategoryList()!=null
                    && currentSubCategory.getSubCategoryList().stream()
                    .anyMatch(p->p.getId().equals(subNodeId))
            ){
                return Optional.ofNullable(currentSubCategory);
            }
            return Optional.empty();
        };
    }

    private static Function<HierarchicalCategory, Optional<HierarchicalCategory>> setNodeCommonPropertiesFunction(HierarchicalCategory category, Optional<String> createdBy){
        return (c)->{
            if(!category.equals(c)){
                c.setId(Optional.ofNullable(c.getId()).orElse(new ObjectId().toString()));
                c.setCreationDate(Optional.ofNullable(c.getCreationDate()).orElse(new Date()));
                c.setCreatedBy(Optional.ofNullable(c.getCreatedBy()).orElse(createdBy.orElse(null)));
            }

            return Optional.empty();
        };
    }

}