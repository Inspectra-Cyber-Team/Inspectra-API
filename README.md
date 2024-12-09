

# AuthRestController

The `AuthRestController` is a Spring Boot REST controller that handles authentication-related operations. It provides endpoints for user login, registration, token refresh, account verification, OTP resending, password reset, password change, and user initialization.
<details>
## Endpoints

### Login
- **URL:** `POST /api/v1/auth/login`
- **Description:** Authenticates a user and returns an authentication token.
- **Request Body:** `AuthRequest`
- **Response:** `AuthResponse`

### Register
- **URL:** `POST /api/v1/auth/register`
- **Description:** Registers a new user.
- **Request Body:** `UserRegisterDto`
- **Response:** `ResponseUserDto`

### Refresh Token
- **URL:** `POST /api/v1/auth/refreshToken`
- **Description:** Refreshes the authentication token.
- **Request Body:** `RefreshTokenRequest`
- **Response:** `AuthResponse`

### Verify Account
- **URL:** `PUT /api/v1/auth/verify-account`
- **Description:** Verifies a user's account.
- **Request Body:** `VerifyAccountRequest`
- **Response:** `String`

### Resend OTP
- **URL:** `PUT /api/v1/auth/resend-otp`
- **Description:** Resends the OTP to the user's email.
- **Request Param:** `email`
- **Response:** `String`

### Reset Password
- **URL:** `PUT /api/v1/auth/reset-password`
- **Description:** Resets the user's password.
- **Request Body:** `ForgetPassword`
- **Response:** `String`

### Change Password
- **URL:** `PUT /api/v1/auth/change-password`
- **Description:** Changes the user's password.
- **Request Body:** `ChangePassword`
- **Response:** `String`

### Initialize User
- **URL:** `POST /api/v1/auth/init-user`
- **Description:** Initializes a user with authentication details.
- **Request Body:** `InitUserRequest`
- **Response:** `ResponseUserDto`

## Request and Response DTOs

- **AuthRequest:** Contains the user's login credentials.
- **AuthResponse:** Contains the authentication token and user details.
- **UserRegisterDto:** Contains the user's registration details.
- **ResponseUserDto:** Contains the user's details.
- **RefreshTokenRequest:** Contains the refresh token.
- **VerifyAccountRequest:** Contains the account verification details.
- **ForgetPassword:** Contains the password reset details.
- **ChangePassword:** Contains the new password details.
- **InitUserRequest:** Contains the user initialization details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).


</details>

# BlogController

The `BlogController` is a Spring Boot REST controller that handles blog-related operations. It provides endpoints for creating, retrieving, updating, liking, verifying, and deleting blogs.

<details>

## Endpoints

### Create Blog
- **URL:** `POST /api/v1/blogs`
- **Description:** Creates a new blog.
- **Request Body:** `BlogRequestDto`
- **Response:** `BlogResponseDto`

### Get All Verified Blogs
- **URL:** `GET /api/v1/blogs/verified`
- **Description:** Retrieves all verified blogs.
- **Request Params:** `page` (default: 0), `size` (default: 25)
- **Response:** `Page<BlogResponseDto>`

### Get All Blogs
- **URL:** `GET /api/v1/blogs`
- **Description:** Retrieves all blogs.
- **Request Params:** `page` (default: 0), `size` (default: 25)
- **Response:** `Page<BlogResponseDto>`

### Like Blog
- **URL:** `POST /api/v1/blogs/{blogUuid}/like`
- **Description:** Likes a blog.
- **Path Variable:** `blogUuid`
- **Response:** `String`

### Get Blog
- **URL:** `GET /api/v1/blogs/{blogUuid}`
- **Description:** Retrieves a specific blog.
- **Path Variable:** `blogUuid`
- **Response:** `BlogResponseDto`

### Update Blog
- **URL:** `PUT /api/v1/blogs/{blogUuid}`
- **Description:** Updates a specific blog.
- **Path Variable:** `blogUuid`
- **Request Body:** `BlogUpdateRequest`
- **Response:** `BlogResponseDto`

### Get Blogs by User
- **URL:** `GET /api/v1/blogs/user/{userUuid}`
- **Description:** Retrieves all blogs by a specific user.
- **Path Variable:** `userUuid`
- **Response:** `List<BlogResponseDto>`

### Delete Blog
- **URL:** `DELETE /api/v1/blogs/{blogUuid}`
- **Description:** Deletes a specific blog.
- **Path Variable:** `blogUuid`
- **Response:** `String`

### Verify Blog
- **URL:** `PUT /api/v1/blogs/{blogUuid}/verify`
- **Description:** Verifies a specific blog.
- **Path Variable:** `blogUuid`
- **Response:** `BlogResponseDto`

### Unverify Blog
- **URL:** `PUT /api/v1/blogs/{blogUuid}/unverified`
- **Description:** Unverifies a specific blog.
- **Path Variable:** `blogUuid`
- **Response:** `BlogResponseDto`

## Request and Response DTOs

- **BlogRequestDto:** Contains the blog creation details.
- **BlogResponseDto:** Contains the blog details.
- **BlogUpdateRequest:** Contains the blog update details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).
</details>



# CodeController

