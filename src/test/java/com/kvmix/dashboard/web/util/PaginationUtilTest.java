package com.kvmix.dashboard.web.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Tests based on parsing algorithm in app/components/util/pagination-util.service.js
 *
 * @see PaginationUtil
 */
class PaginationUtilTest {

  private static final String BASE_URL = "/api/_search/example";

  private UriComponentsBuilder uriBuilder;

  @BeforeEach
  void init() {
    uriBuilder = UriComponentsBuilder.fromUriString(BASE_URL);
  }

  @Test
  void generatePaginationHttpHeadersTest() {
    Page<String> page = new PageImpl<>(new ArrayList<>(), PageRequest.of(6, 50), 400L);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, page);
    List<String> strHeaders = headers.get(HttpHeaders.LINK);
    assertThat(strHeaders).isNotNull();
    assertThat(strHeaders).hasSize(1);
    String headerData = strHeaders.getFirst();
    assertThat(headerData.split(",")).hasSize(4);
    String expectedData =
        "</api/_search/example?page=7&size=50>; rel=\"next\"," +
        "</api/_search/example?page=5&size=50>; rel=\"prev\"," +
        "</api/_search/example?page=7&size=50>; rel=\"last\"," +
        "</api/_search/example?page=0&size=50>; rel=\"first\"";
    assertThat(headerData).isEqualTo(expectedData);
    List<String> xparamTotalCountHeaders = headers.get("X-Total-Count");
    assertThat(xparamTotalCountHeaders).hasSize(1).extracting(Long::parseLong).containsExactly(400L);
  }

  @Test
  void commaTest() {
    uriBuilder.queryParam("query", "Test1, test2");
    List<String> content = new ArrayList<>();
    Page<String> page = new PageImpl<>(content);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, page);
    List<String> strHeaders = headers.get(HttpHeaders.LINK);
    assertThat(strHeaders).isNotNull();
    assertThat(strHeaders).hasSize(1);
    String headerData = strHeaders.getFirst();
    assertThat(headerData.split(",")).hasSize(2);
    String expectedData =
        "</api/_search/example?query=Test1%2C%20test2&page=0&size=0>; rel=\"last\"," +
        "</api/_search/example?query=Test1%2C%20test2&page=0&size=0>; rel=\"first\"";
    assertThat(headerData).isEqualTo(expectedData);
    List<String> xparamTotalCountHeaders = headers.get("X-Total-Count");
    assertThat(xparamTotalCountHeaders).hasSize(1).extracting(Long::parseLong).containsExactly(0L);
  }

  @Test
  void multiplePagesTest() {
    uriBuilder.queryParam("query", "Test1, test2");
    List<String> content = new ArrayList<>();

    // Page 0
    Page<String> page = new PageImpl<>(content, PageRequest.of(0, 50), 400L);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, page);
    List<String> strHeaders = headers.get(HttpHeaders.LINK);
    assertThat(strHeaders).isNotNull();
    assertThat(strHeaders).hasSize(1);
    String headerData = strHeaders.getFirst();
    assertThat(headerData.split(",")).hasSize(3);
    String expectedData =
        "</api/_search/example?query=Test1%2C%20test2&page=1&size=50>; rel=\"next\"," +
        "</api/_search/example?query=Test1%2C%20test2&page=7&size=50>; rel=\"last\"," +
        "</api/_search/example?query=Test1%2C%20test2&page=0&size=50>; rel=\"first\"";
    assertThat(headerData).isEqualTo(expectedData);
    List<String> xparamTotalCountHeaders = headers.get("X-Total-Count");
    assertThat(xparamTotalCountHeaders).hasSize(1).extracting(Long::parseLong).containsExactly(400L);

    // Page 1
    uriBuilder.queryParam("page", "1");
    page = new PageImpl<>(content, PageRequest.of(1, 50), 400L);
    headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, page);
    strHeaders = headers.get(HttpHeaders.LINK);
    assertThat(strHeaders).isNotNull();
    assertThat(strHeaders).hasSize(1);
    headerData = strHeaders.getFirst();
    assertThat(headerData.split(",")).hasSize(4);
    expectedData = "</api/_search/example?query=Test1%2C%20test2&page=2&size=50>; rel=\"next\"," +
                   "</api/_search/example?query=Test1%2C%20test2&page=0&size=50>; rel=\"prev\"," +
                   "</api/_search/example?query=Test1%2C%20test2&page=7&size=50>; rel=\"last\"," +
                   "</api/_search/example?query=Test1%2C%20test2&page=0&size=50>; rel=\"first\"";
    assertThat(headerData).isEqualTo(expectedData);
    xparamTotalCountHeaders = headers.get("X-Total-Count");
    assertThat(xparamTotalCountHeaders).hasSize(1).extracting(Long::parseLong).containsExactly(400L);

    // Page 6
    uriBuilder.queryParam("page", "6");
    page = new PageImpl<>(content, PageRequest.of(6, 50), 400L);
    headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, page);
    strHeaders = headers.get(HttpHeaders.LINK);
    assertThat(strHeaders).isNotNull();
    assertThat(strHeaders).hasSize(1);
    headerData = strHeaders.getFirst();
    assertThat(headerData.split(",")).hasSize(4);
    expectedData = "</api/_search/example?query=Test1%2C%20test2&page=7&size=50>; rel=\"next\"," +
                   "</api/_search/example?query=Test1%2C%20test2&page=5&size=50>; rel=\"prev\"," +
                   "</api/_search/example?query=Test1%2C%20test2&page=7&size=50>; rel=\"last\"," +
                   "</api/_search/example?query=Test1%2C%20test2&page=0&size=50>; rel=\"first\"";
    assertThat(headerData).isEqualTo(expectedData);
    xparamTotalCountHeaders = headers.get("X-Total-Count");
    assertThat(xparamTotalCountHeaders).hasSize(1).extracting(Long::parseLong).containsExactly(400L);

    // Page 7
    uriBuilder.queryParam("page", "7");
    page = new PageImpl<>(content, PageRequest.of(7, 50), 400L);
    headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, page);
    strHeaders = headers.get(HttpHeaders.LINK);
    assertThat(strHeaders).isNotNull();
    assertThat(strHeaders).hasSize(1);
    headerData = strHeaders.getFirst();
    assertThat(headerData.split(",")).hasSize(3);
    expectedData = "</api/_search/example?query=Test1%2C%20test2&page=6&size=50>; rel=\"prev\"," +
                   "</api/_search/example?query=Test1%2C%20test2&page=7&size=50>; rel=\"last\"," +
                   "</api/_search/example?query=Test1%2C%20test2&page=0&size=50>; rel=\"first\"";
    assertThat(headerData).isEqualTo(expectedData);
  }

  @Test
  void greaterSemicolonTest() {
    uriBuilder.queryParam("query", "Test>;test");
    Page<String> page = new PageImpl<>(new ArrayList<>());
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, page);
    List<String> strHeaders = headers.get(HttpHeaders.LINK);
    assertThat(strHeaders).isNotNull();
    assertThat(strHeaders).hasSize(1);
    String headerData = strHeaders.getFirst();
    assertThat(headerData.split(",")).hasSize(2);
    String[] linksData = headerData.split(",");
    assertThat(linksData).hasSize(2);
    assertThat(linksData[0].split(">;")).hasSize(2);
    assertThat(linksData[1].split(">;")).hasSize(2);
    String expectedData =
        "</api/_search/example?query=Test%3E%3Btest&page=0&size=0>; rel=\"last\"," +
        "</api/_search/example?query=Test%3E%3Btest&page=0&size=0>; rel=\"first\"";
    assertThat(headerData).isEqualTo(expectedData);
    List<String> xparamTotalCountHeaders = headers.get("X-Total-Count");
    assertThat(xparamTotalCountHeaders).hasSize(1).extracting(Long::parseLong).containsExactly(0L);
  }
}
