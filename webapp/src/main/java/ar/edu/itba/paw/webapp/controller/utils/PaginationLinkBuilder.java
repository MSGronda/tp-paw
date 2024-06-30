package ar.edu.itba.paw.webapp.controller.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class PaginationLinkBuilder {

    private PaginationLinkBuilder() {

    }

    public static void getResponsePaginationLinks(Response.ResponseBuilder response, UriInfo uriInfo, int currentPage, int lastPage) {
        response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", currentPage).build(), "current");
        response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", 1).build(), "first");
        response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", lastPage).build(), "last");
        if ( currentPage != 1 ) {
            response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", currentPage - 1).build(), "prev");
        }
        if ( currentPage != lastPage ) {
            response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", currentPage + 1).build(), "next");
        }
    }
}