The `CodeController` is a Spring Boot REST controller that handles code-related operations. It provides endpoints for retrieving component trees and sub-component trees of a project.
<details>

## Endpoints

### Get Component Tree
- **URL:** `GET /api/v1/codes/component-tree/{projectName}`
- **Description:** Retrieves the component tree of a specified project.
- **Path Variable:** `projectName`
- **Request Params:**
    - `page` (default: 1)
    - `size` (default: 25)
    - `query` (default: "")
- **Response:** `Flux<Object>`

### Get Sub Component Tree
- **URL:** `GET /api/v1/codes/sub-component-tree/{projectName}`
- **Description:** Retrieves the sub-component tree of a specified project.
- **Path Variable:** `projectName`
- **Request Params:**
    - `page` (default: 1)
    - `size` (default: 100)
    - `query` (default: "")
- **Response:** `Flux<Object>`

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).
</details>


# CommentController

The `CommentController` is a Spring Boot REST controller that handles comment-related operations. It provides endpoints for creating, retrieving, updating, liking, and deleting comments.
<details>
## Endpoints

### Create Comment
- **URL:** `POST /api/v1/comments`
- **Description:** Creates a new comment.
- **Request Body:** `CommentRequest`
- **Response:** `BaseRestResponse<CommentResponse>`

### Get All Comments
- **URL:** `GET /api/v1/comments`
- **Description:** Retrieves all comments.
- **Request Params:** `page` (default: 0), `size` (default: 25)
- **Response:** `Page<CommentResponse>`

### Like Comment
- **URL:** `POST /api/v1/comments/{commentUuid}/like`
- **Description:** Likes a comment.
- **Path Variable:** `commentUuid`
- **Response:** `BaseRestResponse<String>`

### Update Comment
- **URL:** `PUT /api/v1/comments/{uuid}`
- **Description:** Updates a specific comment.
- **Path Variable:** `uuid`
- **Request Body:** `UpdateComment`
- **Response:** `BaseRestResponse<CommentResponse>`

### Delete Comment
- **URL:** `DELETE /api/v1/comments/{uuid}`
- **Description:** Deletes a specific comment.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<String>`

### Get Comments by Blog UUID
- **URL:** `GET /api/v1/comments/{blogUuid}/blog`
- **Description:** Retrieves all comments for a specific blog.
- **Path Variable:** `blogUuid`
- **Request Params:** `page` (default: 0), `size` (default: 25)
- **Response:** `Page<CommentResponse>`

## Request and Response DTOs

- **CommentRequest:** Contains the comment creation details.
- **CommentResponse:** Contains the comment details.
- **UpdateComment:** Contains the comment update details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).
</details>


# DocumentCategoryController

The `DocumentCategoryController` is a Spring Boot REST controller that handles document category-related operations. It provides endpoints for creating, retrieving, updating, and deleting document categories.

<details>

## Endpoints

### Get All Document Categories
- **URL:** `GET /api/v1/document-categories/all`
- **Description:** Retrieves all document categories.
- **Response:** `BaseRestResponse<List<DocumentCategoryResponse>>`

### Get All Document Categories by Page
- **URL:** `GET /api/v1/document-categories`
- **Description:** Retrieves all document categories by page.
- **Request Params:** `page` (default: 0), `size` (default: 25)
- **Response:** `Page<DocumentCategoryResponse>`

### Get Document Category by UUID
- **URL:** `GET /api/v1/document-categories/{uuid}`
- **Description:** Retrieves a specific document category by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<DocumentCategoryResponse>`

### Get Document Category by Name
- **URL:** `GET /api/v1/document-categories/name/{name}`
- **Description:** Retrieves a specific document category by name.
- **Path Variable:** `name`
- **Response:** `BaseRestResponse<DocumentCategoryResponse>`

### Create Document Category
- **URL:** `POST /api/v1/document-categories`
- **Description:** Creates a new document category.
- **Request Body:** `DocumentCategoryRequest`
- **Response:** `BaseRestResponse<DocumentCategoryResponse>`

### Update Document Category
- **URL:** `PUT /api/v1/document-categories/{uuid}`
- **Description:** Updates an existing document category.
- **Path Variable:** `uuid`
- **Request Body:** `DocumentCategoryUpdate`
- **Response:** `BaseRestResponse<DocumentCategoryResponse>`

### Delete Document Category
- **URL:** `DELETE /api/v1/document-categories/{uuid}`
- **Description:** Deletes a specific document category.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<Object>`

### Get All Document Categories Details
- **URL:** `GET /api/v1/document-categories/details`
- **Description:** Retrieves all document categories details.
- **Response:** `BaseRestResponse<List<DocumentCategoryDetails>>`

## Request and Response DTOs

- **DocumentCategoryRequest:** Contains the document category creation details.
- **DocumentCategoryResponse:** Contains the document category details.
- **DocumentCategoryUpdate:** Contains the document category update details.
- **DocumentCategoryDetails:** Contains detailed information about document categories.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).
</details>



# DocumentController

The `DocumentController` is a Spring Boot REST controller that handles document-related operations. It provides endpoints for creating, retrieving, updating, and deleting documents.
<details>
## Endpoints

### Create Document
- **URL:** `POST /api/v1/documents`
- **Description:** Creates a new document.
- **Request Body:** `DocumentRequest`
- **Response:** `BaseRestResponse<DocumentResponse>`

### Get Document by UUID
- **URL:** `GET /api/v1/documents/{uuid}`
- **Description:** Retrieves a specific document by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<DocumentResponse>`

