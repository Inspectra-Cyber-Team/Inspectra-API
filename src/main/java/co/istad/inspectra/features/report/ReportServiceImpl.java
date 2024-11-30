package co.istad.inspectra.features.report;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.Report;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.blog.BlogRepository;
import co.istad.inspectra.features.report.dto.ReportRequest;
import co.istad.inspectra.features.report.dto.ReportResponse;
import co.istad.inspectra.features.report.dto.ReportResponseDetails;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.ReportMapper;
import co.istad.inspectra.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;

    private final BlogRepository blogRepository;

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;
    @Override
    public ReportResponse createReport(ReportRequest reportRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        System.out.println("reportRequest = " + reportRequest);

        if (customUserDetails == null)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to perform this action");
        }

        String userUuid = customUserDetails.getUserUuid();

        User user = userRepository.findUserByUuid(userUuid);

        if (user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        System.out.println("reportRequest.blogUuid() = " + reportRequest.blogUuid());

        Blog blog = blogRepository.findByUuid(reportRequest.blogUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        Report report = reportMapper.mapToReport(reportRequest);

        report.setUuid(UUID.randomUUID().toString());
        report.setUser(user);
        report.setBlog(blog);

        reportRepository.save(report);


        return reportMapper.mapToReportResponse(report);
    }

    @Override
    public Page<ReportResponseDetails> getAllReportDetails(int page, int size) {

        if (page < 0 || size < 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page or size");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Report> reports = reportRepository.findAll(pageRequest);

        return reports.map(reportMapper::mapToReportResponseDetails);

    }

    @Override
    public Page<ReportResponse> getAllReport(int page, int size) {

        if (page < 0 || size < 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page or size");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Report> reports = reportRepository.findAll(pageRequest);

        return reports.map(reportMapper::mapToReportResponse);
    }

    @Override
    public ReportResponseDetails getReportByUuid(String uuid) {

        if(uuid == null || uuid.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uuid is required");
        }

        Report report = reportRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        return reportMapper.mapToReportResponseDetails(report);
    }

    @Override
    public void deleteReport(String uuid) {

            if(uuid == null || uuid.isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uuid is required");
            }

            Report report = reportRepository.findByUuid(uuid).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

            reportRepository.delete(report);
    }
}
