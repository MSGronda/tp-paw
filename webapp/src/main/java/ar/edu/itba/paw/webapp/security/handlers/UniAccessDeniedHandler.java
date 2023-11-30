package ar.edu.itba.paw.webapp.security.handlers;

import ar.edu.itba.paw.webapp.dto.ErrorInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import org.springframework.security.access.AccessDeniedException;


@Component
public class UniAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        ErrorInfoDto errorInfoDto = new ErrorInfoDto();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorInfoDto.setStatus(HttpStatus.FORBIDDEN.value());
        errorInfoDto.setTitle(HttpStatus.FORBIDDEN.getReasonPhrase());
        errorInfoDto.addMessage(exception.getMessage());
        errorInfoDto.setPath(request.getRequestURI());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), errorInfoDto);
    }

}