### Get All Documents
- **URL:** `GET /api/v1/documents/all`
- **Description:** Retrieves all documents.
- **Response:** `BaseRestResponse<List<DocumentResponse>>`

### Update Document
- **URL:** `PUT /api/v1/documents/{uuid}`
- **Description:** Updates a specific document.
- **Path Variable:** `uuid`
- **Request Body:** `DocumentUpdate`
- **Response:** `BaseRestResponse<DocumentResponse>`

### Delete Document
- **URL:** `DELETE /api/v1/documents/{uuid}`
- **Description:** Deletes a specific document.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<Void>`

### Get All Documents by Page
- **URL:** `GET /api/v1/documents`
- **Description:** Retrieves all documents by page.
- **Request Params:** `page` (default: 0), `size` (default: 25)
- **Response:** `Page<DocumentResponse>`

### Get Documents by Category
- **URL:** `GET /api/v1/documents/category/{categoryUuid}`
- **Description:** Retrieves all documents for a specific category.
- **Path Variable:** `categoryUuid`
- **Response:** `BaseRestResponse<List<DocumentResponse>>`

## Request and Response DTOs

- **DocumentRequest:** Contains the document creation details.
- **DocumentResponse:** Contains the document details.
- **DocumentUpdate:** Contains the document update details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).
</details>



# FaqController

The `FaqController` is a Spring Boot REST controller that handles FAQ-related operations. It provides endpoints for creating, retrieving, updating, and deleting FAQs.

<details>

## Endpoints

### Get All FAQs
- **URL:** `GET /api/v1/faqs`
- **Description:** Retrieves all FAQs.
- **Response:** `BaseRestResponse<List<FaqResponse>>`

### Get All FAQs by Pagination
- **URL:** `GET /api/v1/faqs/pagination`
- **Description:** Retrieves all FAQs by pagination.
- **Request Params:** `page` (default: 0), `size` (default: 25)
- **Response:** `Page<FaqResponse>`

### Get FAQ by UUID
- **URL:** `GET /api/v1/faqs/{uuid}`
- **Description:** Retrieves a specific FAQ by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<FaqResponse>`

### Get FAQ by Question
- **URL:** `GET /api/v1/faqs/question/{question}`
- **Description:** Retrieves a specific FAQ by question.
- **Path Variable:** `question`
- **Response:** `BaseRestResponse<FaqResponse>`

### Get FAQ by Answer
- **URL:** `GET /api/v1/faqs/answer/{answer}`
- **Description:** Retrieves a specific FAQ by answer.
- **Path Variable:** `answer`
- **Response:** `BaseRestResponse<FaqResponse>`

### Create FAQ
- **URL:** `POST /api/v1/faqs`
- **Description:** Creates a new FAQ.
- **Request Body:** `FaqRequest`
- **Response:** `BaseRestResponse<FaqResponse>`

### Update FAQ
- **URL:** `PUT /api/v1/faqs/{uuid}`
- **Description:** Updates an existing FAQ.
- **Path Variable:** `uuid`
- **Request Body:** `FaqUpdateRequest`
- **Response:** `BaseRestResponse<FaqResponse>`

### Delete FAQ
- **URL:** `DELETE /api/v1/faqs/{uuid}`
- **Description:** Deletes a specific FAQ.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<Void>`

## Request and Response DTOs

- **FaqRequest:** Contains the FAQ creation details.
- **FaqResponse:** Contains the FAQ details.
- **FaqUpdateRequest:** Contains the FAQ update details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>


# FeedbackController

The `FeedbackController` is a Spring Boot REST controller that handles feedback-related operations. It provides endpoints for creating, retrieving, updating, and deleting feedback.

<details>

## Endpoints

### Get All Feedbacks
- **URL:** `GET /api/v1/feedbacks`
- **Description:** Retrieves all feedbacks.
- **Request Params:** `page` (default: 0), `size` (default: 25)
- **Response:** `Page<FeedbackResponse>`

### Get Feedback by UUID
- **URL:** `GET /api/v1/feedbacks/{uuid}`
- **Description:** Retrieves a specific feedback by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<FeedbackResponse>`

### Create Feedback
- **URL:** `POST /api/v1/feedbacks`
- **Description:** Creates a new feedback.
- **Request Body:** `FeedbackRequest`
- **Response:** `BaseRestResponse<FeedbackResponse>`

### Update Feedback
- **URL:** `PUT /api/v1/feedbacks/{uuid}`
- **Description:** Updates an existing feedback.
- **Path Variable:** `uuid`
- **Request Body:** `FeedBackUpdate`
- **Response:** `BaseRestResponse<FeedbackResponse>`

### Delete Feedback
- **URL:** `DELETE /api/v1/feedbacks/{uuid}`
- **Description:** Deletes a specific feedback.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<Void>`

### Get Feedback Details
- **URL:** `GET /api/v1/feedbacks/details/{uuid}`
- **Description:** Retrieves detailed information about a specific feedback.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<FeedbackResponseDetails>`

