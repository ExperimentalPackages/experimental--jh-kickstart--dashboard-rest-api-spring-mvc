package com.kvmix.dashboard.web.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import com.kvmix.dashboard.domain.Authority;
import com.kvmix.dashboard.repository.AuthorityRepository;
import com.kvmix.dashboard.web.rest.errors.BadRequestAlertException;
import com.kvmix.dashboard.web.util.HeaderUtil;
import com.kvmix.dashboard.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link com.kvmix.dashboard.domain.Authority}.
 */
@RestController
@RequestMapping("/api/authorities")
@Transactional
public class AuthorityResource {

  private static final String ENTITY_NAME = "adminAuthority";
  private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);
  private final AuthorityRepository authorityRepository;
  @Value("${kvmix.clientApp.name}")
  private String applicationName;

  public AuthorityResource(AuthorityRepository authorityRepository) {
    this.authorityRepository = authorityRepository;
  }

  /**
   * {@code POST  /authorities} : Create a new authority.
   *
   * @param authority the authority to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authority, or with status {@code 400 (Bad Request)} if the authority has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<Authority> createAuthority(@Valid @RequestBody Authority authority) throws URISyntaxException {
    log.debug("REST request to save Authority : {}", authority);
    if (authorityRepository.existsById(authority.getName())) {
      throw new BadRequestAlertException("authority already exists", ENTITY_NAME, "idexists");
    }
    authority = authorityRepository.save(authority);
    return ResponseEntity.created(new URI("/api/authorities/" + authority.getName()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, authority.getName()))
        .body(authority);
  }

  /**
   * {@code GET  /authorities} : get all the authorities.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorities in body.
   */
  @GetMapping("")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public List<Authority> getAllAuthorities() {
    log.debug("REST request to get all Authorities");
    return authorityRepository.findAll();
  }

  /**
   * {@code GET  /authorities/:id} : get the "id" authority.
   *
   * @param id the id of the authority to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authority, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<Authority> getAuthority(@PathVariable("id") String id) {
    log.debug("REST request to get Authority : {}", id);
    Optional<Authority> authority = authorityRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(authority);
  }

  /**
   * {@code DELETE  /authorities/:id} : delete the "id" authority.
   *
   * @param id the id of the authority to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<Void> deleteAuthority(@PathVariable("id") String id) {
    log.debug("REST request to delete Authority : {}", id);
    authorityRepository.deleteById(id);
    return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
  }
}