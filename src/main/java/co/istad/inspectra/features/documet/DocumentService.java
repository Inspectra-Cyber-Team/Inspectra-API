package co.istad.inspectra.features.documet;

import co.istad.inspectra.features.documet.dto.DocumentRequest;
import co.istad.inspectra.features.documet.dto.DocumentResponse;
import co.istad.inspectra.features.documet.dto.DocumentUpdate;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DocumentService
 * this service use for document related operations
 * @author : lyhou
 * @version 1.0
 */
public interface DocumentService {

    /**
     * createDocument
     * this method use for create document
     * @param documentRequest use for create document
     * @return {@link DocumentResponse}
     */
    DocumentResponse createDocument(DocumentRequest documentRequest);

    /**
     * get all document
     * @return {@link DocumentResponse}
     *
     */

    List<DocumentResponse> getAllDocument();


    /**
     * get document by uuid
     * @param uuid use for get document
     * @return {@link DocumentResponse}
     */

    DocumentResponse getDocumentByUuid(String uuid);


    /**
     * update document
     * @param documentUpdate use for update document
     * @return {@link DocumentResponse}
     */

    DocumentResponse updateDocument(String uuid, DocumentUpdate documentUpdate);

    /**
     * delete document
     * @param uuid use for delete document
     */

    void deleteDocument(String uuid);


    /**
     * get all document by page
     * @param page use for get document by page
     *  @param size use for get document by size
     * @return {@link org.springframework.data.domain.Page<DocumentResponse>}
     */


    Page<DocumentResponse> getAllDocumentByPage(int page, int size);


    /**
     * get document by category
     * @param categoryUuid use for get document by category
     * @return {@link DocumentResponse}
     */
    List<DocumentResponse> getDocumentByCategory(String categoryUuid);








}