### Get All Feedbacks (No Pagination)
- **URL:** `GET /api/v1/feedbacks/all`
- **Description:** Retrieves all feedbacks without pagination.
- **Response:** `BaseRestResponse<List<FeedbackResponse>>`

## Request and Response DTOs

- **FeedbackRequest:** Contains the feedback creation details.
- **FeedbackResponse:** Contains the feedback details.
- **FeedBackUpdate:** Contains the feedback update details.
- **FeedbackResponseDetails:** Contains detailed information about feedback.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# FileController

The `FileController` is a Spring Boot REST controller that handles file-related operations. It provides endpoints for uploading, downloading, and deleting files.

## Endpoints

<details>

### Upload Single File
- **URL:** `POST /api/v1/files`
- **Description:** Uploads a single file.
- **Request Part:** `file` (MultipartFile)
- **Response:** `BaseRestResponse<FileResponse>`

### Upload Multiple Files
- **URL:** `POST /api/v1/files/multiple`
- **Description:** Uploads multiple files.
- **Request Part:** `files` (MultipartFile[])
- **Response:** `BaseRestResponse<List<String>>`

### Download File
- **URL:** `GET /api/v1/files/download/{fileName}`
- **Description:** Downloads a file.
- **Path Variable:** `fileName`
- **Response:** `ResponseEntity<?>`

### Delete File
- **URL:** `DELETE /api/v1/files/{fileName}`
- **Description:** Deletes a file.
- **Path Variable:** `fileName`
- **Response:** `String`

## Request and Response DTOs

- **FileResponse:** Contains the file details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>


# GitController

The `GitController` is a Spring Boot REST controller that handles Git-related operations. It provides endpoints for retrieving repositories, branches, and files from Git.

## Endpoints

<details>


### Get All Repositories by User
- **URL:** `GET /api/v1/gits/repos/{username}`
- **Description:** Retrieves all repositories by user and only public repositories.
- **Path Variable:** `username`
- **Request Params:** `projectName` (optional)
- **Response:** `Flux<GitRepositoryResponse>`

### Get Repository by User and Project Name
- **URL:** `GET /api/v1/gits/repos/{username}/{projectName}`
- **Description:** Retrieves a specific repository by user and project name.
- **Path Variable:** `username`, `projectName`
- **Response:** `Flux<GitRepositoryResponse>`

### Get All Repositories by Access Token
- **URL:** `GET /api/v1/gits/repos`
- **Description:** Retrieves all repositories by access token.
- **Request Params:** `accessToken`
- **Response:** `Flux<GitRepositoryResponse>`

### Get All Branches by User and Project Name
- **URL:** `GET /api/v1/gits/branches/{username}/{projectName}`
- **Description:** Retrieves all branches by user and project name.
- **Path Variable:** `username`, `projectName`
- **Response:** `Flux<GitBranchResponseDto>`

### Get All Branches by Git URL
- **URL:** `GET /api/v1/gits/branches`
- **Description:** Retrieves all branches by Git URL.
- **Request Params:** `gitUrl`
- **Response:** `Flux<GitBranchResponseDto>`

### List Files in Repository
- **URL:** `GET /api/v1/gits/list_files`
- **Description:** Lists all files and directories in a repository.
- **Request Params:** `gitUrl`, `branch`
- **Response:** `Map<String, Object>`

### Clone Repository
- **URL:** `POST /api/v1/gits/clone`
- **Description:** Clones a repository.
- **Request Params:** `url`, `branch`
- **Response:** `String`

## Request and Response DTOs

- **GitRepositoryResponse:** Contains the repository details.
- **GitBranchResponseDto:** Contains the branch details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# IssueController

The `IssueController` is a Spring Boot REST controller that handles issue-related operations. It provides endpoints for retrieving and filtering issues in a project.

<details>

## Endpoints

### Get All Issues in Project
- **URL:** `GET /api/v1/issues`
- **Description:** Retrieves all issues in a project.
- **Request Params:**
    - `projectName` (required)
    - `page` (default: 0)
    - `size` (default: 25)
    - `cleanCodeAttributeCategories` (optional)
    - `impactSoftwareQualities` (optional)
    - `impactSeverities` (optional)
    - `scopes` (optional)
    - `types` (optional)
    - `languages` (optional)
    - `directories` (optional)
    - `rules` (optional)
    - `issuesStatuses` (optional)
    - `tags` (optional)
    - `files` (optional)
    - `createdInLast` (optional)
- **Response:** `BaseRestResponse<Object>`

### Get Issue Detail by Issue Key
- **URL:** `GET /api/v1/issues/search`
- **Description:** Retrieves specific issue details by issue key.
- **Request Params:**
    - `issueKey` (required)
    - `ruleKey` (required)
- **Response:** `BaseRestResponse<Object>`

### Get Issues by Project Filter
- **URL:** `GET /api/v1/issues/filter`
- **Description:** Retrieves issues in a project with pagination.
- **Request Params:**
    - `projectName` (required)
    - `page` (default: 0)
    - `size` (default: 25)
- **Response:** `BaseRestResponse<Object>`

