package co.istad.inspectra.features.documentcategory;

import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryDetails;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryRequest;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryResponse;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryUpdate;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * This interface is used to define the service methods for the DocumentCategory entity.
 * @author : lyhou
 * @since 1.0
 * @version 1.0
 */

public interface DocumentCategoryService {

    /**
     * This method is used to create a new DocumentCategory.
     * @param documentCategoryRequest : The DocumentCategoryRequest object.
     * @return : The DocumentCategoryResponse object.
     */
    DocumentCategoryResponse create(DocumentCategoryRequest documentCategoryRequest);

    /**
     * This method is used to update an existing DocumentCategory.
     * @param documentCategoryUpdate : The DocumentCategoryRequest object.
     * @return : The DocumentCategoryResponse object.
     */
    DocumentCategoryResponse update(String uuid, DocumentCategoryUpdate documentCategoryUpdate);

    /**
     * This method is used to delete an existing DocumentCategory.
     * @param uuid : The UUID of the DocumentCategory.
     */
    void delete(String uuid);

    /**
     * This method is used to get a DocumentCategory by its UUID.
     * @param uuid : The UUID of the DocumentCategory.
     * @return : The DocumentCategoryResponse object.
     */
    DocumentCategoryResponse getByUuid(String uuid);

    /**
     * This method is used to get a DocumentCategory by its name.
     * @param name : The name of the DocumentCategory.
     * @return : The DocumentCategoryResponse object.
     */
    DocumentCategoryResponse getByName(String name);

    /**
     * This method is used to get all DocumentCategories.
     * @return : The list of DocumentCategoryResponse objects.
     */
    List<DocumentCategoryResponse> getAll();

    /**
     * This method is used to get all DocumentCategories by page.
     * @param page : The page number.
     * @param size : The page size.
     * @return : The Page object of DocumentCategoryResponse.
     */

    Page<DocumentCategoryResponse> getAll(int page, int size);



    List<DocumentCategoryDetails> getAllDocumentCategories();


}