### Get Issue Details by Issue Key
- **URL:** `GET /api/v1/issues/{issueKey}`
- **Description:** Retrieves detailed information about a specific issue.
- **Path Variable:** `issueKey`
- **Response:** `BaseRestResponse<Object>`

### Get Issues by Project Name (Flux)
- **URL:** `GET /api/v1/issues/flux`
- **Description:** Retrieves issues in a project using Flux.
- **Request Params:** `projectName` (required)
- **Response:** `Flux<IssuesResponse>`

### Get Component Issues Message
- **URL:** `GET /api/v1/issues/message`
- **Description:** Retrieves all issue messages in a component.
- **Request Params:** `projectName` (required)
- **Response:** `Flux<ListIssueResponseMessage>`

## Request and Response DTOs

- **IssuesResponse:** Contains the issue details.
- **IssuesWrapperResponse:** Contains a wrapper for issue details.
- **ListIssueResponseMessage:** Contains the issue message details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# PdfExportController

The `PdfExportController` is a Spring Boot REST controller that handles PDF export-related operations. It provides an endpoint for generating PDFs based on a project name.

<details>

## Endpoints

### Generate PDF
- **URL:** `GET /api/v1/pdf/{projectName}`
- **Description:** Generates a PDF for the specified project.
- **Path Variable:** `projectName`
- **Response:** `ResponseEntity<byte[]>`

## Request and Response DTOs

- **PdfExportService:** Service responsible for generating the PDF.

## Response Format

All responses are wrapped in a `ResponseEntity` object which contains the following fields:
- **status:** HTTP status code.
- **body:** The response data (PDF content as byte array).
- **headers:** HTTP headers (optional).

</details>



# ProjectController

The `ProjectController` is a Spring Boot REST controller that handles project-related operations. It provides endpoints for creating, retrieving, updating, and deleting projects, as well as other project-specific actions.

## Endpoints

<details>

### Create Project
- **URL:** `POST /api/v1/projects`
- **Description:** Creates a new project.
- **Request Body:** `ProjectRequest`
- **Response:** `BaseRestResponse<ProjectResponse>`

### Get All Projects
- **URL:** `GET /api/v1/projects`
- **Description:** Retrieves all projects.
- **Response:** `BaseRestResponse<List<ProjectResponse>>`

### Get Project by Project Name
- **URL:** `GET /api/v1/projects/{projectName}`
- **Description:** Retrieves a specific project by project name.
- **Path Variable:** `projectName`
- **Response:** `BaseRestResponse<ProjectResponse>`

### Delete Project by Project Name
- **URL:** `DELETE /api/v1/projects/{projectName}`
- **Description:** Deletes a specific project by project name.
- **Path Variable:** `projectName`
- **Response:** `BaseRestResponse<Object>`

### Update Project Name
- **URL:** `PUT /api/v1/projects`
- **Description:** Updates an existing project.
- **Request Body:** `ProjectUpdateDto`
- **Response:** `BaseRestResponse<ProjectResponse>`

### Find Project by User UUID
- **URL:** `GET /api/v1/projects/user/{uuid}`
- **Description:** Retrieves projects by user UUID.
- **Path Variable:** `uuid`
- **Request Params:** `page` (default: 0), `size` (default: 10)
- **Response:** `Flux<ProjectOverview>`

### Favorite Project
- **URL:** `POST /api/v1/projects/favorite`
- **Description:** Marks a project as favorite.
- **Request Params:** `projectName`
- **Response:** `BaseRestResponse<Object>`

### Get All Projects (Alternative)
- **URL:** `GET /api/v1/projects/{projectName}/all`
- **Description:** Retrieves all projects (alternative endpoint).
- **Path Variable:** `projectName`
- **Response:** `BaseRestResponse<Object>`

### Get Favorite Projects
- **URL:** `GET /api/v1/projects/favorite`
- **Description:** Retrieves favorite projects.
- **Response:** `Flux<Object>`

### Remove Favorite Project
- **URL:** `POST /api/v1/projects/remove/favorite`
- **Description:** Removes a project from favorites.
- **Request Params:** `projectKey`
- **Response:** `BaseRestResponse<Object>`

### Get Security Hotspot
- **URL:** `GET /api/v1/projects/security-hotspot/{projectName}`
- **Description:** Retrieves security hotspots for a project.
- **Path Variable:** `projectName`
- **Response:** `Flux<Object>`

### Get Project Branch
- **URL:** `GET /api/v1/projects/branch/{projectName}`
- **Description:** Retrieves branches for a project.
- **Path Variable:** `projectName`
- **Response:** `Flux<Object>`

### Get Project Warning
- **URL:** `GET /api/v1/projects/warning/{projectName}`
- **Description:** Retrieves warnings for a project.
- **Path Variable:** `projectName`
- **Response:** `Flux<Object>`

### Get Project Overview
- **URL:** `GET /api/v1/projects/overview/{projectName}`
- **Description:** Retrieves an overview of a project.
- **Path Variable:** `projectName`
- **Response:** `Mono<ProjectOverview>`

### Get Project Details
- **URL:** `GET /api/v1/projects/details`
- **Description:** Retrieves detailed information about a project.
- **Request Params:** `projectName`
- **Response:** `Flux<Object>`

### Get Facets
- **URL:** `GET /api/v1/projects/facets`
- **Description:** Retrieves facets for projects.
- **Response:** `Flux<Object>`

### Create Project for Non-Login User
- **URL:** `POST /api/v1/projects/create/non_user`
- **Description:** Creates a project for a non-login user.
- **Response:** `BaseRestResponse<List<ProjectResponse>>`

## Request and Response DTOs

- **ProjectRequest:** Contains the project creation details.
- **ProjectResponse:** Contains the project details.
- **ProjectUpdateDto:** Contains the project update details.
- **ProjectOverview:** Contains an overview of the project.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# ReplyController

The `ReplyController` is a Spring Boot REST controller that handles reply-related operations. It provides endpoints for creating, retrieving, updating, and deleting replies, as well as other reply-specific actions.

<details>

## Endpoints

### Create Reply
- **URL:** `POST /api/v1/replies`
- **Description:** Creates a new reply.
- **Request Body:** `ReplyRequest`
- **Response:** `BaseRestResponse<ReplyResponse>`

### Get All Replies
- **URL:** `GET /api/v1/replies`
- **Description:** Retrieves all replies.
- **Response:** `BaseRestResponse<List<ReplyResponse>>`

### Get Reply by UUID
- **URL:** `GET /api/v1/replies/{replyUuid}`
- **Description:** Retrieves a specific reply by UUID.
- **Path Variable:** `replyUuid`
- **Response:** `BaseRestResponse<ReplyResponse>`

### Get Replies by Comment ID
- **URL:** `GET /api/v1/replies/{commentId}`
- **Description:** Retrieves replies by comment ID.
- **Path Variable:** `commentId`
- **Response:** `BaseRestResponse<List<ReplyResponse>>`

### Like Reply
- **URL:** `POST /api/v1/replies/{replyUuid}/like`
- **Description:** Likes a reply.
- **Path Variable:** `replyUuid`
- **Response:** `BaseRestResponse<String>`

### Delete Reply
- **URL:** `DELETE /api/v1/replies/{replyUuid}`
- **Description:** Deletes a specific reply by UUID.
- **Path Variable:** `replyUuid`
- **Response:** `BaseRestResponse<String>`

### Update Reply
- **URL:** `PUT /api/v1/replies/{replyUuid}/reply`
- **Description:** Updates an existing reply.
- **Path Variable:** `replyUuid`
- **Request Body:** `ReplyUpdate`
- **Response:** `BaseRestResponse<ReplyResponse>`

## Request and Response DTOs

- **ReplyRequest:** Contains the reply creation details.
- **ReplyResponse:** Contains the reply details.
- **ReplyUpdate:** Contains the reply update details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# ReportController

The `ReportController` is a Spring Boot REST controller that handles report-related operations. It provides endpoints for creating, retrieving, and deleting reports, as well as retrieving report details.

<details>

## Endpoints

### Create Report
- **URL:** `POST /api/v1/reports`
- **Description:** Creates a new report.
- **Request Body:** `ReportRequest`
- **Response:** `BaseRestResponse<ReportResponse>`

### Get All Report Details
- **URL:** `GET /api/v1/reports/details`
- **Description:** Retrieves all report details.
- **Request Params:**
    - `page` (default: 0)
    - `size` (default: 25)
- **Response:** `Page<ReportResponseDetails>`

### Get All Reports
- **URL:** `GET /api/v1/reports`
- **Description:** Retrieves all reports.
- **Request Params:**
    - `page` (default: 0)
    - `size` (default: 25)
- **Response:** `Page<ReportResponse>`

### Get Report by UUID
- **URL:** `GET /api/v1/reports/{uuid}`
- **Description:** Retrieves a specific report by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<ReportResponseDetails>`

### Delete Report by UUID
- **URL:** `DELETE /api/v1/reports/{uuid}`
- **Description:** Deletes a specific report by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<ReportResponse>`

## Request and Response DTOs

- **ReportRequest:** Contains the report creation details.
- **ReportResponse:** Contains the report details.
- **ReportResponseDetails:** Contains detailed information about the report.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# RoleController

The `RoleController` is a Spring Boot REST controller that handles role-related operations. It provides endpoints for creating, retrieving, updating, and deleting roles.

<details>

## Endpoints

### Get All User Roles
- **URL:** `GET /api/v1/roles`
- **Description:** Retrieves all user roles.
- **Response:** `BaseRestResponse<List<RoleResponse>>`

### Create Role
- **URL:** `POST /api/v1/roles`
- **Description:** Creates a new role.
- **Request Body:** `RoleRequest`
- **Response:** `BaseRestResponse<RoleResponse>`

### Get Role by UUID
- **URL:** `GET /api/v1/roles/{uuid}`
- **Description:** Retrieves a specific role by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<RoleResponse>`

### Update Role
- **URL:** `PUT /api/v1/roles/{uuid}`
- **Description:** Updates an existing role.
- **Path Variable:** `uuid`
- **Request Body:** `RoleRequest`
- **Response:** `BaseRestResponse<RoleResponse>`

### Delete Role
- **URL:** `DELETE /api/v1/roles/{uuid}`
- **Description:** Deletes a specific role by UUID.
- **Path Variable:** `uuid`
- **Response:** `void`

## Request and Response DTOs

- **RoleRequest:** Contains the role creation and update details.
- **RoleResponse:** Contains the role details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# RuleController

The `RuleController` is a Spring Boot REST controller that handles rule-related operations. It provides endpoints for retrieving rules, rule details, languages, and rule repositories.

<details>

## Endpoints

### Get All Rules
- **URL:** `GET /api/v1/rules`
- **Description:** Retrieves all rules.
- **Request Params:**
    - `page` (default: 1)
    - `pageSize` (default: 10)
- **Response:** `Mono<Object>`

### Get Rule Details
- **URL:** `GET /api/v1/rules/{ruleKey}`
- **Description:** Retrieves details of a specific rule by rule key.
- **Path Variable:** `ruleKey`
- **Response:** `Flux<RulesResponseDto>`

### Get All Languages
- **URL:** `GET /api/v1/rules/languages`
- **Description:** Retrieves all languages.
- **Response:** `Mono<Object>`

### Get Rules Repository
- **URL:** `GET /api/v1/rules/rules-repository`
- **Description:** Retrieves all rules repository.
- **Response:** `Mono<Object>`

## Request and Response DTOs

- **RulesResponseDto:** Contains the rule details.
- **RuleLanguageCountResponse:** Contains the language count details.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>


# ScanController

The `ScanController` is a Spring Boot REST controller that handles scan-related operations. It provides endpoints for scanning projects for both logged-in users and non-users.

<details>

## Endpoints

### Scan Project
- **URL:** `POST /api/v1/scan`
- **Description:** Scans a project for logged-in users.
- **Request Body:** `ScanningRequestDto`
- **Response:** `BaseRestResponse<String>`

### Scan Project for Non-User
- **URL:** `POST /api/v1/scan/non-user`
- **Description:** Scans a project for non-users.
- **Request Body:** `ScanForNonUserRequest`
- **Response:** `BaseRestResponse<String>`

## Request and Response DTOs

- **ScanningRequestDto:** Contains the scanning request details for logged-in users.
- **ScanForNonUserRequest:** Contains the scanning request details for non-users.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# SourceController

The `SourceController` is a Spring Boot REST controller that handles source-related operations. It provides endpoints for retrieving code snippet issues, source code, and source code lines.

<details>

## Endpoints

### Get Code Snippet Issues
- **URL:** `GET /api/v1/sources/{issueKey}`
- **Description:** Retrieves code snippet issues using the issue key.
- **Path Variable:** `issueKey`
- **Response:** `BaseRestResponse<Object>`

### Get Source Code
- **URL:** `GET /api/v1/sources/components/{componentKey}`
- **Description:** Retrieves the source code of a project using the component key.
- **Path Variable:** `componentKey`
- **Response:** `Flux<Object>`

### Get Source Code Lines
- **URL:** `GET /api/v1/sources/lines/{componentKey}`
- **Description:** Retrieves the source code lines of a project using the component key.
- **Path Variable:** `componentKey`
- **Response:** `Flux<Object>`

## Request and Response DTOs

- **BaseRestResponse:** Contains the response details including status, data, message, and timestamp.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# TopicController

The `TopicController` is a Spring Boot REST controller that handles topic-related operations. It provides endpoints for creating, retrieving, and retrieving details of topics.

<details>

## Endpoints

### Create Topic
- **URL:** `POST /api/v1/topics`
- **Description:** Creates a new topic.
- **Request Body:** `TopicRequest`
- **Response:** `BaseRestResponse<TopicResponse>`

### Get All Topics
- **URL:** `GET /api/v1/topics`
- **Description:** Retrieves all topics.
- **Request Params:**
    - `page` (default: 0)
    - `size` (default: 25)
- **Response:** `Page<TopicResponse>`

### Get All Topics Details
- **URL:** `GET /api/v1/topics/{topicName}`
- **Description:** Retrieves all topic details by topic name.
- **Path Variable:** `topicName`
- **Request Params:**
    - `page` (default: 0)
    - `size` (default: 25)
- **Response:** `Page<TopicResponseDetails>`

## Request and Response DTOs

- **TopicRequest:** Contains the topic creation details.
- **TopicResponse:** Contains the topic details.
- **TopicResponseDetails:** Contains detailed information about the topic.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# UserRestController

The `UserRestController` is a Spring Boot REST controller that handles user-related operations. It provides endpoints for creating, retrieving, updating, and deleting users, as well as blocking and unblocking users.

<details>

## Endpoints

### Find User by UUID
- **URL:** `GET /api/v1/users/{uuid}`
- **Description:** Retrieves a specific user by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<ResponseUserDto>`

### Get All Users by Page
- **URL:** `GET /api/v1/users`
- **Description:** Retrieves all users with pagination.
- **Request Params:**
    - `page` (default: 0)
    - `size` (default: 25)
- **Response:** `Page<ResponseUserDto>`

### Get All Users
- **URL:** `GET /api/v1/users/list`
- **Description:** Retrieves all users.
- **Response:** `BaseRestResponse<List<ResponseUserDto>>`

### Delete User by UUID
- **URL:** `DELETE /api/v1/users/{uuid}`
- **Description:** Deletes a specific user by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<ResponseUserDto>`

### Update Profile
- **URL:** `PUT /api/v1/users/profile`
- **Description:** Updates the profile of the authenticated user.
- **Request Body:** `UpdateUserDto`
- **Response:** `BaseRestResponse<ResponseUserDto>`

### Block User
- **URL:** `PUT /api/v1/users/block/{uuid}`
- **Description:** Blocks a specific user by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<ResponseUserDto>`

### Unblock User
- **URL:** `PUT /api/v1/users/unblock/{uuid}`
- **Description:** Unblocks a specific user by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<ResponseUserDto>`

### Get Users by Filter
- **URL:** `POST /api/v1/users/filter`
- **Description:** Retrieves users based on filter criteria.
- **Request Body:** `BaseSpecification.FilterDto`
- **Response:** `BaseRestResponse<List<ResponseUserDto>>`

### Create Admin
- **URL:** `POST /api/v1/users/admin`
- **Description:** Creates a new admin user.
- **Request Body:** `UserRegisterDto`
- **Response:** `BaseRestResponse<ResponseUserDto>`

### Get User Details
- **URL:** `GET /api/v1/users/details/{uuid}`
- **Description:** Retrieves detailed information of a specific user by UUID.
- **Path Variable:** `uuid`
- **Response:** `BaseRestResponse<UserDetailsResponse>`

### Get All Admin Users
- **URL:** `GET /api/v1/users/admins`
- **Description:** Retrieves all admin users with pagination.
- **Request Params:**
    - `page` (default: 0)
    - `size` (default: 25)
- **Response:** `Page<ResponseUserDto>`

## Request and Response DTOs

- **ResponseUserDto:** Contains the user response details.
- **UpdateUserDto:** Contains the user update details.
- **UserDetailsResponse:** Contains detailed information about the user.
- **UserRegisterDto:** Contains the user registration details.
- **BaseSpecification.FilterDto:** Contains the filter criteria.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

</details>



# UserLikeBlogController

The `UserLikeBlogController` is a Spring Boot REST controller that handles operations related to users liking blogs. It provides an endpoint for retrieving all users who liked a specific blog.

<details>

## Endpoints

### Get All Users Who Liked a Blog
- **URL:** `GET /api/v1/user_like_blog/{blogUuid}`
- **Description:** Retrieves all users who liked a specific blog.
- **Path Variable:** `blogUuid`
- **Response:** `BaseRestResponse<List<UserLikeBlogResponse>>`

## Request and Response DTOs

- **UserLikeBlogResponse:** Contains the response details of users who liked the blog.

## Response Format

All responses are wrapped in a `BaseRestResponse` object which contains the following fields:
- **status:** HTTP status code.
- **data:** The response data.
- **message:** A message describing the result.
- **timestamp:** The time the response was generated (optional).

<br><br>
# UserLikeCommentController

The `UserLikeCommentController` is a Spring Boot REST controller that handles operations related to users liking comments. It provides an endpoint for retrieving all users who liked a specific comment.

## Endpoints

### Get All Users Who Liked a Comment
- **URL:** `GET /api/v1/user-like-comments/{commentUUid}`
- **Description:** Retrieves all users who liked a specific comment.
- **Path Variable:** `commentUUid`
- **Request Params:**
    - `page` (default: 0)
    - `size` (default: 25)
- **Response:** `Page<UserLikeCommentResponse>`

## Request and Response DTOs

- **UserLikeCommentResponse:** Contains the response details of users who liked the comment.

## Response Format

All responses are wrapped in a `Page` object which contains the following fields:
- **content:** The list of `UserLikeCommentResponse` objects.
- **pageable:** Information about the pagination.
- **totalPages:** The total number of pages.
- **totalElements:** The total number of elements.
- **last:** Whether it is the last page.
- **size:** The size of the page.
- **number:** The current page number.
- **sort:** Sorting information.
- **first:** Whether it is the first page.
- **numberOfElements:** The number of elements in the current page.

</details>



# UserLikeReplyCommentController

The `UserLikeReplyCommentController` is a Spring Boot REST controller that handles operations related to users liking reply comments. It provides an endpoint for retrieving all users who liked a specific reply comment.

<details>

## Endpoints

### Get All Users Who Liked a Reply Comment
- **URL:** `GET /api/v1/user-like-reply-comment/{replyUuid}`
- **Description:** Retrieves all users who liked a specific reply comment.
- **Path Variable:** `replyUuid`
- **Request Params:**
    - `page` (default: 0)
    - `size` (default: 10)
- **Response:** `Page<UserLikeReplyCommentResponse>`

## Request and Response DTOs

- **UserLikeReplyCommentResponse:** Contains the response details of users who liked the reply comment.

## Response Format

All responses are wrapped in a `Page` object which contains the following fields:
- **content:** The list of `UserLikeReplyCommentResponse` objects.
- **pageable:** Information about the pagination.
- **totalPages:** The total number of pages.
- **totalElements:** The total number of elements.
- **last:** Whether it is the last page.
- **size:** The size of the page.
- **number:** The current page number.
- **sort:** Sorting information.
- **first:** Whether it is the first page.
- **numberOfElements:** The number of elements in the current page.

</details>

